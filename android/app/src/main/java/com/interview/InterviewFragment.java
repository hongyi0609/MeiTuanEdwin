package com.interview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.base.BaseFragment;
import com.interview.invocation.DialogApi;
import com.interview.invocation.DialogProxyHandler;
import com.interview.invocation.ProxyDialog;
import com.interview.service.MyIntentService;
import com.interview.service.MyService;
import com.meituan.R;

import java.lang.reflect.Proxy;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
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

    private View root;

    private TextView mTextView;

    public static BaseFragment createFragment() {
        return new InterviewFragment();
    }

    @Override
    public void onAttach(@org.jetbrains.annotations.Nullable Context context) {
        super.onAttach(context);
        if (mContext == null) {
            mContext = getContext();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.interview_fragment, container, false);
        mTextView = root.findViewById(R.id.invocation_handler_text_view);
        mTextView.setOnClickListener(l);
        root.findViewById(R.id.countdown_latch_text_view).setOnClickListener(l);
        root.findViewById(R.id.thread_pool_add_worker_text_view).setOnClickListener(l);
        root.findViewById(R.id.service_text_view).setOnClickListener(l);
        root.findViewById(R.id.classLoader_text_view).setOnClickListener(l);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mTextView != null) {
            mTextView = null;
        }
        if (root != null) {
            root = null;
        }
    }

    View.OnClickListener l = v -> {
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
            case R.id.service_text_view:
                ServiceDemo();
                break;
            case R.id.classLoader_text_view:
                ClassLoaderDemo();
                break;
            default:
                break;
        }
    };

    private void ClassLoaderDemo() {
        // 获取当前线程的类加载器
        ClassLoader classLoader = InterviewFragment.class.getClassLoader();
        Log.d(TAG, "Current class loader: " + classLoader);

        // 打印类加载器的层次结构
        while (classLoader != null) {
            Log.d(TAG, "ClassLoader's parent : " + classLoader);
            classLoader = classLoader.getParent();
        }
    }

    private void ServiceDemo() {
        if (mContext != null) {
            Intent startIntent = new Intent(mContext, MyService.class);
            mContext.startService(startIntent); // 启动服务

            // 启动IntentService
            Intent intentServiceIntent = new Intent(mContext, MyIntentService.class);
            mContext.startService(intentServiceIntent);
        }
    }

    private void showInvocationHandlerDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
        mBuilder.setTitle("Interview");
        mBuilder.setPositiveButton("确定", (dialog, which) -> {
            ProxyDialog proxyDialog = new ProxyDialog();
            Log.d(TAG, "proxy动态代理前———————— ");
            proxyDialog.handleDialog();
            proxyDialog.handleDialogTitle("你大爷！");
            Log.d(TAG, "proxy动态代理后———————— ");
            DialogProxyHandler dialogProxyHandler = new DialogProxyHandler(proxyDialog);
            DialogApi dialogApi = (DialogApi) Proxy.newProxyInstance(
                    proxyDialog.getClass().getClassLoader(),
                    proxyDialog.getClass().getInterfaces(),
                    dialogProxyHandler);
            dialogApi.handleDialog();
            dialogApi.handleDialogTitle("你大爷！");
            dialog.dismiss();
        }).setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        final AlertDialog alertDialog = mBuilder.create();
        alertDialog.show();
    }

    /**
     * 获取 ANR 文件命令：adb bugreport anr_filename.zip
     * path：anr_filename/FS/data/anr
     * 模仿阻塞，可产生 input dispatcher 的 ANR
     */
    private void CountdownLatchDemo() {
        CountDownLatch mCountDownLatch = new CountDownLatch(3);
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
            Log.d(TAG, "countDownLatchException = " + e.getMessage());
        }
    }

    /**
     * 验证死锁
     */
    private void ThreadPoolExecutorCheckAddWorker() {
        final boolean[] isAddWorker = {false};
        while (!isAddWorker[0]) {
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    isAddWorker[0] = true;
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
            Log.d(TAG, "ReentrantLock#InterruptedException = " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    // 生产者消费者模式
    private static class ProductConsumerMode {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

        public ProductConsumerMode() {
            // 生产线程
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    //
                    make(0);
                }
            });

            // 消费线程
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    //
                    make(1);
                }
            });
        }

        private void make(int mode) {
            try {
                while (true) {
                    if (mode == 0) {
                        // 生产模式

                        queue.put(new Random(100).nextInt());
                        Thread.sleep(100); // 模拟耗时操作

                    } else {
                        // 消费模式
                        queue.take();
                        Thread.sleep(100); // 模拟耗时操作
                    }
                }
            } catch (InterruptedException|IllegalArgumentException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
