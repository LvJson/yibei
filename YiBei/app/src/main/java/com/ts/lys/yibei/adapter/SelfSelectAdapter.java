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

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.GetQuotesModel;
import com.ts.lys.yibei.bean.RealTimeBean;
import com.ts.lys.yibei.utils.AppUtils;
import com.ts.lys.yibei.utils.BaseUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jcdev1 on 2018/5/9.
 */

public class SelfSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private OnItemClickListenerr listenerr;
    private OnItemLongClickListener longClickListener;

    private List<GetQuotesModel.DataBean.SymbolsBean> mList;
    private List<RealTimeBean> realList;
    private String tag;


    public SelfSelectAdapter(Context mContext, String tag) {
        this.mContext = mContext;
        this.tag = tag;
        mInflater = LayoutInflater.from(mContext);

    }

    public void setData(List<GetQuotesModel.DataBean.SymbolsBean> list) {
        this.mList = list;
        realList = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            RealTimeBean bean = new RealTimeBean();
            bean.setSymbol(mList.get(i).getSymbolEn());
            bean.setAsk(mList.get(i).getAsk());
            bean.setBid(mList.get(i).getBid());
            bean.setMarket(mList.get(i).getMarket());
            realList.add(bean);
        }
        notifyDataSetChanged();

    }

    public void setOnItemClickListenerr(OnItemClickListenerr listenerr) {
        this.listenerr = listenerr;
    }

    public void setOnLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new HotForeignViewholder(mInflater.inflate(R.layout.adapter_self_select_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof HotForeignViewholder) {

            final GetQuotesModel.DataBean.SymbolsBean model = mList.get(position);
            model.setPosition(position);
            String en = model.getSymbolEn();
            if (en.endsWith("200")) {
                en = en.replace("200", "");
            }
            ((HotForeignViewholder) holder).tvSymbolEn.setText(en);
            ((HotForeignViewholder) holder).tvSymbolCn.setText(model.getSymbolCn());
            int digits = model.getDigits();
            if (model.getBid() == 0) {
                ((HotForeignViewholder) holder).tvPrice.setText("0.00");
            } else {
                if (digits != -1) {
                    ((HotForeignViewholder) holder).tvPrice.setText(AppUtils.getDigitsData(model.getBid(), digits));
                }
            }

            DecimalFormat df = new DecimalFormat("######0.00");


            if (model.getIsOpen().equals("0") && model.getDifference() == 0) {
                ((HotForeignViewholder) holder).tvPersent.setText(df.format(model.getGains()) + "%");

                if (model.getGains() > 0) {
                    ((HotForeignViewholder) holder).tvPersent.setTextColor(mContext.getResources().getColor(R.color.rise_color));

                } else if (model.getGains() < 0) {
                    ((HotForeignViewholder) holder).tvPersent.setTextColor(mContext.getResources().getColor(R.color.fall_color));
                }
            } else {
                //计算百分比
                double percent = (model.getBid() - model.getYesterdayClosePrice()) / model.getYesterdayClosePrice() * 100;
                ((HotForeignViewholder) holder).tvPersent.setText(df.format(percent) + "%");

                if (model.getBid() > model.getYesterdayClosePrice()) {
                    ((HotForeignViewholder) holder).tvPersent.setTextColor(mContext.getResources().getColor(R.color.rise_color));
                    ((HotForeignViewholder) holder).tvPersent.setText("+" + df.format(percent) + "%");
                } else if (model.getBid() == model.getYesterdayClosePrice()) {
                    ((HotForeignViewholder) holder).tvPersent.setTextColor(mContext.getResources().getColor(R.color.rise_color));
                } else {
                    ((HotForeignViewholder) holder).tvPersent.setTextColor(mContext.getResources().getColor(R.color.fall_color));
                    ((HotForeignViewholder) holder).tvPersent.setText(df.format(percent) + "%");
                }
                if (model.getState() == 1) {
                    ((HotForeignViewholder) holder).llPriceBg.setBackgroundResource(R.drawable.rise_bg);
                    ((HotForeignViewholder) holder).ivStatus.setImageResource(R.mipmap.white_arrow_rise);
                    ((HotForeignViewholder) holder).ivStatus.setVisibility(View.VISIBLE);
                } else if (model.getState() == -1) {
                    ((HotForeignViewholder) holder).llPriceBg.setBackgroundResource(R.drawable.fall_bg);
                    ((HotForeignViewholder) holder).ivStatus.setImageResource(R.mipmap.white_arrow_fall);
                    ((HotForeignViewholder) holder).ivStatus.setVisibility(View.VISIBLE);
                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listenerr.onSingleClick(model, position);
                }
            });

            if (tag.equals("自选")) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        longClickListener.onLongClick(model, position);
                        return false;
                    }
                });
            }
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {

            final GetQuotesModel.DataBean.SymbolsBean model = mList.get(position);
            model.setPosition(position);

            int digits = model.getDigits();
            if (model.getBid() == 0) {
                ((HotForeignViewholder) holder).tvPrice.setText("0.00");
            } else {
                if (digits != -1) {
                    ((HotForeignViewholder) holder).tvPrice.setText(AppUtils.getDigitsData(model.getBid(), digits));
                }
            }
            DecimalFormat df = new DecimalFormat("######0.00");


            if (model.getIsOpen().equals("0") && model.getDifference() == 0) {
                ((HotForeignViewholder) holder).tvPersent.setText(df.format(model.getGains()) + "%");

                if (model.getGains() > 0) {
                    ((HotForeignViewholder) holder).tvPersent.setTextColor(mContext.getResources().getColor(R.color.rise_color));

                } else if (model.getGains() < 0) {
                    ((HotForeignViewholder) holder).tvPersent.setTextColor(mContext.getResources().getColor(R.color.fall_color));
                }
            } else {
                //计算百分比
                double percent = (model.getBid() - model.getYesterdayClosePrice()) / model.getYesterdayClosePrice() * 100;
                ((HotForeignViewholder) holder).tvPersent.setText(df.format(percent) + "%");

                if (model.getBid() > model.getYesterdayClosePrice()) {
                    ((HotForeignViewholder) holder).tvPersent.setTextColor(mContext.getResources().getColor(R.color.rise_color));
                    ((HotForeignViewholder) holder).tvPersent.setText("+" + df.format(percent) + "%");
                } else if (model.getBid() == model.getYesterdayClosePrice()) {
                    ((HotForeignViewholder) holder).tvPersent.setTextColor(mContext.getResources().getColor(R.color.rise_color));
                } else {
                    ((HotForeignViewholder) holder).tvPersent.setTextColor(mContext.getResources().getColor(R.color.fall_color));
                    ((HotForeignViewholder) holder).tvPersent.setText(df.format(percent) + "%");
                }
                if (model.getState() == 1) {
                    ((HotForeignViewholder) holder).llPriceBg.setBackgroundResource(R.drawable.rise_bg);
                    ((HotForeignViewholder) holder).ivStatus.setImageResource(R.mipmap.white_arrow_rise);
                    ((HotForeignViewholder) holder).ivStatus.setVisibility(View.VISIBLE);
                } else if (model.getState() == -1) {
                    ((HotForeignViewholder) holder).llPriceBg.setBackgroundResource(R.drawable.fall_bg);
                    ((HotForeignViewholder) holder).ivStatus.setImageResource(R.mipmap.white_arrow_fall);
                    ((HotForeignViewholder) holder).ivStatus.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    /**
     * 取消收藏
     *
     * @param position
     */
    public void removeItem(int position) {
        notifyItemRemoved(position);
        mList.remove(position);
        realList.remove(position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    long updateTime = 0;

    public synchronized void setUpdateModel(RealTimeBean model) {
        if (realList == null || realList.size() == 0) return;
        for (int i = 0; i < realList.size(); i++) {
            if (realList.get(i).getSymbol().equals(model.getSymbol())) {
                realList.get(i).setBid(model.getBid());
                realList.get(i).setMarket(model.getMarket());
                realList.get(i).setAsk(model.getAsk());
                realList.get(i).setTime(model.getTime());
                break;
            }
        }

        /**
         * 实时刷新:问题->更新的条目会整个闪烁
         */
        for (int i = 0; i < mList.size(); i++) {
            GetQuotesModel.DataBean.SymbolsBean m = mList.get(i);
            RealTimeBean um = realList.get(i);
            m.setDifference(um.getBid() - m.getBid());
            m.setAsk(um.getAsk());
            m.setBid(um.getBid());
            m.setMarket(um.getMarket());
            if (m.getDifference() > 0) {
                m.setState(1);
                localNotify(i);
            } else if (m.getDifference() < 0) {
                m.setState(-1);
                localNotify(i);
            }

        }

        /**
         * 间隔一秒后刷新
         */
//
//        if (updateTime == 0 || System.currentTimeMillis() - updateTime > 500) {
//            updateTime = System.currentTimeMillis();
//        } else {
//            return;
//        }
//
//        for (int i = 0; i < mList.size(); i++) {
//            GetQuotesModel.DataBean.SymbolsBean m = mList.get(i);
//            RealTimeBean um = realList.get(i);
//            m.setDifference(um.getBid() - m.getBid());
//            m.setAsk(um.getAsk());
//            m.setBid(um.getBid());
//            m.setMarket(um.getMarket());
//            if (m.getDifference() > 0) {
//                m.setState(1);
//            } else if (m.getDifference() < 0) {
//                m.setState(-1);
//            }
//        }
//        notifyDataSetChanged();
    }

    /**
     * 局部刷新，减少性能消耗
     *
     * @param i
     */
    private void localNotify(int i) {

        notifyItemChanged(i, "lys");
    }


    public class HotForeignViewholder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_symbol_en)
        TextView tvSymbolEn;
        @Bind(R.id.tv_symbol_cn)
        TextView tvSymbolCn;
        @Bind(R.id.tv_price)
        TextView tvPrice;
        @Bind(R.id.iv_status)
        ImageView ivStatus;
        @Bind(R.id.tv_persent)
        TextView tvPersent;
        @Bind(R.id.ll_price_bg)
        LinearLayout llPriceBg;

        HotForeignViewholder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            AutoUtils.autoSize(view);
        }
    }

    public interface OnItemClickListenerr {
        void onSingleClick(GetQuotesModel.DataBean.SymbolsBean model, int position);

    }

    public interface OnItemLongClickListener {

        void onLongClick(GetQuotesModel.DataBean.SymbolsBean model, int position);
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
