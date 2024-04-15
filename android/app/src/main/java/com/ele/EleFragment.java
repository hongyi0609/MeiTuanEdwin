package com.ele;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.base.BaseFragment;
import com.meituan.R;
import com.meituan.ReactMainActivity;

import org.jetbrains.annotations.Nullable;

/**
 * Date:
 * Author:
 * Description:
 */
public class EleFragment extends BaseFragment {

    private final String TAG = com.ele.EleFragment.class.getSimpleName();

    public static com.ele.EleFragment createFragment() {
        return new com.ele.EleFragment();
    }

    private View root;

    private WebView webView;

    private boolean needDownload = true;

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mContext == null) {
            mContext = getContext();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.ele_fragment, null);

        root.findViewById(R.id.open_ele_text_view).setOnClickListener(l);

        root.findViewById(R.id.open_meituan_text_view).setOnClickListener(l);

        initWebView();
        return root;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        webView = root.findViewById(R.id.web_view);
        webView.findViewById(R.id.button).setOnClickListener(l);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if ("eleme".equals(request.getUrl().getScheme())) {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(request.getUrl()))));
                        try {
                            Intent intent = new Intent();
//                            Uri mUri = Uri.parse("eleme://windvane2?url=https%3A%2F%2Ftb.ele.me%2Fwow%2Falsc%2Fmod%2F5ff478c565f7c94acf46acd4");
                            Uri mUri = Uri.parse(String.valueOf(Uri.parse(String.valueOf((request.getUrl())))));
                            intent.setData(mUri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            needDownload = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Log.d(TAG, "DownloadListener -> url = " + url + ",isDownload = " + needDownload);
                if (needDownload) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse(url);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(uri);
                    mContext.startActivity(intent);
                }
                needDownload = true;//重置为初始状态
            }
        });
        WebSettings webSettings = webView.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        String url = "https:www.baidu.com";
        // 先载入JS代码
        // 格式规定为:file:///android_asset/文件名.html
        url = "file:///android_asset/javascript.html";
        url = "https://market.wapa.taobao.com/app/starlink/dev-tool-demo/pages/starlink?env=er&mtop=m&star_id=3446&forceErMode=er";
        webView.loadUrl(url);

        // 由于设置了弹窗检验调用结果,所以需要支持js对话框
        // webview只是载体，内容的渲染需要使用webviewChromClient类去实现
        // 通过设置WebChromeClient对象处理JavaScript的对话框
        //设置响应js 的Alert()函数
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }

        });
    }

    private View.OnClickListener l = (v) -> {
        switch (v.getId()) {
            case R.id.open_ele_text_view:
                try {
                    String backUrl = "eleme://search?search_hint=%7B%22content%22%3A%22%E5%A4%A7%E6%B6%A6%E5%8F%91%22%2C%22factors4Box%22%3A%22%7B%5C%22storeId%5C%22%3A239806190%2C%5C%22shopId%5C%22%3A150005304%2C%5C%22clickFrom%5C%22%3A%5C%22BOX%5C%22%7D%22%2C%22factors4Button%22%3A%22%7B%5C%22storeId%5C%22%3A239806190%2C%5C%22shopId%5C%22%3A150005304%2C%5C%22clickFrom%5C%22%3A%5C%22BUTTON%5C%22%7D%22%2C%22guideTrack%22%3A%22%7B%5C%22search_ab_bucket%5C%22%3A%5C%22a%5C%22%2C%5C%22search_app_id%5C%22%3A19907%2C%5C%22search_trace_id%5C%22%3A%5C%220b58a43716399882150451829e4907%5C%22%2C%5C%22search_rank_id%5C%22%3A%5C%220bb7325f923a45288984ff5f87899ed1%5C%22%2C%5C%22trackEntities%5C%22%3A%5C%22%255B%257B%2522shopId%2522%253A150005304%252C%2522storeId%2522%253A239806190%257D%255D%5C%22%7D%22%2C%22hint%22%3A%22%E5%A4%A7%E6%B6%A6%E5%8F%91%20%E7%BA%A2%E5%8C%8589%E5%87%8F10%22%2C%22index%22%3A%221%22%2C%22rankId%22%3A%220bb7325f923a45288984ff5f87899ed1%22%7D&factors=%7B%22storeId%22%3A239806190%2C%22shopId%22%3A150005304%2C%22clickFrom%22%3A%22BOX%22%7D&factors=factors4Button";
                    Uri uri = Uri.parse(backUrl);
                    Intent backUrlIntent = new Intent();
                    backUrlIntent.setAction(Intent.ACTION_VIEW);
                    backUrlIntent.setData(uri);
                    backUrlIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    backUrlIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(backUrlIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.open_meituan_text_view:
                startActivity(new Intent(getActivity(), ReactMainActivity.class));
                break;
            case R.id.button:
                Toast.makeText(mContext.getApplicationContext(), "系好安全带!", Toast.LENGTH_SHORT).show();
                // 通过Handler发送消息
                webView.post(new Runnable() {
                    @Override
                    public void run() {

                        // 注意调用的JS方法名要对应上
                        // 调用javascript的callJS()方法
                        webView.loadUrl("javascript:callJS()");
                    }
                });
                break;
        }
    };

    @Override
    public void onFirstFetchData() {
        super.onFirstFetchData();
        Log.d(TAG, "****onFirstFetchData()***");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (webView != null) {
            webView.destroy();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}