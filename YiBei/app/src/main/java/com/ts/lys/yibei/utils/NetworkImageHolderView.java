package com.ts.lys.yibei.utils;

import android.content.Context;
import android.view.View;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.customeview.SmartImageView;

/**
 * Created by jcdev1 on 2018/6/14.
 */

public class NetworkImageHolderView extends Holder<String> {
    private SmartImageView imageView;
    private Context mContext;
    private float scale = 0.0f;

    public NetworkImageHolderView(View itemView, Context mContext, float scale) {
        super(itemView);
        this.mContext = mContext;
        this.scale = scale;
    }

    @Override
    protected void initView(View itemView) {
        imageView = itemView.findViewById(R.id.smart_view);

    }

    @Override
    public void updateUI(String data) {
        Glide.with(mContext)
                .load(data)
                .placeholder(R.mipmap.home_no)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);

    }
}
