package com.ts.lys.yibei.customeview;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ts.lys.yibei.R;


/**
 * 选择上传头像弹窗
 */
public class PhotoDialog extends Dialog {

    public View view;
    private Context c;
    private TextView photo_textview;
    private TextView album_textview;
    private TextView cancel_textview;

    private ItemClickListenr listenr;

    public PhotoDialog(Context paramContext, int paramInt) {
        super(paramContext, paramInt);
        view = View.inflate(paramContext, R.layout.dialog_photo, null);
        this.setContentView(view);
        this.c = paramContext;
        photo_textview = findViewById(R.id.photo_textview);
        album_textview = findViewById(R.id.album_textview);
        cancel_textview = findViewById(R.id.cancel_textview);
        initListener();
    }

    private void initListener() {
        photo_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenr.onPhotoClick();
            }
        });

        album_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenr.onAlbumClick();
            }
        });

        cancel_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenr.onCancleClick();
            }
        });

    }

    /**
     * 显示弹出框
     */
    public void showDialog() {
        this.show();
    }

    public TextView getPhoto_textview() {
        return photo_textview;
    }

    public TextView getAlbum_textview() {
        return album_textview;
    }

    public TextView getCancel_texxtview() {
        return cancel_textview;
    }


    public void setOnItemClickListener(ItemClickListenr listenr) {

        this.listenr = listenr;
    }

    public interface ItemClickListenr {

        void onPhotoClick();

        void onAlbumClick();

        void onCancleClick();
    }
}
