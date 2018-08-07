package com.ts.lys.yibei.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.EventBean;
import com.ts.lys.yibei.constant.EventContents;
import com.ts.lys.yibei.utils.AndroidInterface;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jcdev1 on 2018/7/11.
 */

public class WebViewActivity extends BaseActivity {

    @Bind(R.id.ll_head)
    AutoLinearLayout llHead;

    private AgentWeb mAgentWeb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        setBackButton();
        setStatusBarStatus();
        String url = getIntent().getStringExtra("url");
        mAgentWeb = AgentWeb.with(this)//传入Activity
                .setAgentWebParent(llHead, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
                .useDefaultIndicator()// 使用默认进度条
                .setWebViewClient(webViewClient)
                .setWebChromeClient(webChromeClient)
                .createAgentWeb()
                .ready()
                .go(addUserIdAndTokenToUrl(url));
        mAgentWeb.getJsInterfaceHolder().addJavaObject("android", new AndroidInterface(WebViewActivity.this));

    }

    private WebViewClient webViewClient = new WebViewClient() {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    };

    private WebChromeClient webChromeClient = new WebChromeClient() {

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            AlertDialog.Builder b = new AlertDialog.Builder(WebViewActivity.this);
            b.setTitle("提示");
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

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            AlertDialog.Builder b = new AlertDialog.Builder(WebViewActivity.this);
            b.setTitle("提示");
            b.setMessage(message);
            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            });
            b.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            });
            b.create().show();
            return true;
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }
    };


    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        if (mAgentWeb.getWebCreator().getWebView().canGoBack()) {
            mAgentWeb.back();
        } else {
            finish();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean event) {
        String tagOne = event.getTagOne();
        if (tagOne.equals(EventContents.BIND_MT4)) {

            String accType = (String) event.getResponse();
            Intent intent = new Intent(this, BindAccountActivity.class);
            intent.putExtra("accType", accType);
            startActivity(intent);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        mAgentWeb.clearWebCache();
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();

    }


    /**
     * 给webview里url添加UserId和Token字段
     *
     * @param url 原url
     * @return 修改之后的url
     */
    private String addUserIdAndTokenToUrl(String url) {
        if (!TextUtils.isEmpty(userId)) {
            if (url.indexOf("?") != -1) {
                return url + "&userId=" + userId + "&accessToken=" + accessToken + "&clickSource=android";
            } else {
                return url + "?userId=" + userId + "&accessToken=" + accessToken + "&clickSource=android";
            }
        } else
            return url;

    }
}
