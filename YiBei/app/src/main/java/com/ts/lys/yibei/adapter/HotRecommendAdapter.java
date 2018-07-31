package com.ts.lys.yibei.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.IndexBean;
import com.ts.lys.yibei.utils.BaseUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jcdev1 on 2018/5/9.
 */

public class HotRecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private OnItemClickListenerr listenerr;
    private List<IndexBean.DataBean.NewsBean> activityBeanList;


    public HotRecommendAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<IndexBean.DataBean.NewsBean> activityBeanList) {
        this.activityBeanList = null;
        this.activityBeanList = activityBeanList;
        notifyDataSetChanged();

    }

    public void setOnItemClickListenerr(OnItemClickListenerr listenerr) {
        this.listenerr = listenerr;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new HotForeignViewholder(mInflater.inflate(R.layout.adapter_hot_recommend_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HotForeignViewholder) {

            IndexBean.DataBean.NewsBean newsBean = activityBeanList.get(position);
            ((HotForeignViewholder) holder).tvContent.setText(newsBean.getTitle());
            ((HotForeignViewholder) holder).tvLabel.setText(newsBean.getLabel());
            ((HotForeignViewholder) holder).tvTime.setText(newsBean.getTime());
            Glide.with(mContext).load(newsBean.getImage()).placeholder(R.mipmap.a_test_bg).error(R.mipmap.a_test_bg).into(((HotForeignViewholder) holder).ivPic);
        }

    }

    @Override
    public int getItemCount() {

        return activityBeanList == null ? 0 : activityBeanList.size();
    }

    public class HotForeignViewholder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.tv_label)
        TextView tvLabel;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.iv_pic)
        ImageView ivPic;

        HotForeignViewholder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            AutoUtils.autoSize(view);
        }
    }

    public interface OnItemClickListenerr {

    }

    public static String millionToDate(Long million) {
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(million);
        return formatter.format(calendar.getTime());
    }

    /**
     * 根据图片动态设置容器高度
     */
    private void setPicHight(View view) {
        Resources resources = mContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels - BaseUtils.dip2px(mContext, 30);
        float height = width / 2.21f;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = (int) height;
        view.setLayoutParams(layoutParams);

    }
}
