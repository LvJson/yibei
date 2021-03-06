package com.ts.lys.yibei.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.EventBean;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.constant.EventContents;
import com.ts.lys.yibei.customeview.CustomPopWindow;
import com.ts.lys.yibei.customeview.PhotoDialog;
import com.ts.lys.yibei.utils.ButtonUtils;
import com.ts.lys.yibei.utils.Logger;
import com.ts.lys.yibei.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.DecimalFormat;

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
    @Bind(R.id.ll_father)
    LinearLayout llFather;

    private PhotoDialog photoDialog;
    private Uri imageUri;
    private TakePhoto takePhoto;
    private CropOptions cropOptions;
    private Toast toast;


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

        String phone = getIntent().getStringExtra("phone");
        if (!TextUtils.isEmpty(phone)) {
            int index = phone.indexOf(")");
            if (index == -1)
                tvPhoneNum.setText(phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length()));
            else
                tvPhoneNum.setText(phone.substring(index + 1, index + 4) + "****" + phone.substring(phone.length() - 4, phone.length()));
        }
    }

    /**
     * 初始化拍照工具
     */
    private void initTakePhoto() {
        File file = new File(Environment.getExternalStorageDirectory(), "/yibei/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        imageUri = Uri.fromFile(file);
        takePhoto = getTakePhoto();
        int maxSize = 10240;
        int width = 100;
        int height = 100;
        boolean showProgressBar = true;
        boolean enableRawFile = true;
        //压缩
        CompressConfig config = new CompressConfig.Builder()
                .setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)
                .enableReserveRaw(enableRawFile)
                .create();
        takePhoto.onEnableCompress(config, showProgressBar);
        //裁剪
        cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();

        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(true);
        builder.setCorrectImage(true);
        takePhoto.setTakePhotoOptions(builder.create());

    }

    private void initListener() {
        photoDialog.setOnItemClickListener(new PhotoDialog.ItemClickListenr() {
            @Override
            public void onPhotoClick() {
                //TODO 拍照
                photoDialog.dismiss();
                initTakePhoto();
                takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
//                takePhoto.onPickFromCapture(imageUri);
            }

            @Override
            public void onAlbumClick() {
                //TODO 选取图片
                photoDialog.dismiss();
                initTakePhoto();
                takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
//                takePhoto.onPickFromGallery();
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
//                photoDialog.show();
                break;
            case R.id.ll_nickname:
                break;
            case R.id.btn_sign_out:
                if (ButtonUtils.isFastDoubleClick(R.id.btn_sign_out, 1500)) return;
                showQuitPop();
                break;
        }
    }

    private void showQuitPop() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_base_remind_layout, null);
        TextView tvTitle = contentView.findViewById(R.id.tv_title);
        TextView tvCancel = contentView.findViewById(R.id.tv_cancle);
        TextView tvConfirm = contentView.findViewById(R.id.tv_confirm);
        tvTitle.setText(getString(R.string.drop_out_app));
        final CustomPopWindow popupWindowBuilder = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(0.7f)
                .create()
                .showAtLocation(llFather, Gravity.CENTER, 0, 0);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindowBuilder.dissmiss();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpUtils.cleanSP(PersonInfoActivity.this);
                startActivity(new Intent(PersonInfoActivity.this, AccountLoginActivity.class));
                EventBus.getDefault().post(new EventBean(EventContents.ALL_MAIN_REFRESH, null));
                EventBus.getDefault().post(new EventBean(EventContents.DROP_OUT_SUCCESS, null));
                finish();
                popupWindowBuilder.dissmiss();

            }
        });
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
        showToast(getString(R.string.cancel_do));
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        showToast(msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        String str = result.getImages().get(0).getCompressPath();
        File file = new File(str);
        long fileS = file.length();
        DecimalFormat df = new DecimalFormat("#.00");
        if (fileS < 1048576) {
            String size = df.format((double) fileS / 1024) + "KB";
            Logger.e("size", size);
        } else {
            String size = df.format((double) fileS) + "BT";
            Logger.e("size", size);

        }

        Glide.with(this).load(str).into(ivHead);
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
}
