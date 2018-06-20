package com.ts.lys.yibei.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.customeview.CustomProgress;
import com.ts.lys.yibei.utils.CloseAllActivity;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.ts.lys.yibei.utils.SpUtils;

import butterknife.ButterKnife;

/**
 * Created by jcdev1 on 2017/12/22.
 */

public class BaseActivity extends Activity {

    private CustomProgress customProgress;
    public String className = "";
    public String accToken = "";
    public String telePhone = "";
    private Toast toast;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        accToken = SpUtils.getString(this, "accessToken", "");
        telePhone = SpUtils.getString(this, "phoneNum", "");
        /**
         * SDK版本>=23，可设置状态栏字体颜色
         */
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
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

    protected void showToast(String content) {

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
     * 展示自定义dialog
     */
    protected void showCustomProgress() {
        if (customProgress == null)
            customProgress = CustomProgress.show(this);
        else
            customProgress.show();
    }

    /**
     * 展示自定义dialog
     */
    protected void showCustomProgress(boolean cancelable) {
        if (customProgress == null)
            customProgress = CustomProgress.show(this, cancelable);
        else
            customProgress.show();
    }

    protected void disCustomProgress() {
        if (customProgress != null)
            customProgress.dismiss();
    }
}
