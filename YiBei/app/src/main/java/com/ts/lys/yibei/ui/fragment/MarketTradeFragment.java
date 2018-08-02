package com.ts.lys.yibei.ui.fragment;

import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.EventBean;
import com.ts.lys.yibei.bean.MarginAndProfitBean;
import com.ts.lys.yibei.bean.OpenTrader;
import com.ts.lys.yibei.bean.PendingOrder;
import com.ts.lys.yibei.bean.RealTimeBean;
import com.ts.lys.yibei.bean.RealTimeQuoteDatas;
import com.ts.lys.yibei.bean.SymbolInfo;
import com.ts.lys.yibei.constant.EventContents;
import com.ts.lys.yibei.customeview.AddDeleteView;
import com.ts.lys.yibei.customeview.CustomPopWindow;
import com.ts.lys.yibei.customeview.KeyboardLayout;
import com.ts.lys.yibei.mvppresenter.TradePresenter;
import com.ts.lys.yibei.mvpview.ITradeOrPendingView;
import com.ts.lys.yibei.ui.activity.QuotationsActivity;
import com.ts.lys.yibei.utils.Arith;
import com.ts.lys.yibei.utils.BaseUtils;
import com.ts.lys.yibei.utils.ButtonUtils;
import com.ts.lys.yibei.utils.CalMarginAndProfitUtil;
import com.ts.lys.yibei.utils.Logger;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jcdev1 on 2018/6/21.
 */

public class MarketTradeFragment extends BaseFragment implements ITradeOrPendingView {
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
    /**
     * 是否通过加减和输入设置过值了
     */
    private boolean isSettedValue = false;

    private TradePresenter tradePresenter = new TradePresenter(this);


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
        tradePresenter.attachView(this);
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
        stopLossAdv.setLimit(-1, 10000000, parentActivity.digits);
        stopLossAdv.setIsStopLossOrProfit(true);

