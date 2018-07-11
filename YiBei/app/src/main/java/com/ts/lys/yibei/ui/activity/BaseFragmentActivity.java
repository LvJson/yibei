package com.ts.lys.yibei.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.customeview.CustomProgress;
import com.ts.lys.yibei.mvpview.BaseMvpView;
import com.ts.lys.yibei.utils.CloseAllActivity;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.ts.lys.yibei.utils.SpUtils;

import butterknife.ButterKnife;

/**
 * Created by jcdev1 on 2017/12/22.
 */

public class BaseFragmentActivity extends FragmentActivity implements BaseMvpView {

    public String className = "";
    public CustomProgress customProgress;
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
        accessToken = SpUtils.getString(this, "accessToken", "");
        userId = SpUtils.getString(this, "userId");
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
    }

    @Override
    protected void onPause() {
        super.onPause();
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
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
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
     * 展示自定义dialog
     */
    protected void showCustomProgress() {
        customProgress = CustomProgress.show(this);
        if (customProgress != null)
            customProgress.show();
    }

    /**
     * 展示自定义dialog
     */
    @Override
    public void showCustomProgress(boolean cancelable) {
        if (customProgress == null)
            customProgress = CustomProgress.show(this, cancelable);
        else
            customProgress.show();
    }

    @Override
    public void disCustomProgress() {
        if (customProgress != null)
            customProgress.dismiss();
    }

}
