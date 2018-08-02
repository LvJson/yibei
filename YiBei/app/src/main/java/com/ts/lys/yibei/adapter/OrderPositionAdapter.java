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
import com.ts.lys.yibei.bean.OrderPositionModel;
import com.ts.lys.yibei.utils.Arith;
import com.ts.lys.yibei.utils.BaseUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 订单 - 持仓
 */
public class OrderPositionAdapter extends RecyclerView.Adapter<OrderPositionAdapter.ViewHolder> {


    private Context mContext;
    private OnItemClickListener listener;
    List<OrderPositionModel.DataBean.TraderOrderBean> traderOrderBeanList;


    public OrderPositionAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<OrderPositionModel.DataBean.TraderOrderBean> traderOrderBeanList) {
        this.traderOrderBeanList = null;
        this.traderOrderBeanList = traderOrderBeanList;
        notifyDataSetChanged();
    }

    /**
     * price中包括[ask,bid,market,profit]
     *
     * @param position
     * @param price
     */
    public synchronized void upData(int position, String symbolEn, double[] price) {
        if (traderOrderBeanList != null && price != null && price.length == 4 && traderOrderBeanList.get(position) != null && traderOrderBeanList.get(position).getSymbolEn().equals(symbolEn)) {
            OrderPositionModel.DataBean.TraderOrderBean traderOrderBean = traderOrderBeanList.get(position);
            traderOrderBean.setAsk(price[0]);
            traderOrderBean.setBid(price[1]);
            traderOrderBean.setMarket(price[2]);
            traderOrderBean.setProfit(price[3]);
            notifyDataSetChanged();
        }
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

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {
            final OrderPositionModel.DataBean.TraderOrderBean tb = traderOrderBeanList.get(position);

            holder.tvSymbolEn.setText(tb.getSymbolEn());
            holder.tvSymbolCn.setText(tb.getSymbolCn());
            if (tb.getCmd() == 0) {
                holder.ivStatus.setImageResource(R.mipmap.buy_icon);
                holder.tvMarketPrice.setText(mContext.getResources().getString(R.string.current_price2) + ":" + BaseUtils.getDigitsData(tb.getMarket(), tb.getDigits()));
            } else {
                holder.ivStatus.setImageResource(R.mipmap.sell_icon);
                holder.tvMarketPrice.setText(mContext.getResources().getString(R.string.current_price2) + ":" + BaseUtils.getDigitsData(tb.getAsk(), tb.getDigits()));

            }
            holder.tvLots.setText(tb.getVolume() + mContext.getResources().getString(R.string.lots));
            holder.tvOpenPrice.setText(mContext.getResources().getString(R.string.open_position_price) + ":" + BaseUtils.getDigitsData(tb.getOpenPrice(), tb.getDigits()));


            double fee = Arith.add(tb.getSwaps(), tb.getCommission());
            double realProfit = Arith.add(tb.getProfit(), fee);
            holder.tvFloatPrice.setText(BaseUtils.dealSymbol(realProfit));
            if (realProfit < 0)
                holder.tvFloatPrice.setTextColor(mContext.getResources().getColor(R.color.fall_color));
            else
                holder.tvFloatPrice.setTextColor(mContext.getResources().getColor(R.color.rise_color));


            /****************************************以下为点击监听*********************************************************/

            holder.llQuickDo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClosePositionClick(tb);
                }
            });

            ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(tb);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return traderOrderBeanList == null ? 0 : traderOrderBeanList.size();
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

        void onItemClick(OrderPositionModel.DataBean.TraderOrderBean tb);

        void onClosePositionClick(OrderPositionModel.DataBean.TraderOrderBean tb);
    }
}
