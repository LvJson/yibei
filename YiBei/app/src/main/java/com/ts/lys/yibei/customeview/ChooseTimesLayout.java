package com.ts.lys.yibei.customeview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.ts.lys.yibei.R;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * Created by jcdev1 on 2018/6/20.
 */

public class ChooseTimesLayout extends AutoLinearLayout implements View.OnClickListener {

    private OnItemClickListener listener;

    private TextView tvOneTimes;
    private TextView tvTwoTimes;
    private TextView tvThreeTimes;
    private TextView tvFourTimes;

    public static final float ZERO_TIMES = 0.00f;
    public static final float ONE_TIMES = 0.01f;
    public static final float TWO_TIMES = 0.10f;
    public static final float THREE_TIMES = 0.50f;
    public static final float FOUR_TIMES = 1.00f;

    /**
     * 当前选中的位置
     */
    private int clickItemtag = 0;

    /**
     * 最低购买手数
     */
    private double mintimes = 0.01;


    public ChooseTimesLayout(Context context) {
        this(context, null);
    }

    public ChooseTimesLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChooseTimesLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.simple_choose_times_layout, this);
        tvOneTimes = findViewById(R.id.tv_one_times);
        tvTwoTimes = findViewById(R.id.tv_two_times);
        tvThreeTimes = findViewById(R.id.tv_three_times);
        tvFourTimes = findViewById(R.id.tv_four_times);

        tvOneTimes.setOnClickListener(this);
        tvTwoTimes.setOnClickListener(this);
        tvThreeTimes.setOnClickListener(this);
        tvFourTimes.setOnClickListener(this);


    }


    public interface OnItemClickListener {
        void onItemClick(float times);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        if (listener != null)
            this.listener = listener;

    }


    /**
     * 最低购买手数
     */
    public void setBaseTimes(double minTimes) {
        this.mintimes = minTimes;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_one_times:
                if (clickItemtag == 0)
                    return;
                else
                    clickItemtag = 0;
                if (mintimes > ONE_TIMES) {
                    listener.onItemClick(ZERO_TIMES);
                    return;
                }
                setTextBg(0);
                listener.onItemClick(ONE_TIMES);
                break;
            case R.id.tv_two_times:
                if (clickItemtag == 1)
                    return;
                else
                    clickItemtag = 1;
                if (mintimes > TWO_TIMES) {
                    listener.onItemClick(ZERO_TIMES);
                    return;
                }
                setTextBg(1);
                listener.onItemClick(TWO_TIMES);
                break;
            case R.id.tv_three_times:
                if (clickItemtag == 2)
                    return;
                else
                    clickItemtag = 2;
                if (mintimes > THREE_TIMES) {
                    listener.onItemClick(ZERO_TIMES);
                    return;
                }
                setTextBg(2);
                listener.onItemClick(THREE_TIMES);
                break;
            case R.id.tv_four_times:
                if (clickItemtag == 3)
                    return;
                else
                    clickItemtag = 3;
                if (mintimes > FOUR_TIMES) {
                    listener.onItemClick(ZERO_TIMES);
                    return;
                }
                setTextBg(3);
                listener.onItemClick(FOUR_TIMES);
                break;
        }
    }

    private void setTextBg(int tag) {
        tvOneTimes.setBackgroundResource(R.drawable.simple_times_bg);
        tvTwoTimes.setBackgroundResource(R.drawable.simple_times_bg);
        tvThreeTimes.setBackgroundResource(R.drawable.simple_times_bg);
        tvFourTimes.setBackgroundResource(R.drawable.simple_times_bg);

        tvOneTimes.setTextColor(getResources().getColor(R.color.one_text_color));
        tvTwoTimes.setTextColor(getResources().getColor(R.color.one_text_color));
        tvThreeTimes.setTextColor(getResources().getColor(R.color.one_text_color));
        tvFourTimes.setTextColor(getResources().getColor(R.color.one_text_color));

        switch (tag) {
            case 0:
                tvOneTimes.setBackgroundResource(R.drawable.btn_gradual_change_bg);
                tvOneTimes.setTextColor(getResources().getColor(R.color.white));
                break;
            case 1:
                tvTwoTimes.setBackgroundResource(R.drawable.btn_gradual_change_bg);
                tvTwoTimes.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                tvThreeTimes.setBackgroundResource(R.drawable.btn_gradual_change_bg);
                tvThreeTimes.setTextColor(getResources().getColor(R.color.white));
                break;
            case 3:
                tvFourTimes.setBackgroundResource(R.drawable.btn_gradual_change_bg);
                tvFourTimes.setTextColor(getResources().getColor(R.color.white));
                break;

        }


    }

}
