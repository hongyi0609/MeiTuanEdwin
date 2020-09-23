package com.interview;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.BaseFragment;
import com.interview.invocation.DialogApi;
import com.interview.invocation.DialogProxyHandler;
import com.interview.invocation.ProxyDialog;
import com.meituan.R;

import java.lang.reflect.Proxy;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Edwin on 2020/9/10.
 *
 * @author Edwin
 */

public class InterviewFragment extends BaseFragment {

    private static final String TAG = InterviewFragment.class.getSimpleName();

    private Context mContext;

    private TextView mTextView;

    public static BaseFragment createFragment(){
        return new InterviewFragment();
    }

    @Override
    public void onAttach(@org.jetbrains.annotations.Nullable Context context) {
        super.onAttach(context);
        if (mContext == null) {
            mContext = context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.interview_fragment, container, false);
        mTextView = view.findViewById(R.id.invocation_handler_text_view);
        mTextView.setOnClickListener(l);
        view.findViewById(R.id.countdown_latch_text_view).setOnClickListener(l);
        view.findViewById(R.id.thread_pool_add_worker_text_view).setOnClickListener(l);
        return view;
    }

    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.invocation_handler_text_view:
                    showInvocationHandlerDialog();
                    break;
                case R.id.countdown_latch_text_view:
                    CountdownLatchDemo();
                    break;
                case R.id.thread_pool_add_worker_text_view:
                    ThreadPoolExecutorCheckAddWorker();
                    break;
                case R.id.reentrant_lock_text_view:
                    ReentrantLockDemo();
                    break;

            }
        }
    };

    private void showInvocationHandlerDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
        mBuilder.setTitle("Interview");
        mBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ProxyDialog proxyDialog = new ProxyDialog();
                DialogProxyHandler dialogProxyHandler = new DialogProxyHandler(proxyDialog);
                DialogApi dialogApi = (DialogApi) Proxy.newProxyInstance(
                        proxyDialog.getClass().getClassLoader(),
                        proxyDialog.getClass().getInterfaces(),
                        dialogProxyHandler);
                Log.d(TAG, "动态代理前———————— ");
                proxyDialog.handleDialog();
                proxyDialog.handleDialogTitle("你大爷！");
                Log.d(TAG, "动态代理后———————— ");
                dialogApi.handleDialog();
                dialogApi.handleDialogTitle("你大爷！");
                dialog.dismiss();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog alertDialog = mBuilder.create();
        alertDialog.show();
    }

    private void CountdownLatchDemo() {
        CountDownLatch mCountDownLatch = new CountDownLatch(2);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    mCountDownLatch.countDown();
                } catch (InterruptedException e) {
                    mCountDownLatch.countDown();
                }
            }
        });
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    mCountDownLatch.countDown();
                } catch (InterruptedException e) {
                    mCountDownLatch.countDown();
                }
            }
        });

        try {
            mCountDownLatch.await(); // 阻塞当前线程
        } catch (InterruptedException e) {
            Log.d(TAG,"countDownLatchException = " + e.getMessage());
        }
    }

    /**
     * 验证死锁
     */
    private void ThreadPoolExecutorCheckAddWorker() {
        final boolean[] isAddWorker = {false};
        while (!isAddWorker[0]) {
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        isAddWorker[0] = true;
                    }
                }
            });
        }
    }

    private void ReentrantLockDemo() {
        ReentrantLock lock = new ReentrantLock();
        try {
            lock.lock();
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Log.d(TAG,"ReentrantLock#InterruptedException = " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}
