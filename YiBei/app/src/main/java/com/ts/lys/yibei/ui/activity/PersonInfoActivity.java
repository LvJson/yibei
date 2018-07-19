package com.ts.lys.yibei.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.jph.takephoto.app.TakePhotoActivity;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.customeview.PhotoDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jcdev1 on 2018/7/19.
 */

public class PersonInfoActivity extends TakePhotoActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_head)
    ImageView ivHead;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.tv_phone_num)
    TextView tvPhoneNum;

    private PhotoDialog photoDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);

        initView();
        initListener();
    }


    private void initView() {

        tvTitle.setText(getString(R.string.edit_person_info));
        setStatusBarStatus();

        photoDialog = new PhotoDialog(this,
                getResources().getIdentifier("BottomDialog", "style", getPackageName()));
        photoDialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.LEFT | Gravity.RIGHT);
    }

    private void initListener() {
        photoDialog.setOnItemClickListener(new PhotoDialog.ItemClickListenr() {
            @Override
            public void onPhotoClick() {
                //TODO 拍照
                photoDialog.dismiss();
            }

            @Override
            public void onAlbumClick() {
                //TODO 选取图片
                photoDialog.dismiss();

            }

            @Override
            public void onCancleClick() {
                photoDialog.dismiss();

            }
        });

    }

    @OnClick({R.id.ll_back, R.id.ll_choose_head, R.id.ll_nickname, R.id.btn_sign_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_choose_head:
                photoDialog.show();
                break;
            case R.id.ll_nickname:
                break;
            case R.id.btn_sign_out:
                break;
        }
    }


    private void setStatusBarStatus() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        /**
         * SDK版本>=23，可设置状态栏字体颜色
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }
}
