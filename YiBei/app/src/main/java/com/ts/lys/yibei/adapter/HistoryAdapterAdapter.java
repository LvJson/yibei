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
import android.widget.TextView;

import com.ts.lys.yibei.R;
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
    private List<?> activityBeanList;

    /**
     * 记录详情被打开的位置
     */
    private Map<Integer, Boolean> map = new HashMap<>();


    public HistoryAdapterAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        for (int i = 0; i < 10; i++) {
            map.put(i, false);
        }
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

        return new HistoryViewholder(mInflater.inflate(R.layout.item_trade_history_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof HistoryViewholder) {

            if (map.get(position))
                ((HistoryViewholder) holder).llMore.setVisibility(View.VISIBLE);
            else
                ((HistoryViewholder) holder).llMore.setVisibility(View.GONE);

            ((HistoryViewholder) holder).ivDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HistoryViewholder) holder).llMore.measure(0, 0);
                    openAnim(((HistoryViewholder) holder).llMore, ((HistoryViewholder) holder).llMore.getMeasuredHeight());
                    map.put(position, true);
                    ((HistoryViewholder) holder).ivDown.setVisibility(View.GONE);

                }
            });

            ((HistoryViewholder) holder).ivUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeAnimate(((HistoryViewholder) holder).llMore);
                    map.put(position, false);
                    ((HistoryViewholder) holder).ivDown.setVisibility(View.VISIBLE);
                }
            });

        }

    }

    @Override
    public int getItemCount() {

        return 10;
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
        @Bind(R.id.ll_one)
        AutoLinearLayout llOne;
        @Bind(R.id.tv_position_price)
        TextView tvPositionPrice;
        @Bind(R.id.tv_one)
        TextView tvOne;
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
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(million);
        return formatter.format(calendar.getTime());
    }

    private void openAnim(View v, int mHeight) {
        v.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(v, 0, mHeight);
        animator.start();
    }

    private void closeAnimate(final View view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
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
