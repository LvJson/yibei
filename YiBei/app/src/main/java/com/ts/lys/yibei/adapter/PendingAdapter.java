package com.ts.lys.yibei.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.OrderPendingModel;
import com.ts.lys.yibei.utils.BaseUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 订单 - 持仓
 */
public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.ViewHolder> {


    private Context mContext;
    List<OrderPendingModel.DataBean.PendOrderBean> pendingList;

    private List<double[]> realTimeData;//0:market,1:ask,2:bid
    private double[] realTimeProfit;

    private OnItemClickListener listener;

    public PendingAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<OrderPendingModel.DataBean.PendOrderBean> pendingList) {
        this.pendingList = null;
        this.pendingList = pendingList;
        notifyDataSetChanged();
    }

    /**
     * price[ask,bid,market]
     *
     * @param position
     * @param price
     */
    public void update(int position, double[] price) {

        if (pendingList != null && pendingList.size() > 0 && pendingList.get(position) != null) {

            OrderPendingModel.DataBean.PendOrderBean pob = pendingList.get(position);

            if (pob.getCmd() == 2 || pob.getCmd() == 4)
                pob.setMarket(price[1]);
            else
                pob.setMarket(price[0]);

            notifyDataSetChanged();
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_order_position, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }


    public void setOnItemClickListener(OnItemClickListener listener) {

        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {

            final OrderPendingModel.DataBean.PendOrderBean pb = pendingList.get(position);

            holder.tvFloatPrice.setVisibility(View.GONE);
            holder.tvClosePosition.setText(mContext.getString(R.string.withdrawal));
            holder.ivStatus.setImageResource(R.mipmap.pending_icon);


            holder.tvSymbolCn.setText(pb.getSymbolCn());
            holder.tvSymbolEn.setText(pb.getSymbolEn());
            holder.tvLots.setText(pb.getVolume() + mContext.getString(R.string.lots));

            holder.tvOpenPrice.setText(mContext.getString(R.string.opening_price) + ":" + BaseUtils.getDigitsData(pb.getOpenPrice(), pb.getDigits()));
            holder.tvMarketPrice.setText(mContext.getString(R.string.current_price2) + ":" + BaseUtils.getDigitsData(pb.getMarket(), pb.getDigits()));

            /**************************************************************************/

            holder.llQuickDo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCanclePendingClick(pb);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(pb);
                }
            });


        }


    }

    @Override
    public int getItemCount() {
        return pendingList == null ? 0 : pendingList.size();
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
        @Bind(R.id.ll_quick_do)
        LinearLayout llQuickDo;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            AutoUtils.autoSize(itemView);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(OrderPendingModel.DataBean.PendOrderBean pb);

        void onCanclePendingClick(OrderPendingModel.DataBean.PendOrderBean pb);
    }
}
