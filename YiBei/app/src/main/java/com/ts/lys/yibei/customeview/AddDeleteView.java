package com.ts.lys.yibei.customeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ts.lys.yibei.R;
import com.zhy.autolayout.AutoLinearLayout;

import java.text.DecimalFormat;


/**
 * Created by jcdev1 on 2018/6/4.
 */

public class AddDeleteView extends AutoLinearLayout implements View.OnTouchListener {

    Integer number;
    private ImageView txtDelete;
    private ImageView txtAdd;
    private EditText et_number;
    private RelativeLayout rlHead;
    private TextView tvExplain;

    private OnAddDelClickLstener lister;
    private int decimalDigits = 2;//小数的位数
    private DecimalFormat df0;
    private Context mContent;
    private ScrollView scrollView;
    private LinearLayout llTest;
    private int advHeight = 0;

    private boolean onClick = false;
    private double min = 0;
    private double max = 100000000;

    private boolean isStopLossOrProfit = false;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (getnumber() > max || max - getnumber() < 0.0000001 || max == getnumber()) {
                        onClick = false;
                        break;
                    }
                    lister.onAddClick();
                    break;
                case 1:
                    if (getnumber() < min || getnumber() - min < 0.0000001 || min == getnumber()) {
                        onClick = false;
                        break;
                    }
                    lister.onDelClick();
                    break;
            }

        }
    };

    //定义一个对外开放的接口
    public interface OnAddDelClickLstener {
        void onAddClick();

        void onDelClick();

        void onEditText(double lots);
    }

    public void setOnAddDelClickLstener(OnAddDelClickLstener lister) {
        if (lister != null) {
            this.lister = lister;
        }
    }

    public AddDeleteView(Context context) {
        this(context, null);
    }

    public AddDeleteView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddDeleteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
        initListener();
    }


    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        mContent = getContext();
        View.inflate(context, R.layout.zuhe, this);
        txtDelete = findViewById(R.id.txt_delete);
        txtAdd = findViewById(R.id.txt_add);
        et_number = findViewById(R.id.et_number);
        rlHead = findViewById(R.id.rl_head);
        tvExplain = findViewById(R.id.tv_explain);

        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.AddDeleteViewStyle);
//        String leftText = typeArray.getString(R.styleable.AddDeleteViewStyle_left_text);
//        String rightText = typeArray.getString(R.styleable.AddDeleteViewStyle_right_text);
        Drawable leftPic = typeArray.getDrawable(R.styleable.AddDeleteViewStyle_left_pic);
        Drawable rightPic = typeArray.getDrawable(R.styleable.AddDeleteViewStyle_right_pic);

