package com.ts.lys.yibei.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.IndexBean;
import com.ts.lys.yibei.bean.RealTimeBean;
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

public class HotForeignAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private LayoutInflater mInflater;
    private Context mContext;
    private OnItemClickListenerr listenerr;
    private List<IndexBean.DataBean.HotsBean> activityBeanList;


    public HotForeignAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<IndexBean.DataBean.HotsBean> activityBeanList) {
        this.activityBeanList = null;
        this.activityBeanList = activityBeanList;
        notifyDataSetChanged();

    }

    public synchronized void setUpdateModel(RealTimeBean model) {
        if (model != null && activityBeanList != null) {

            for (int i = 0; i < activityBeanList.size(); i++) {
                if (activityBeanList.get(i).getSymbol().equals(model.getSymbol())) {
                    IndexBean.DataBean.HotsBean hotsBean = activityBeanList.get(i);
                    if (model.getBid() < hotsBean.getPrice())
                        hotsBean.setStatus(-1);
                    else if (model.getBid() == hotsBean.getPrice())
                        hotsBean.setStatus(0);
                    else
                        hotsBean.setStatus(1);

                    hotsBean.setPrice(model.getBid());
                    double percent = (model.getBid() - hotsBean.getYesterdayPrice()) / hotsBean.getYesterdayPrice() * 100;
                    hotsBean.setGains(percent);

                    if (hotsBean.getStatus() != 0) {
                        notifyDataSetChanged();
                    }
                    break;
                }
            }

        }

    }

    public void setOnItemClickListenerr(OnItemClickListenerr listenerr) {
        this.listenerr = listenerr;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new HotForeignViewholder(mInflater.inflate(R.layout.adapter_index_hot_foreign_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HotForeignViewholder) {

            IndexBean.DataBean.HotsBean hb = activityBeanList.get(position);
            ((HotForeignViewholder) holder).tvNameOne.setText(hb.getSymbolCn());

            if (hb.getStatus() < 0)
                ((HotForeignViewholder) holder).tvPriceOne.setTextColor(mContext.getResources().getColor(R.color.fall_color));
            else
                ((HotForeignViewholder) holder).tvPriceOne.setTextColor(mContext.getResources().getColor(R.color.rise_color));

            ((HotForeignViewholder) holder).tvPriceOne.setText(BaseUtils.getDigitsData(hb.getPrice(), hb.getDigits()));
            if (hb.getGains() < 0)
                ((HotForeignViewholder) holder).tvSpreadOne.setTextColor(mContext.getResources().getColor(R.color.fall_color));
            else
                ((HotForeignViewholder) holder).tvSpreadOne.setTextColor(mContext.getResources().getColor(R.color.rise_color));

            ((HotForeignViewholder) holder).tvSpreadOne.setText(BaseUtils.getDigitsData(hb.getGains(), 2) + "%");


        }

    }

    @Override
    public int getItemCount() {

        return activityBeanList == null ? 0 : activityBeanList.size();
    }

    public class HotForeignViewholder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_name_one)
        TextView tvNameOne;
        @Bind(R.id.tv_price_one)
        TextView tvPriceOne;
        @Bind(R.id.tv_spread_one)
        TextView tvSpreadOne;
        @Bind(R.id.ll_father)
        LinearLayout llFather;

        HotForeignViewholder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            AutoUtils.autoSize(view);
            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) llFather.getLayoutParams();
            int width = (BaseUtils.getScreenWidth(mContext) - BaseUtils.dip2px(mContext, 20)) / 3;
            layoutParams.width = width;
            llFather.setLayoutParams(layoutParams);
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
