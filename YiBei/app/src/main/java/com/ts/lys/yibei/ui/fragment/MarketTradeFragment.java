package com.ts.lys.yibei.ui.fragment;

import android.view.View;
import android.widget.TextView;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.EventBean;
import com.ts.lys.yibei.bean.MarginAndProfitBean;
import com.ts.lys.yibei.bean.RealTimeBean;
import com.ts.lys.yibei.bean.RealTimeQuoteDatas;
import com.ts.lys.yibei.bean.SymbolInfo;
import com.ts.lys.yibei.constant.EventContents;
import com.ts.lys.yibei.customeview.AddDeleteView;
import com.ts.lys.yibei.customeview.KeyboardLayout;
import com.ts.lys.yibei.ui.activity.QuotationsActivity;
import com.ts.lys.yibei.utils.Arith;
import com.ts.lys.yibei.utils.BaseUtils;
import com.ts.lys.yibei.utils.CalMarginAndProfitUtil;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jcdev1 on 2018/6/21.
 */

public class MarketTradeFragment extends BaseFragment {
    @Bind(R.id.tv_buy_price)
    TextView tvBuyPrice;
    @Bind(R.id.tv_sell_price)
    TextView tvSellPrice;
    @Bind(R.id.trade_times_adv)
    AddDeleteView tradeTimesAdv;
    @Bind(R.id.stop_loss_adv)
    AddDeleteView stopLossAdv;
    @Bind(R.id.stop_profit_adv)
    AddDeleteView stopProfitAdv;

    QuotationsActivity parentActivity;
    ComplexTradeFragment parentFragment;


    @Bind(R.id.tv_buy_in)
    TextView tvBuyIn;
    @Bind(R.id.ll_buy_price)
    AutoLinearLayout llBuyPrice;
    @Bind(R.id.tv_sell_out)
    TextView tvSellOut;
    @Bind(R.id.ll_sell_price)
    AutoLinearLayout llSellPrice;

    /**
     * 交易手数步长
     */
    private double tradetimesLever = 0.01;

    /**
     * 止损步长
     */
    private double stopLossLever = 0.00001;

    /**
     * 止盈步长
     */
    private double stopProfitLever = 0.00001;

    private MarginAndProfitBean mapb = new MarginAndProfitBean();

    private double lots = 0;//手数

    private int cmd = 0;//0:买涨  1：买跌

    /**
     * 当前货币的ask，bid值
     */
    private double[] currentCurrency = new double[]{0, 0};
    /**
     * 保证金计算需要：提供转换货币的ask，bid值
     */
    private double[] marginCalCurrency = new double[]{0, 0};
    /**
     * 计算预计收益时使用：提供转换货币的ask，bid值
     */
    private double[] profitCalCurrency = new double[]{0, 0};

    private SymbolInfo.DataBean.SymbolInfoBean symbolInfoBean;

    /**
     * 软键盘是否出现
     */
    private boolean isShowKeybord = false;


    @Override
    protected int getLayoutID() {
        return R.layout.fragment_market_trade;
    }

    @Override
    protected void initBaseView() {
        initView();
        initListener();
        initData();
    }


    private void initView() {
        parentActivity = (QuotationsActivity) getActivity();
        parentFragment = (ComplexTradeFragment) getParentFragment();
        int baseHeight = (int) (BaseUtils.getScreenHeight(getActivity()) * 0.14692654);
        /**
         * 交易手数
         */
        tradeTimesAdv.setScrollView(parentActivity.scrollView, parentActivity.llScrollContent, 2 * baseHeight);
        /**
         *止损价格
         */
        stopLossAdv.setScrollView(parentActivity.scrollView, parentActivity.llScrollContent, baseHeight);
        /**
         *止盈价格
         */
        stopProfitAdv.setScrollView(parentActivity.scrollView, parentActivity.llScrollContent, baseHeight);
    }

    private void initData() {
        setSymbolCalInfo(parentActivity.sbinfo);
        setFirstRealTimePrice(parentActivity.quote);
    }