//        int rightTextColor = typeArray.getColor(R.styleable.AddDeleteViewStyle_right_text_color, Color.RED);
        String middText = typeArray.getString(R.styleable.AddDeleteViewStyle_midd_text);

        txtDelete.setImageDrawable(leftPic);
        txtAdd.setImageDrawable(rightPic);
        if (middText.equals("0"))
            et_number.setText(null);
        else
            et_number.setText(middText);
        et_number.setCursorVisible(false);
        typeArray.recycle();
    }


    private void initListener() {

        et_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ss = s.toString().trim();

                //如果最小手数大于1，一旦出现小数点就禁止输入
                if (min >= 1 && ss.contains(".") && decimalDigits == 0) {
                    Toast.makeText(mContent, "最小手数：" + (int) min + "手", Toast.LENGTH_LONG).show();
                    et_number.setText(ss.substring(0, ss.length() - 1));
                    return;
                }

                countrolPointCount(s, et_number);

                if (lister == null) return;
                if (!TextUtils.isEmpty(ss)) {
                    try {
                        if (ss.contains(".") && s.length() - 1 - s.toString().indexOf(".") > decimalDigits)
                            return;
                        double sd = Double.valueOf(ss);
                        lister.onEditText(sd);

                    } catch (Exception e) {
                        lister.onEditText(0);
                    }
                } else
                    lister.onEditText(0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_number.setOnTouchListener(this);
        txtDelete.setOnTouchListener(this);
        txtAdd.setOnTouchListener(this);

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {
            /**
             * 减
             */
            case R.id.txt_delete:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    onClick = true;
                    new Thread() {
                        @Override
                        public void run() {
                            while (onClick) {
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                handler.sendEmptyMessage(1);
                            }
                        }
                    }.start();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    onClick = false;
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    onClick = false;
                }
                return true;
            /**
             * 加
             */
            case R.id.txt_add:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    onClick = true;
                    new Thread() {
                        @Override
                        public void run() {
                            while (onClick) {
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                handler.sendEmptyMessage(0);

                            }
                        }
                    }.start();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    onClick = false;
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    onClick = false;
                }
                return true;
            /**
             *点击文本框，控制scrollview滚动
             */
            case R.id.et_number:
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    et_number.setCursorVisible(true);// 再次点击显示光标

                    scrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.scrollTo(0, llTest.getBottom() - advHeight - scrollView.getHeight());
                        }
                    }, 300);
                }
                return false;
        }
        return true;
    }

    /**
     * 设置取值范围
     *
     * @param min
     * @param max
     */
    public void setLimit(double min, double max, int decimalDigits) {
        if (min > 0)
            this.min = min;
        if (max > 0)
            this.max = max;
        if (min < 0)
            this.min = min;

        this.decimalDigits = decimalDigits;

        if (decimalDigits > 0) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("0.");
            for (int i = 0; i < decimalDigits; i++) {
                buffer.append("0");
            }
            df0 = new DecimalFormat(buffer.toString());
        } else
            df0 = new DecimalFormat("#");
    }

    /**
     * 控制scrollview滚动到自己想要的位置
     *
     * @param scrollView
     * @param lltest
     * @param adv
     */
    public void setScrollView(ScrollView scrollView, LinearLayout lltest, int adv) {
        this.scrollView = scrollView;
        this.llTest = lltest;
        this.advHeight = adv;
    }

    /**
     * 中断长按循环
     *
     * @param onClick
     */
    public void setOnClickStatus(boolean onClick) {
        this.onClick = onClick;
    }


    /**
     * 对外提供设置EditText值的方法
     *
     * @param number
     */
    public void setnumber(double number) {

        if (number > 0 && df0 != null) {
            et_number.setText(df0.format(number));
        }

    }

    public void setTvExplain(String explain) {
        if (tvExplain != null)
            tvExplain.setText(explain);
    }

    /**
     * 是否用于止盈止损
     *
     * @param isStopLossOrProfit
     */
    public void setIsStopLossOrProfit(boolean isStopLossOrProfit) {
        this.isStopLossOrProfit = isStopLossOrProfit;
    }

    /**
     * 得到控件原来的值
     *
     * @return
     */
    public double getnumber() {
        double number = 0;
        try {
            String numberStr = et_number.getText().toString().trim();
            number = Double.valueOf(numberStr);

        } catch (Exception e) {
            number = 0;
        }
        return number;
    }

    /**
     * 控制小数点位数
     *
     * @param s
     * @param editText
     */
    private void countrolPointCount(CharSequence s, EditText editText) {


        editText.setSelection(s.length());

        /**
         * 输入内容包含小数点且位数大于限制位数的
         */
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > decimalDigits) {
                s = s.toString().subSequence(0,
                        s.toString().indexOf(".") + decimalDigits + 1);
                editText.setText(s);
                editText.setSelection(s.length());
            }
        }
        /**
         * 输入内容"."开头的
         */
        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            editText.setText(s);
            editText.setSelection(2);
        }
        /**
         * 输入内容"0"开头的
         */
        if (s.toString().startsWith("0")
                && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                editText.setText(s.subSequence(0, 1));
                editText.setSelection(1);
                return;
            }
        }

    }

    /**
     * 动态设置文本框光标是否显示以及文字展示样式
     *
     * @param status
     */
    public void setEditTextStatus(boolean status) {

        if (et_number != null)
            if (status)
                et_number.setCursorVisible(true);
            else {
                et_number.setCursorVisible(false);
                //检查文本框 补全小数点位数
                if (getnumber() > 0 && df0 != null)
                    et_number.setText(df0.format(getnumber()));
                else {
                    if (isStopLossOrProfit)
                        et_number.setText(null);
                    else
                        et_number.setText(df0.format(min));
                }
            }

    }

    /**
     * 隐藏软键盘
     *
     * @param view
     */
    private void hideSoftWindow(View view) {
        InputMethodManager imm = (InputMethodManager)
                mContent.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        imm.showSoftInputFromInputMethod(view.getWindowToken(), 1);
        et_number.setCursorVisible(false);
    }

}



