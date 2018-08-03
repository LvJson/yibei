package com.ts.lys.yibei.ui.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.just.agentweb.AgentWeb;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.EventBean;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.constant.EventContents;
import com.ts.lys.yibei.customeview.CustomDialog2;
import com.ts.lys.yibei.utils.AndroidInterface;
import com.ts.lys.yibei.utils.Logger;
import com.ts.lys.yibei.utils.SpUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PictureWebViewActivity extends TakePhotoActivity {

    @Bind(R.id.linear_layout)
    AutoLinearLayout mLinearLayout;
    @Bind(R.id.tv_title)
    TextView tvTitle;


    private AgentWeb mAgentWeb;

    //    private ProgressDialog longDialog;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    private CustomDialog2 menuDialog;
    private Uri imageUri;
    private TakePhoto takePhoto;
    private String userId;
    private String accessToken;


    private WebChromeClient webChromeClient = new WebChromeClient() {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            tvTitle.setText(title);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            AlertDialog.Builder b = new AlertDialog.Builder(PictureWebViewActivity.this);
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
            AlertDialog.Builder b = new AlertDialog.Builder(PictureWebViewActivity.this);
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
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                // 网页加载完成
//                longDialog.dismiss();
            }

        }

        // For 3.0+ Devices (Start)
        // onActivityResult attached before constructor
        protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
//                getImageFromAlbum();
            showMenuDialog();
        }


        // For Lollipop 5.0+ Devices
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }

            uploadMessage = filePathCallback;
            showMenuDialog();
            return true;
        }

        //For Android 4.1 only
        protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;

            showMenuDialog();
//                getImageFromAlbum();
        }

        protected void openFileChooser(ValueCallback<Uri> uploadMsg) {

            mUploadMessage = uploadMsg;
            showMenuDialog();
//                getImageFromAlbum();
        }
    };
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_web);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initTakePhoto() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        imageUri = Uri.fromFile(file);
        takePhoto = getTakePhoto();
        int maxSize = 1024000;
        int width = 800;
        int height = 800;
        boolean showProgressBar = true;
        boolean enableRawFile = true;
        CompressConfig config = new CompressConfig.Builder()
                .setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)
                .enableReserveRaw(enableRawFile)
                .create();
        takePhoto.onEnableCompress(config, showProgressBar);
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(true);
        builder.setCorrectImage(true);
        takePhoto.setTakePhotoOptions(builder.create());
    }


    private void initView() {
        accessToken = SpUtils.getString(this, BaseContents.ACCESS_TOKEN, "");
        userId = SpUtils.getString(this, BaseContents.USERID, "");
        setStatusBarStatus();
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
//        initDialogs();
//        longDialog.show();

        mAgentWeb = AgentWeb.with(this)//传入Activity
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
                .useDefaultIndicator()// 使用默认进度条
                .setWebChromeClient(webChromeClient)
                .setWebViewClient(webViewClient)
                .createAgentWeb()//
                .ready()
                .go(addUserIdAndTokenToUrl(url));
        mAgentWeb.getJsInterfaceHolder().addJavaObject("android", new AndroidInterface(PictureWebViewActivity.this));


    }

    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        if (mAgentWeb.getWebCreator().getWebView().canGoBack()) {
            mAgentWeb.back();
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 显示dialog
     */
    private void showMenuDialog() {
        if (menuDialog == null) {
            initMenuDialog();
        }
        menuDialog.show();
    }

    /**
     * 初始化dialog
     */
    private void initMenuDialog() {
        menuDialog = new CustomDialog2(this, R.style.customDialogStyle);
        menuDialog.setCanceledOnTouchOutside(true);
        //监听dialog 取消状态，并设置ValueCallback 为null以及onReceiveValue传递null,否则点击不会有回调
        menuDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(null);
                    mUploadMessage = null;
                }
            }
        });
        menuDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });
        menuDialog.setTakePhotoRunnable(new Runnable() {
            @Override
            public void run() {
                initTakePhoto();
                takePhoto.onPickFromCapture(imageUri);//拍照
            }
        });
        menuDialog.setTakePictureRunnable(new Runnable() {
            @Override
            public void run() {
                initTakePhoto();
                takePhoto.onPickFromGallery();//选择照片
            }
        });
        menuDialog.setTakeCancle(new Runnable() {
            @Override
            public void run() {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(null);
                    mUploadMessage = null;
                }
            }
        });
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
        if (uploadMessage != null) {
            uploadMessage.onReceiveValue(null);
            uploadMessage = null;
        }
        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(null);
            mUploadMessage = null;
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        if (uploadMessage != null) {
            uploadMessage.onReceiveValue(null);
            uploadMessage = null;
        }
        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(null);
            mUploadMessage = null;
        }
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        String str = result.getImages().get(0).getCompressPath();
        File file = new File(str);
        Uri uri = Uri.fromFile(file);
        Logger.e("uri", uri.getPath());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (uploadMessage == null)
                return;
            uploadMessage.onReceiveValue(new Uri[]{uri});
            uploadMessage = null;
        } else {
            if (mUploadMessage == null)
                return;
            mUploadMessage.onReceiveValue(uri);
            mUploadMessage = null;
        }
    }

    protected void setStatusBarStatus() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        /**
         * SDK版本>=23，可设置状态栏字体颜色
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    /**
     * 初始化用于加载的dialog
     */
//    private void initDialogs() {
//        longDialog = new ProgressDialog(this);
//        longDialog.setMessage("连接中,请耐心等待");
//        longDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        longDialog.setCancelable(false);
//        longDialog.setIndeterminate(false);
//    }
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
        mAgentWeb.clearWebCache();
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
        EventBus.getDefault().unregister(this);

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

    /**
     * 给webview里url添加UserId和Token字段
     *
     * @param url 原url
     * @return 修改之后的url
     */
    private String addUserIdAndTokenToUrl(String url) {
        if (!TextUtils.isEmpty(userId)) {
            if (url.indexOf("?") != -1) {
                return url + "&userId=" + userId + "&accessToken=" + accessToken;
            } else {
                return url + "?userId=" + userId + "&accessToken=" + accessToken;
            }
        } else
            return url;

    }
}