    private void initListener() {
        /**
         * 交易手数
         */
        tradeTimesAdv.setOnAddDelClickLstener(new AddDeleteView.OnAddDelClickLstener() {
            @Override
            public void onAddClick() {
                double getnumber = tradeTimesAdv.getnumber();
                getnumber += tradetimesLever;
                lots = getnumber;
                setMargin();
                tradeTimesAdv.setnumber(getnumber);
            }

            @Override
            public void onDelClick() {
                double getnumber = tradeTimesAdv.getnumber();
                getnumber -= tradetimesLever;
                lots = getnumber;
                setMargin();
                tradeTimesAdv.setnumber(getnumber);
            }

            @Override
            public void onEditText(double lots) {
                MarketTradeFragment.this.lots = lots;
                setMargin();
            }
        });
        /**
         * 止损价格
         */
        stopLossAdv.setOnAddDelClickLstener(new AddDeleteView.OnAddDelClickLstener() {
            @Override
            public void onAddClick() {
                double getnumber = stopLossAdv.getnumber();
                getnumber += stopLossLever;
                stopLossAdv.setnumber(getnumber);
                initstopLossLimit();
            }

            @Override
            public void onDelClick() {
                double getnumber = stopLossAdv.getnumber();
                getnumber -= stopLossLever;
                stopLossAdv.setnumber(getnumber);
                initstopLossLimit();

            }

            @Override
            public void onEditText(double lots) {
                initstopLossLimit();
            }
        });
        /**
         * 止盈价格
         */
        stopProfitAdv.setOnAddDelClickLstener(new AddDeleteView.OnAddDelClickLstener() {
            @Override
            public void onAddClick() {
                double getnumber = stopProfitAdv.getnumber();
                getnumber += stopProfitLever;
                stopProfitAdv.setnumber(getnumber);
                initStopProfitLimit();
            }

            @Override
            public void onDelClick() {
                double getnumber = stopProfitAdv.getnumber();
                getnumber -= stopProfitLever;
                stopProfitAdv.setnumber(getnumber);
                initStopProfitLimit();
            }

            @Override
            public void onEditText(double lots) {
                initStopProfitLimit();

            }
        });

    }

    /**
     * 软键盘状态
     *
     * @param state
     */
    public void setKeyboardStatus(int state) {
        if (state == KeyboardLayout.KEYBOARD_STATE_HIDE) {
            isShowKeybord = false;
            tradeTimesAdv.setEditTextStatus(false);
            stopLossAdv.setEditTextStatus(false);
            stopProfitAdv.setEditTextStatus(false);
        } else if (state == KeyboardLayout.KEYBOARD_STATE_SHOW)
            isShowKeybord = true;
    }

    @OnClick({R.id.ll_buy_price, R.id.ll_sell_price})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_buy_price:
                parentActivity.marketDealStatus = 0;
                EventBus.getDefault().post(new EventBean(EventContents.BUTTON_STATUS_CHANGE, null));

                llBuyPrice.setBackgroundResource(R.drawable.rise_bg);
                llSellPrice.setBackgroundResource(R.drawable.simple_times_bg);
                tvBuyIn.setTextColor(getResources().getColor(R.color.white));
                tvBuyPrice.setTextColor(getResources().getColor(R.color.white));
                tvSellOut.setTextColor(getResources().getColor(R.color.two_text_color));
                tvSellPrice.setTextColor(getResources().getColor(R.color.two_text_color));

                cmd = 0;

                initstopLossLimit();
                initStopProfitLimit();
                setMargin();

                break;
            case R.id.ll_sell_price:
                parentActivity.marketDealStatus = 1;
                EventBus.getDefault().post(new EventBean(EventContents.BUTTON_STATUS_CHANGE, null));

                llBuyPrice.setBackgroundResource(R.drawable.simple_times_bg);
                llSellPrice.setBackgroundResource(R.drawable.rise_bg);
                tvBuyIn.setTextColor(getResources().getColor(R.color.two_text_color));
                tvBuyPrice.setTextColor(getResources().getColor(R.color.two_text_color));
                tvSellOut.setTextColor(getResources().getColor(R.color.white));
                tvSellPrice.setTextColor(getResources().getColor(R.color.white));

