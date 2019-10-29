package com.edwin;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.BaseEvent;
import com.common.Constants;
import com.common.HomeReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.meituan.MainActivity;
import com.meituan.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Edwin,CHEN on 2019/10/14.
 */

public class HomeFragment extends Fragment {

    public static HomeFragment createHomeFragment() {
        return new HomeFragment();
    }

    private View root;

    private TextView eventBusFlagText;

    private ReactRootView mReactRootView;

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mContext == null) {
            mContext = context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.home_fragment, null);
        root.findViewById(R.id.open_meituan_text_view).setOnClickListener(l);
        root.findViewById(R.id.event_bus_sender_text_view).setOnClickListener(l);
        eventBusFlagText = root.findViewById(R.id.event_bus_flag_text_view);
        root.findViewById(R.id.event_bus_sender_text_view).setOnClickListener(l);
        initReactRootView(root);
        return root;
    }

    private void initReactRootView(View view) {
        mReactRootView = new ReactRootView(mContext);

        // The string here (e.g. "MyReactNativeApp") has to match
        // the string in AppRegistry.registerComponent() in index.js
        mReactRootView.startReactApplication(HomeReactInstanceManager.newInstance().getReactInstanceManager(), "MeiTuan", null);

        LinearLayout homeReactRootView = view.findViewById(R.id.home_react_view);
        if (homeReactRootView.indexOfChild(mReactRootView) == -1) {
            (homeReactRootView).addView(mReactRootView,0);
        }
    }

    private View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.open_meituan_text_view:
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    break;
                case R.id.event_bus_sender_text_view:
                    EventBus.getDefault().post(new HomeEvent());
                    EventBus.getDefault().post(new HomeEvent(Constants.HOME_FRAGMENT_SENDER_REFRESH));
                    EventBus.getDefault().post(new HomeEvent(Constants.HOME_FRAGMENT_SENDER_COMPLEX, new HomeEvent()));
                    break;
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveRefreshEvent(BaseEvent baseEvent) {
        switch (baseEvent.getEventType()) {
            case Constants.HOME_FRAGMENT_SENDER_REFRESH:
                Toast.makeText(getActivity(), "刷新就刷新", Toast.LENGTH_LONG).show();
                break;
            case Constants.HOME_FRAGMENT_SENDER_COMPLEX:
                eventBusFlagText.setText("复杂点儿");
                Toast.makeText(getActivity(), "复杂点儿", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getActivity(), "我就想验证下EventBus", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if (mReactRootView != null) {
            mReactRootView.unmountReactApplication();
        }
    }

}
