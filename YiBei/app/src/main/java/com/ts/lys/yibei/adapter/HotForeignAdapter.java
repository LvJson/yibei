package com.ts.lys.yibei.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.utils.BaseUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by jcdev1 on 2018/5/9.
 */

public class HotForeignAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private OnItemClickListenerr listenerr;
    private List<?> activityBeanList;


    public HotForeignAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<?> activityBeanList) {
        this.activityBeanList = null;
        this.activityBeanList = activityBeanList;
        notifyDataSetChanged();

    }

    public void setOnItemClickListenerr(OnItemClickListenerr listenerr) {
        this.listenerr = listenerr;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new HotForeignViewholder(mInflater.inflate(R.layout.adapter_hot_foreign_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HotForeignViewholder) {
        }

    }

    @Override
    public int getItemCount() {

        return 3;
    }

    public class HotForeignViewholder extends RecyclerView.ViewHolder {

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
