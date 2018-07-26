package com.ts.lys.yibei.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.OrderHistoryModel;
import com.ts.lys.yibei.utils.BaseUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jcdev1 on 2018/5/9.
 */

public class HistoryAdapterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private OnItemClickListenerr listenerr;
    List<OrderHistoryModel.DataBean.HistoryOrderBean> historyOrderBeanList;
    private int llMoreHegiht;
    private boolean isFirst = true;

    /**
     * 记录详情被打开的位置
     */
    private Map<Integer, Boolean> map = new HashMap<>();


    public HistoryAdapterAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<OrderHistoryModel.DataBean.HistoryOrderBean> historyOrderBeanList) {
        this.historyOrderBeanList = null;
        this.historyOrderBeanList = historyOrderBeanList;
        for (int i = 0; i < historyOrderBeanList.size(); i++) {
            map.put(i, false);
        }
        notifyDataSetChanged();

    }

    public void setOnItemClickListenerr(OnItemClickListenerr listenerr) {
        this.listenerr = listenerr;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new HistoryViewholder(mInflater.inflate(R.layout.item_trade_history_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof HistoryViewholder) {

            OrderHistoryModel.DataBean.HistoryOrderBean hb = historyOrderBeanList.get(position);
            ((HistoryViewholder) holder).tvSymbolCn.setText(hb.getSymbolCn());
            ((HistoryViewholder) holder).tvSymbolEn.setText(hb.getSymbolEn());
            ((HistoryViewholder) holder).tvOpenPrice.setText(String.valueOf(hb.getOpenPrice()));
            ((HistoryViewholder) holder).tvPositionPrice.setText(String.valueOf(hb.getClosePrice()));
            ((HistoryViewholder) holder).tvIncome.setText(BaseUtils.dealSymbol(hb.getProfit()));
            if (hb.getProfit() < 0)
                ((HistoryViewholder) holder).tvIncome.setTextColor(mContext.getResources().getColor(R.color.fall_color));
            else
                ((HistoryViewholder) holder).tvIncome.setTextColor(mContext.getResources().getColor(R.color.rise_color));
            if (hb.getCmd() == 0)
                ((HistoryViewholder) holder).ivStatus.setImageResource(R.mipmap.buy_icon);
            else
                ((HistoryViewholder) holder).ivStatus.setImageResource(R.mipmap.sell_icon);

            ((HistoryViewholder) holder).tvDate.setText(hb.getCloseTime());
            ((HistoryViewholder) holder).tvOrderNum.setText(mContext.getString(R.string.order_number) + "：" + hb.getTicket());

            ((HistoryViewholder) holder).inventoryFee.setText(mContext.getString(R.string.inventory_fee) + "：" + hb.getSwaps());
            ((HistoryViewholder) holder).tvHandlingFee.setText(mContext.getString(R.string.handling_fee) + "：" + hb.getCommission());

            ((HistoryViewholder) holder).tvTradeLots.setText(mContext.getString(R.string.trade_times) + "：" + hb.getVolume() + mContext.getString(R.string.lots));
            ((HistoryViewholder) holder).tvUseMargin.setText(mContext.getString(R.string.user_margin2) + "：" + BaseUtils.dealSymbol(hb.getMargin()));
            ((HistoryViewholder) holder).tvStopLoss.setText(mContext.getString(R.string.stop_loss_price) + "：" + hb.getSl());
            ((HistoryViewholder) holder).tvStopProfit.setText(mContext.getString(R.string.stop_profit_price) + "：" + hb.getTp());
            ((HistoryViewholder) holder).tvOpenPositionTime.setText(mContext.getString(R.string.position_time) + "：" + hb.getOpenTime());


            /********************************************************************************************************/
            if (isFirst) {
                ((HistoryViewholder) holder).llMore.measure(0, 0);
                llMoreHegiht = ((HistoryViewholder) holder).llMore.getMeasuredHeight();
                isFirst = false;
            }

            ((HistoryViewholder) holder).ivDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((HistoryViewholder) holder).llMore.setVisibility(View.VISIBLE);
                    ((HistoryViewholder) holder).ivDown.setVisibility(View.GONE);

                    openAnim(((HistoryViewholder) holder).llMore, llMoreHegiht);

                    map.put(position, true);
                }
            });

            ((HistoryViewholder) holder).ivUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    closeAnimate(((HistoryViewholder) holder).llMore);
                    ((HistoryViewholder) holder).ivDown.setVisibility(View.VISIBLE);

                    map.put(position, false);
                }
            });


            if (map.get(position)) {
                /**
                 * 一下三行代码说明：由于属性动画的原因导致setVisibility()无效
                 * 可能原因是：执行动画的view属性被修改（即高度被修改为0），所以
                 * 在下次未通过动画显示该view的时候需从新设置该view的高度。
                 */
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ((HistoryViewholder) holder).llMore.getLayoutParams();
                layoutParams.height = llMoreHegiht;
                ((HistoryViewholder) holder).llMore.setLayoutParams(layoutParams);

                ((HistoryViewholder) holder).llMore.setVisibility(View.VISIBLE);
                ((HistoryViewholder) holder).ivDown.setVisibility(View.GONE);

            } else {
                ((HistoryViewholder) holder).llMore.setVisibility(View.GONE);
                ((HistoryViewholder) holder).ivDown.setVisibility(View.VISIBLE);
            }

        }

    }

    @Override
    public int getItemCount() {

        return historyOrderBeanList == null ? 0 : historyOrderBeanList.size();
    }

    public class HistoryViewholder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_symbol_en)
        TextView tvSymbolEn;
        @Bind(R.id.tv_symbol_cn)
        TextView tvSymbolCn;
        @Bind(R.id.iv_status)
        ImageView ivStatus;
        @Bind(R.id.tv_open_price)
        TextView tvOpenPrice;
        @Bind(R.id.tv_position_price)
        TextView tvPositionPrice;
        @Bind(R.id.tv_income)
        TextView tvIncome;
        @Bind(R.id.tv_date)
        TextView tvDate;
        @Bind(R.id.iv_down)
        ImageView ivDown;
        @Bind(R.id.tv_order_num)
        TextView tvOrderNum;
        @Bind(R.id.inventory_fee)
        TextView inventoryFee;
        @Bind(R.id.tv_handling_fee)
        TextView tvHandlingFee;
        @Bind(R.id.tv_trade_lots)
        TextView tvTradeLots;
        @Bind(R.id.tv_use_margin)
        TextView tvUseMargin;
        @Bind(R.id.tv_stop_loss)
        TextView tvStopLoss;
        @Bind(R.id.tv_stop_profit)
        TextView tvStopProfit;
        @Bind(R.id.tv_open_position_time)
        TextView tvOpenPositionTime;
        @Bind(R.id.iv_up)
        ImageView ivUp;
        @Bind(R.id.ll_more)
        AutoLinearLayout llMore;

        HistoryViewholder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            AutoUtils.autoSize(view);
        }
    }

    public interface OnItemClickListenerr {

    }

    public static String millionToDate(Long million) {
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(million);
        return formatter.format(calendar.getTime());
    }

    private void openAnim(final View v, int mHeight) {

        ValueAnimator animator = createDropAnimator(v, 0, mHeight);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                v.clearAnimation();
            }
        });
        animator.start();
    }

    private void closeAnimate(final View view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.clearAnimation();
                view.setVisibility(View.GONE);
            }
        });
        animator.start();

    }

    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                int value = (int) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
}