                cmd = 1;

                initstopLossLimit();
                initStopProfitLimit();
                setMargin();
                break;
        }
    }


    /**
     * 接收顺序 1
     * <p>
     * 接收实时价格
     *
     * @param quoteBeen
     */
    public void setRealPrice(RealTimeBean quoteBeen) {
        if (quoteBeen.getSymbol().equals(parentActivity.symbol)) {
            tvBuyPrice.setText(BaseUtils.getDigitsData(quoteBeen.getAsk(), parentActivity.digits));
            tvSellPrice.setText(BaseUtils.getDigitsData(quoteBeen.getBid(), parentActivity.digits));
        }

        if (symbolInfoBean != null) {

            if (quoteBeen.getSymbol().equals(symbolInfoBean.getMarginCalCurrency())) {
                marginCalCurrency[0] = quoteBeen.getAsk();
                marginCalCurrency[1] = quoteBeen.getBid();
            }

            if (quoteBeen.getSymbol().equals(parentActivity.symbol)) {
                currentCurrency[0] = quoteBeen.getAsk();
                currentCurrency[1] = quoteBeen.getBid();
            }

            if (quoteBeen.getSymbol().equals(symbolInfoBean.getProfitCalCurrency())) {
                profitCalCurrency[0] = quoteBeen.getAsk();
                profitCalCurrency[1] = quoteBeen.getBid();
            }
            setMargin();
            initstopLossLimit();
            initStopProfitLimit();

        }


    }

    /**
     * 接收顺序 2
     * <p>
     * 品种保证金及止盈止损数据接口
     *
     * @param sb
     */
    public void setSymbolCalInfo(SymbolInfo.DataBean.SymbolInfoBean sb) {
        if (sb == null) return;
        symbolInfoBean = sb;
        mapb.setSymbolInfoBean(sb);
        lots = sb.getMinVolume();
        tradetimesLever = sb.getMinVolume();
        if (sb.getMinVolume() >= 1)
            tradeTimesAdv.setLimit(sb.getMinVolume(), 10, 0);//TODO 第二个参数待定
        else
            tradeTimesAdv.setLimit(sb.getMinVolume(), 10, 2);//TODO 第二个参数待定
        tradeTimesAdv.setnumber(sb.getMinVolume());
        //************************止盈/止损步长***********************************//
        stopLossLever = Arith.div(1, Math.pow(10, parentActivity.digits));
        stopProfitLever = Arith.div(1, Math.pow(10, parentActivity.digits));
    }

    /**
     * 接收顺序 3
     * <p>
     * 多个symbol的最新价格
     *
     * @param quote
     */
    public void setFirstRealTimePrice(List<RealTimeQuoteDatas.DataBean.QuoteBean> quote) {
        if (quote == null) return;
        for (int i = 0; i < quote.size(); i++) {

            if (quote.get(i).getSymbol().equals(parentActivity.symbol)) {
                currentCurrency[0] = quote.get(i).getAsk();
                currentCurrency[1] = quote.get(i).getBid();
                tvBuyPrice.setText(BaseUtils.getDigitsData(currentCurrency[0], parentActivity.digits));
                tvSellPrice.setText(BaseUtils.getDigitsData(currentCurrency[1], parentActivity.digits));
            }

            if (quote.get(i).getSymbol().equals(symbolInfoBean.getMarginCalCurrency())) {
                marginCalCurrency[0] = quote.get(i).getAsk();
                marginCalCurrency[1] = quote.get(i).getBid();
            }

            if (quote.get(i).getSymbol().equals(symbolInfoBean.getProfitCalCurrency())) {
                profitCalCurrency[0] = quote.get(i).getAsk();
                profitCalCurrency[1] = quote.get(i).getBid();
            }

        }
        initstopLossLimit();
        initStopProfitLimit();
        setMargin();
    }


    /**
     * 初始化止损价格列数据展示
     */
    private void initstopLossLimit() {
        double stopLossLimit = CalMarginAndProfitUtil.stopLossOrprofitLimit(currentCurrency, symbolInfoBean.getStopsLevel(), parentActivity.digits, cmd, 0);

        stopLossAdv.setLimit(0, stopLossLimit, parentActivity.digits);

        /**
         * 如果加减后的价格不在范围内则强制改变输入框的价格（注意：是点击加号和减号的处理逻辑）
         *
         * 如果软键盘出现，即手动输入时不允许强制修改输入框里的内容
         */
        if (!isShowKeybord)
            switch (cmd) {
                case 0:
                    if (stopLossAdv.getnumber() > stopLossLimit) {
                        stopLossAdv.setnumber(stopLossLimit);
                    }
                    break;
                case 1:
                    if (stopLossAdv.getnumber() < stopLossLimit) {
                        stopLossAdv.setnumber(stopLossLimit);
                    }
                    break;
            }

        mapb.setLots(lots);
        mapb.setCmd(cmd);
        mapb.setProfitCalCurrency(profitCalCurrency);
        mapb.setOpenPrice(currentCurrency[cmd]);
        mapb.setClosePrice(stopLossAdv.getnumber());
        double profit = CalMarginAndProfitUtil.getProfit(mapb);

        String explain;
        if (cmd == 0)
            explain = "价格<" + BaseUtils.getDigitsData(stopLossLimit, parentActivity.digits) + "  预计盈亏：" + BaseUtils.dealSymbol(profit);
        else
            explain = "价格>" + BaseUtils.getDigitsData(stopLossLimit, parentActivity.digits) + "  预计盈亏：" + BaseUtils.dealSymbol(profit);
        stopLossAdv.setTvExplain(explain);
    }

    /**
     * 初始化止盈列表数据展示
     */
    private void initStopProfitLimit() {
        double stopProfitLimit = CalMarginAndProfitUtil.stopLossOrprofitLimit(currentCurrency, symbolInfoBean.getStopsLevel(), parentActivity.digits, cmd, 1);
        stopProfitAdv.setLimit(stopProfitLimit, 0, parentActivity.digits);

        /**
         * 如果加减后的价格不在范围内则强制改变输入框的价格（注意：是点击加号和减号的处理逻辑）
         *
         * 如果软键盘出现，即手动输入时不允许强制修改输入框里的内容
         */
        if (!isShowKeybord)
            switch (cmd) {
                case 0:
                    if (stopProfitAdv.getnumber() < stopProfitLimit) {
                        stopProfitAdv.setnumber(stopProfitLimit);
                    }
                    break;
                case 1:
                    if (stopProfitAdv.getnumber() > stopProfitLimit) {
                        stopProfitAdv.setnumber(stopProfitLimit);
                    }
                    break;
            }

        mapb.setLots(lots);
        mapb.setCmd(cmd);
        mapb.setProfitCalCurrency(profitCalCurrency);
        mapb.setOpenPrice(currentCurrency[cmd]);
        mapb.setClosePrice(stopProfitAdv.getnumber());
        double profit = CalMarginAndProfitUtil.getProfit(mapb);

        String explain;
        if (cmd == 0)
            explain = "价格>" + BaseUtils.getDigitsData(stopProfitLimit, parentActivity.digits) + "  预计盈亏：" + BaseUtils.dealSymbol(profit);
        else
            explain = "价格<" + BaseUtils.getDigitsData(stopProfitLimit, parentActivity.digits) + "  预计盈亏：" + BaseUtils.dealSymbol(profit);
        stopProfitAdv.setTvExplain(explain);
    }

    /**
     * 计算保证金
     */
    private void setMargin() {
        mapb.setSymbolInfoBean(symbolInfoBean);
        mapb.setCurrentCurrency(currentCurrency);
        mapb.setMarginCalCurrency(marginCalCurrency);
        mapb.setLots(lots);
        mapb.setCmd(cmd);
        double margin = CalMarginAndProfitUtil.getMargin(mapb);
        tradeTimesAdv.setTvExplain("已用保证金：$ " + margin);
    }


}