        /**
         *止盈价格
         */
        stopProfitAdv.setScrollView(parentActivity.scrollView, parentActivity.llScrollContent, baseHeight);
        stopProfitAdv.setLimit(-1, 10000000, parentActivity.digits);
        stopProfitAdv.setIsStopLossOrProfit(true);
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
                if (stopLossAdv.getnumber() != 0) {
                    double getnumber = stopLossAdv.getnumber();
                    getnumber += stopLossLever;
                    stopLossAdv.setnumber(getnumber);
                    initstopLossLimit();
                } else {
                    double stopLossLimit = CalMarginAndProfitUtil.stopLossOrprofitLimit(currentCurrency, symbolInfoBean.getStopsLevel(), parentActivity.digits, cmd, 0);
                    if (cmd == 0)
                        stopLossAdv.setLimit(0, stopLossLimit, parentActivity.digits);
                    else
                        stopLossAdv.setLimit(stopLossLimit, 10000000, parentActivity.digits);
                    stopLossAdv.setnumber(stopLossLimit);
                }

            }

            @Override
            public void onDelClick() {
                if (stopLossAdv.getnumber() != 0) {
                    double getnumber = stopLossAdv.getnumber();
                    getnumber -= stopLossLever;
                    stopLossAdv.setnumber(getnumber);
                    initstopLossLimit();
                } else {
                    double stopLossLimit = CalMarginAndProfitUtil.stopLossOrprofitLimit(currentCurrency, symbolInfoBean.getStopsLevel(), parentActivity.digits, cmd, 0);
                    if (cmd == 0)
                        stopLossAdv.setLimit(0, stopLossLimit, parentActivity.digits);
                    else
                        stopLossAdv.setLimit(stopLossLimit, 10000000, parentActivity.digits);
                    stopLossAdv.setnumber(stopLossLimit);
                }

            }

            @Override
            public void onEditText(double lots) {
                if (stopLossAdv.getnumber() != 0) {
                    initstopLossLimit();
                }
            }
        });
        /**
         * 止盈价格
         */
        stopProfitAdv.setOnAddDelClickLstener(new AddDeleteView.OnAddDelClickLstener() {
            @Override
            public void onAddClick() {
                if (stopProfitAdv.getnumber() != 0) {
                    double getnumber = stopProfitAdv.getnumber();
                    getnumber += stopProfitLever;
                    stopProfitAdv.setnumber(getnumber);
                    initStopProfitLimit();
                } else {
                    double stopProfitLimit = CalMarginAndProfitUtil.stopLossOrprofitLimit(currentCurrency, symbolInfoBean.getStopsLevel(), parentActivity.digits, cmd, 1);
                    if (cmd == 0)
                        stopProfitAdv.setLimit(stopProfitLimit, 10000000, parentActivity.digits);
                    else
                        stopProfitAdv.setLimit(0, stopProfitLimit, parentActivity.digits);
                    stopProfitAdv.setnumber(stopProfitLimit);
                }
            }

            @Override
            public void onDelClick() {
                Logger.e("number", stopProfitAdv.getnumber() + "");
                if (stopProfitAdv.getnumber() != 0) {
                    double getnumber = stopProfitAdv.getnumber();
                    getnumber -= stopProfitLever;
                    stopProfitAdv.setnumber(getnumber);
                    initStopProfitLimit();
                } else {
                    double stopProfitLimit = CalMarginAndProfitUtil.stopLossOrprofitLimit(currentCurrency, symbolInfoBean.getStopsLevel(), parentActivity.digits, cmd, 1);
                    if (cmd == 0)
                        stopProfitAdv.setLimit(stopProfitLimit, 10000000, parentActivity.digits);
                    else
                        stopProfitAdv.setLimit(0, stopProfitLimit, parentActivity.digits);
                    stopProfitAdv.setnumber(stopProfitLimit);
                }
            }

            @Override
            public void onEditText(double lots) {
                if (stopProfitAdv.getnumber() != 0)
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
        if (ButtonUtils.isFastDoubleClick(view.getId(), 1500)) return;
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
                llSellPrice.setBackgroundResource(R.drawable.fall_bg);
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
            tradeTimesAdv.setLimit(sb.getMinVolume(), 10000, 0);//TODO 第二个参数待定
        else
            tradeTimesAdv.setLimit(sb.getMinVolume(), 10000, 2);//TODO 第二个参数待定
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
        if (cmd == 0) {
            stopLossAdv.setLimit(0, stopLossLimit, parentActivity.digits);
            Logger.e("买入", stopLossLimit + "");
        } else {
            Logger.e("卖出", stopLossLimit + "");
            stopLossAdv.setLimit(stopLossLimit, 10000000, parentActivity.digits);
        }

        /**
         * 如果加减后的价格不在范围内则强制改变输入框的价格（注意：是点击加号和减号的处理逻辑）
         *
         * 如果软键盘出现，即手动输入时不允许强制修改输入框里的内容
         */
        if (!isShowKeybord)
            switch (cmd) {
                case 0:
                    if (stopLossAdv.getnumber() > stopLossLimit) {
                        if (stopLossAdv.getnumber() == 0) break;
                        stopLossAdv.setnumber(stopLossLimit);
                    }
                    break;
                case 1:
                    if (stopLossAdv.getnumber() < stopLossLimit) {
                        if (stopLossAdv.getnumber() == 0) break;
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
            explain = getString(R.string.price) + "<" + BaseUtils.getDigitsData(stopLossLimit, parentActivity.digits) + "  " + getString(R.string.expected_loss) + "：" + (stopLossAdv.getnumber() == 0 ? "$0" : BaseUtils.dealSymbol(profit));
        else
            explain = getString(R.string.price) + ">" + BaseUtils.getDigitsData(stopLossLimit, parentActivity.digits) + "  " + getString(R.string.expected_loss) + "：" + (stopLossAdv.getnumber() == 0 ? "$0" : BaseUtils.dealSymbol(profit));
        stopLossAdv.setTvExplain(explain);
    }

    /**
     * 初始化止盈列表数据展示
     */
    private void initStopProfitLimit() {

        double stopProfitLimit = CalMarginAndProfitUtil.stopLossOrprofitLimit(currentCurrency, symbolInfoBean.getStopsLevel(), parentActivity.digits, cmd, 1);
        if (cmd == 0)
            stopProfitAdv.setLimit(stopProfitLimit, 10000000, parentActivity.digits);
        else
            stopProfitAdv.setLimit(0, stopProfitLimit, parentActivity.digits);

        /**
         * 如果加减后的价格不在范围内则强制改变输入框的价格（注意：是点击加号和减号的处理逻辑）
         *
         * 如果软键盘出现，即手动输入时不允许强制修改输入框里的内容
         */
        if (!isShowKeybord)
            switch (cmd) {
                case 0:
                    if (stopProfitAdv.getnumber() < stopProfitLimit) {
                        if (stopProfitAdv.getnumber() == 0) break;
                        stopProfitAdv.setnumber(stopProfitLimit);
                    }
                    break;
                case 1:
                    if (stopProfitAdv.getnumber() > stopProfitLimit) {
                        if (stopProfitAdv.getnumber() == 0) break;
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
            explain = getString(R.string.price) + ">" + BaseUtils.getDigitsData(stopProfitLimit, parentActivity.digits) + "  " + getString(R.string.expected_profit) + "：" + (stopProfitAdv.getnumber() == 0 ? "$0" : BaseUtils.dealSymbol(profit));
        else
            explain = getString(R.string.price) + "<" + BaseUtils.getDigitsData(stopProfitLimit, parentActivity.digits) + "  " + getString(R.string.expected_profit) + "：" + (stopProfitAdv.getnumber() == 0 ? "$0" : BaseUtils.dealSymbol(profit));
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
        tradeTimesAdv.setTvExplain(getString(R.string.about_use_margin) + "：$ " + margin);
    }

    /**
     * 复杂-市价-买入
     */
    public void complexBuyIn() {
        showTradePop(getParameter(0), 0);

    }

    /**
     * 复杂-市价-卖出
     */
    public void complexSellOut() {
        showTradePop(getParameter(1), 1);

    }


    @NonNull
    private String getParameter(int cmd) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getString(R.string.remind_two));
        buffer.append(cmd == 0 ? getString(R.string.purchase) : getString(R.string.sell_out));
        buffer.append(lots);
        buffer.append(getString(R.string.lots));
        buffer.append(" ");
        buffer.append(parentActivity.symbol);
        buffer.append(" ");
        buffer.append(getString(R.string.ma));
        return buffer.toString();
    }

    private void showTradePop(String title, final int cmd) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_trade_remind_layout, null);
        TextView tvTitle = contentView.findViewById(R.id.tv_title);
        TextView tvContent = contentView.findViewById(R.id.tv_content);
        TextView tvCancle = contentView.findViewById(R.id.tv_cancle);
        TextView tvConfirm = contentView.findViewById(R.id.tv_confirm);
        LinearLayout llStopLossProfit = contentView.findViewById(R.id.ll_stop_loss_profit);
        TextView tvStopLossPri = contentView.findViewById(R.id.tv_stop_loss_pri);
        TextView tvStopProfitPri = contentView.findViewById(R.id.tv_stop_profit_pri);
        if (stopProfitAdv.getnumber() == 0 && stopLossAdv.getnumber() == 0) {
            llStopLossProfit.setVisibility(View.GONE);
            tvContent.setVisibility(View.VISIBLE);
        } else {
            llStopLossProfit.setVisibility(View.VISIBLE);
            tvContent.setVisibility(View.GONE);
            tvStopLossPri.setText(stopLossAdv.getnumber() == 0 ? getString(R.string.not_setting) : BaseUtils.getDigitsData(stopLossAdv.getnumber(), parentActivity.digits));
            tvStopProfitPri.setText(stopProfitAdv.getnumber() == 0 ? getString(R.string.not_setting) : BaseUtils.getDigitsData(stopProfitAdv.getnumber(), parentActivity.digits));
        }
        final CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
                .setView(contentView)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(0.7f)
                .create()
                .showAtLocation(parentActivity.keyboardLayout, Gravity.CENTER, 0, 0);
        tvTitle.setText(title);
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPopWindow.dissmiss();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPopWindow.dissmiss();
                //TODO 开仓
                Map<String, String> map = new HashMap<>();
                map.put("accessToken", accessToken);
                map.put("symbol", parentActivity.symbol);
                map.put("userId", userId);
                map.put("cmd", cmd + "");
                map.put("volume", lots + "");
                map.put("tp", stopProfitAdv.getnumber() == 0 ? "" : String.valueOf(stopProfitAdv.getnumber()));
                map.put("sl", stopLossAdv.getnumber() == 0 ? "" : String.valueOf(stopLossAdv.getnumber()));
                tradePresenter.openPosition(map, className + "1");
            }
        });

    }

    @Override
    public void setTradeBackInfo(OpenTrader openTrader) {

        OpenTrader.DataBean.OpenTraderBean otb = openTrader.getData().getOpenTrader();
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_trade_back_info_layout, null);
        TextView tvSymbol = contentView.findViewById(R.id.tv_symbol);
        TextView tvDirection = contentView.findViewById(R.id.tv_direction);
        TextView tvOpenPri = contentView.findViewById(R.id.tv_open_price);
        TextView tvTradeTimes = contentView.findViewById(R.id.tv_trade_times);
        TextView tvStopLossPri = contentView.findViewById(R.id.tv_stop_loss_pri);
        TextView tvStopProfitPri = contentView.findViewById(R.id.tv_stop_profit_pri);
        TextView tvUserMargin = contentView.findViewById(R.id.tv_user_margin);
        TextView tvCancle = contentView.findViewById(R.id.tv_cancle);
        TextView tvSeeOrder = contentView.findViewById(R.id.tv_see_order);

        final CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
                .setView(contentView)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(0.7f)
                .create()
                .showAtLocation(parentActivity.keyboardLayout, Gravity.CENTER, 0, 0);

        tvSymbol.setText(otb.getSymbol());
        tvDirection.setText(otb.getCmd() == 0 ? getString(R.string.purchase) : getString(R.string.sell_out));
        tvOpenPri.setText(BaseUtils.getDigitsData(otb.getOpenPrice(), parentActivity.digits));
        tvTradeTimes.setText(BaseUtils.getDigitsData(otb.getVolume(), 2));
        tvStopLossPri.setText(otb.getSl() == 0 ? getString(R.string.not_setting) : BaseUtils.getDigitsData(otb.getSl(), parentActivity.digits));
        tvStopProfitPri.setText(otb.getTp() == 0 ? getString(R.string.not_setting) : BaseUtils.getDigitsData(otb.getTp(), parentActivity.digits));
        tvUserMargin.setText("$" + BaseUtils.getDigitsData(otb.getMargin(), 2));

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPopWindow.dissmiss();
            }
        });
        tvSeeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPopWindow.dissmiss();
                showToast("TODO：查看订单记录");
            }
        });
    }

    @Override
    public void setPendingBackInfo(PendingOrder pendingOrder) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tradePresenter.detachView();
    }
}
