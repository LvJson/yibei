package com.ts.lys.yibei.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.OrderPositionModel;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 订单 - 持仓
 */
public class OrderPositionAdapter extends RecyclerView.Adapter<OrderPositionAdapter.ViewHolder> {


    private Context mContext;
    private List<OrderPositionModel.DataBean.TraderOrderBean> mList;
    private Handler mHandler;
    private int tag = 0;// 标记是否是第一次进入
    private OnItemClickListener listener;

    private List<double[]> realTimeData;//0:market,1:ask,2:bid
    private double[] realTimeProfit;

//    public OrderPositionAdapter(Context context, List<OrderPositionModel.DataBean.TraderOrderBean> list, Handler handler) {
//        mContext = context;
//        mList = list;
//        mHandler = handler;
//    }

    public OrderPositionAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_order_position, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    public void setDatas(List<OrderPositionModel.DataBean.TraderOrderBean> traderOrderList, int tag, List<double[]> realTimeData, double[] realTimeProfit) {
        mList = traderOrderList;
        this.realTimeData = realTimeData;
        this.tag = tag;
        this.realTimeProfit = realTimeProfit;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {

            holder.tvClosePosition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClosePositionClick();
                }
            });

            ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick();
                }
            });
        }
//        final OrderPositionModel.DataBean.TraderOrderBean model = mList.get(position);
//        double fee = Arith.add(model.getSwaps(), model.getCommission());
//        int digits = model.getDigits();
//
//        String en = model.getSymbolEn();
//        holder.en_name_textview.setText(en);
//        holder.ch_name_textview.setText(model.getSymbolCn());
//
//        if (model.getCmd() == 0) {
//            holder.status_imageview.setImageResource(R.mipmap.p_icon_buy);
//        } else {
//            holder.status_imageview.setImageResource(R.mipmap.p_icon_sell);
//        }
//
//        holder.volume_textview.setText(model.getVolume() + "手");
//        holder.open_textview.setText(BaseUtils.getDigitsData(model.getOpenPrice(), model.getDigits()) + " - ");
//        if (tag == 0) {
//            holder.close_textview.setText(BaseUtils.getDigitsData(model.getMarket(), model.getDigits()) + "");
//            holder.total_textview.setText(BaseUtils.dealSymbol(Arith.add(model.getProfit(), fee)));
//            if (model.getProfit() > 0) {
//                holder.total_textview.setTextColor(mContext.getResources().getColor(R.color.account_login_red));
//            } else {
//                holder.total_textview.setTextColor(mContext.getResources().getColor(R.color.account_login_green));
//            }
//        } else {
//            //实时计算盈亏
//            double[] rdt = realTimeData.get(position);
//            double realTP = Arith.add(realTimeProfit[position], fee);
//
//            if (rdt != null) {
//                if (model.getCmd() == 0) {
//                    model.setClosePrice(rdt[2]);
//                    holder.close_textview.setText(BaseUtils.getDigitsData(rdt[2], digits) + "");//market
//                } else {
//                    model.setClosePrice(rdt[1]);
//                    holder.close_textview.setText(BaseUtils.getDigitsData(rdt[1], digits) + "");//ask
//                }
//                if (realTP > 0) {
//                    holder.total_textview.setTextColor(mContext.getResources().getColor(R.color.account_login_red));
//                } else {
//                    holder.total_textview.setTextColor(mContext.getResources().getColor(R.color.account_login_green));
//                }
//                holder.total_textview.setText(BaseUtils.dealSymbol(Arith.round(realTP, 2)));
//                model.setProfit(realTP);
//            }
//        }
//
//        //跳转页面
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Message msg = new Message();
//                msg.what = 0;
//                msg.obj = model;
//                mHandler.sendMessage(msg);
//            }
//        });
//        //平仓
//        holder.close_imageview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Message msg = new Message();
//                msg.what = 1;
//                msg.obj = model;
//                mHandler.sendMessage(msg);
//            }
//        });
    }

    @Override
    public int getItemCount() {
//        return mList == null ? 0 : mList.size();
        return 10;
    }

    public void update(List<OrderPositionModel.DataBean.TraderOrderBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_symbol_en)
        TextView tvSymbolEn;
        @Bind(R.id.tv_symbol_cn)
        TextView tvSymbolCn;
        @Bind(R.id.iv_status)
        ImageView ivStatus;
        @Bind(R.id.tv_lots)
        TextView tvLots;
        @Bind(R.id.tv_open_price)
        TextView tvOpenPrice;
        @Bind(R.id.tv_market_price)
        TextView tvMarketPrice;
        @Bind(R.id.tv_float_price)
        TextView tvFloatPrice;
        @Bind(R.id.tv_colse_position)
        TextView tvClosePosition;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            AutoUtils.autoSize(itemView);
        }
    }

    public interface OnItemClickListener {

        void onItemClick();

        void onClosePositionClick();
    }
}
