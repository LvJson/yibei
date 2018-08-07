package com.ts.lys.yibei.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.customeview.CustomProgress;
import com.ts.lys.yibei.mvpview.BaseMvpView;
import com.ts.lys.yibei.utils.CloseAllActivity;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.ts.lys.yibei.utils.SpUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * Created by jcdev1 on 2017/12/22.
 */

public class BaseActivity extends Activity implements BaseMvpView {

    private CustomProgress customProgress;
    public String className = "";
    private Toast toast;
    public String TAG = "";
    public String accessToken = "";
    public String userId = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBaseData();
    }

    private void initBaseData() {
        TAG = getClass().getSimpleName();
        accessToken = SpUtils.getString(this, BaseContents.ACCESS_TOKEN, "");
        userId = SpUtils.getString(this, BaseContents.USERID, "");
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        onViewCreated();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
        onViewCreated();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
        onViewCreated();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomHttpUtils.cancelHttp(className);
        CloseAllActivity.getScreenManager().popActivity(this);
    }

    protected void onViewCreated() {
        className = getClass().getName();
        CloseAllActivity.getScreenManager().pushActivity(this);

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

    protected boolean filterException(Exception e) {
        if (e != null) {
            e.printStackTrace();
            showToast(e.getMessage());
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void showToast(String content) {
        if (content.equals(BaseContents.NET_ERROR))
            content = getString(R.string.net_error);

        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(this, content, Toast.LENGTH_SHORT);
        toast.show();
    }

    protected void showToast(int resId) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 返回按钮
     */
    protected void setBackButton() {
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 设置标题
     *
     * @param title
     */
    protected void setTitle(String title) {
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(title);
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    protected void startActivity(Class<?> cls, String... objs) {
        Intent intent = new Intent(this, cls);
        for (int i = 0; i < objs.length; i++) {
            intent.putExtra(objs[i], objs[++i]);
        }
        startActivity(intent);
    }


    /**
     * 是否继续显示加载中的dialog；
     */
    private boolean isGoOnShow = true;

    /**
     * 展示自定义dialog
     */
    public void showCustomProgress() {
        isGoOnShow = true;
        //一秒倒计时
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isGoOnShow) {
                    if (customProgress == null)
                        customProgress = CustomProgress.show(BaseActivity.this);
                    else
                        customProgress.show();
                }

            }
        }, 1000);

    }

    /**
     * 展示自定义dialog
     */
    @Override
    public void showCustomProgress(final boolean cancelable) {
        isGoOnShow = true;
        //一秒倒计时
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isGoOnShow) {
                    if (customProgress == null)
                        customProgress = CustomProgress.show(BaseActivity.this, cancelable);
                    else
                        customProgress.show();
                }

            }
        }, 1000);

    }

    @Override
    public void disCustomProgress() {
        isGoOnShow = false;
        if (customProgress != null)
            customProgress.dismiss();
    }

}
