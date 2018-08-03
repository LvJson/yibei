package com.ts.lys.yibei.customeview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ts.lys.yibei.R;


/**
 * Created by yangcaihao on 2017/7/6.
 */

public class CustomDialog2 extends Dialog implements View.OnClickListener {
    private Context context;
    private TextView takePhotoBtn;
    private TextView takePictureBtn;
    private TextView cancelBtn;
    private Runnable takePhotoRunnable;
    private Runnable takePictureRunnable;
    private Runnable takeCancle;


    public CustomDialog2(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public CustomDialog2(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public CustomDialog2(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_select_pic2);
        initView();
    }

    private void initView() {
        WindowManager manager = ((Activity) context).getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = width;
        lp.alpha = 1;
        lp.dimAmount = 0.4f;// window设置背景透明度
        window.setAttributes(lp);
        takePhotoBtn = (TextView) findViewById(R.id.btn_take_photo);
        takePhotoBtn.setOnClickListener(this);
        takePictureBtn = (TextView) findViewById(R.id.btn_take_picture);
        takePictureBtn.setOnClickListener(this);
        cancelBtn = (TextView) findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(this);
    }

    public void setTakePhotoRunnable(Runnable runnable) {
        takePhotoRunnable = runnable;
    }

    public void setTakePictureRunnable(Runnable runnable) {
        takePictureRunnable = runnable;
    }

    public void setTakeCancle(Runnable runnable) {
        takeCancle = runnable;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
                if (takePhotoRunnable != null) {
                    takePhotoRunnable.run();
                }
                this.dismiss();
                break;
            case R.id.btn_take_picture:
                if (takePictureRunnable != null) {
                    takePictureRunnable.run();
                }
                this.dismiss();
                break;
            case R.id.btn_cancel:
                if (takeCancle != null) {
                    takeCancle.run();
                }
                this.dismiss();
                break;
        }
    }
}
