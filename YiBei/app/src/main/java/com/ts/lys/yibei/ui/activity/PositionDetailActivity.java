package com.ts.lys.yibei.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.CloseTraderModel;
import com.ts.lys.yibei.bean.EventBean;
import com.ts.lys.yibei.bean.MarginAndProfitBean;
import com.ts.lys.yibei.bean.OrderPositionModel;
import com.ts.lys.yibei.bean.RealTimeBean;
import com.ts.lys.yibei.bean.RealTimeQuoteDatas;
import com.ts.lys.yibei.bean.SymbolInfo;
import com.ts.lys.yibei.bean.SymbolPrice;
import com.ts.lys.yibei.constant.EventContents;
import com.ts.lys.yibei.customeview.CustomPopWindow;
import com.ts.lys.yibei.mvppresenter.PositionListPresenter;
import com.ts.lys.yibei.mvppresenter.QuotationPresenter;
import com.ts.lys.yibei.mvppresenter.RealTimeDataPresenter;
import com.ts.lys.yibei.mvpview.IPositionFragmentView;
import com.ts.lys.yibei.mvpview.IQuotationView;
import com.ts.lys.yibei.mvpview.IRealTimeView;
import com.ts.lys.yibei.ui.fragment.ChangeStopLossOrProfitFragment;
import com.ts.lys.yibei.ui.fragment.KLineFragment;
import com.ts.lys.yibei.utils.Arith;
import com.ts.lys.yibei.utils.BaseUtils;
import com.ts.lys.yibei.utils.ButtonUtils;
import com.ts.lys.yibei.utils.CalMarginAndProfitUtil;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.ts.lys.yibei.utils.HiddenAnimUtils;
import com.ts.lys.yibei.utils.Logger;
import com.zhy.autolayout.AutoFrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jcdev1 on 2018/7/12.
 */

public class PositionDetailActivity extends BaseFragmentActivity implements IQuotationView, IRealTimeView, IPositionFragmentView {

    @Bind(R.id.frame_layout_k_line)
    AutoFrameLayout frameLayoutKLine;
    @Bind(R.id.tv_float_profit)
    TextView tvFloatProfit;
    @Bind(R.id.iv_arrow)
    ImageView ivArrow;
    @Bind(R.id.tv_profit_and_loss)
    TextView tvProfitAndLoss;
    @Bind(R.id.tv_inventory_fee)
    TextView tvInventoryFee;
    @Bind(R.id.tv_handling_fee)
    TextView tvHandlingFee;
    @Bind(R.id.tv_order_num)
    TextView tvOrderNum;
    @Bind(R.id.tv_open_price)
    TextView tvOpenPrice;
    @Bind(R.id.tv_current_price)
    TextView tvCurrentPrice;
    @Bind(R.id.tv_trade_lots)
    TextView tvTradeLots;
    @Bind(R.id.tv_margin)
    TextView tvMargin;
    @Bind(R.id.tv_stop_loss_pri)
    TextView tvStopLossPri;
    @Bind(R.id.tv_stop_profit_pri)
    TextView tvStopProfitPri;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.ll_more)
    LinearLayout llMore;
    @Bind(R.id.rl_father)
    RelativeLayout rlFather;

    private KLineFragment kLineFragment;
    private QuotationPresenter quotationPresenter = new QuotationPresenter(this);
    private RealTimeDataPresenter realTimeDataPresenter = new RealTimeDataPresenter(this);
    private PositionListPresenter presenter = new PositionListPresenter(this);


    public String symbol;//只用于参数调用
    private String symbolCN;//只用于展示
    private String symbolEN;//只用于展示
    /**
     * 价格小数点后位数
     */
    public int digits = 2;
    /**
     * 品种保证金及止盈止损数据接口
     */
    public SymbolInfo.DataBean.SymbolInfoBean sbinfo;
    /**
     * 出现交叉盘时计算预计收益需要使用的货币对
     */
    public String profitCalCurrency;
    /**
     * 第一次进入时请求一次所需产品的实时价格做缓存：因为部分品种数据波动小导致没有价格推送过来
     */
    public List<RealTimeQuoteDatas.DataBean.QuoteBean> quote;
    /**
     * 当前货币的ask，bid值
     */
    public double[] currentCurrencyArray = new double[]{0, 0};
    /**
     * 计算预计收益时使用：提供转换货币的ask，bid值
     */
    public double[] profitCalCurrencyArray = new double[]{0, 0};
    /**
     * 当前产品基本数据
     */
    public OrderPositionModel.DataBean.TraderOrderBean tb;
    private MarginAndProfitBean mapb = new MarginAndProfitBean();

    private ChangeStopLossOrProfitFragment changeStopLossOrProfitFragment;
    /**
     * 止损步长
     */
    public double stopLossLever = 0.00001;

    /**
     * 止盈步长
     */
    public double stopProfitLever = 0.00001;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_detail);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }


    private void initView() {

        symbol = getIntent().getStringExtra("symbol");
        symbolEN = getIntent().getStringExtra("symbol");
        symbolCN = getIntent().getStringExtra("symbolCn");
        digits = getIntent().getIntExtra("digits", 2);

        setDataToUI();

        setBackButton();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        kLineFragment = new KLineFragment();
        Bundle bundle = new Bundle();
        bundle.putString("symbol", symbol);
        bundle.putInt("digits", digits);
        kLineFragment.setArguments(bundle);
        transaction.add(R.id.frame_layout_k_line, kLineFragment);
        transaction.commit();
    }

    private void setDataToUI() {
        Bundle extras = getIntent().getExtras();
        tb = (OrderPositionModel.DataBean.TraderOrderBean) extras.getSerializable("bean");
        if (tb != null) {
            double fee = Arith.add(tb.getSwaps(), tb.getCommission());
            double realProfit = Arith.add(tb.getProfit(), fee);
            tvFloatProfit.setText(BaseUtils.dealSymbol(realProfit));

            if (tb.getProfit() < 0)
                tvFloatProfit.setTextColor(getResources().getColor(R.color.fall_color));
            else
                tvFloatProfit.setTextColor(getResources().getColor(R.color.rise_color));

            tvProfitAndLoss.setText(BaseUtils.dealSymbol(tb.getProfit()));
            tvInventoryFee.setText(BaseUtils.dealSymbol(tb.getSwaps()));
            tvHandlingFee.setText(BaseUtils.dealSymbol(tb.getCommission()));
            tvOrderNum.setText(tb.getTicket());
            tvOpenPrice.setText(BaseUtils.getDigitsData(tb.getOpenPrice(), tb.getDigits()));
            tvCurrentPrice.setText(BaseUtils.getDigitsData(tb.getMarket(), tb.getDigits()));
            if (tb.getSl() == 0)
                tvStopLossPri.setText(getString(R.string.not_setting));
            else
                tvStopLossPri.setText(BaseUtils.getDigitsData(tb.getSl(), tb.getDigits()));
            if (tb.getTp() == 0)
                tvStopProfitPri.setText(getString(R.string.not_setting));
            else
                tvStopProfitPri.setText(BaseUtils.getDigitsData(tb.getTp(), tb.getDigits()));

            tvTradeLots.setText(String.valueOf(tb.getVolume()));
            tvMargin.setText(String.valueOf(tb.getMargin()));
            tvDate.setText(getString(R.string.position_time) + "：" + tb.getOpenTime());

        }

    }


    private void initData() {
        quotationPresenter.attachView(this);
        realTimeDataPresenter.attachView(this);
        presenter.attachView(this);


        setTitle(symbolEN + " " + symbolCN);

        Map<String, String> map1 = new HashMap<>();
        map1.put("accessToken", accessToken);
        map1.put("userId", userId);
        map1.put("symbol", symbol);
        quotationPresenter.getSymbolCalInfo(map1, className + "1");
    }

    @OnClick({R.id.ll_float_profit, R.id.btn_stop_loss_profit, R.id.btn_position})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_float_profit:
                if (ButtonUtils.isFastDoubleClick(R.id.ll_float_profit, 1500)) return;
                llMore.measure(0, 0);
                HiddenAnimUtils.newInstance(getApplicationContext(), llMore, ivArrow, llMore.getMeasuredHeight()).toggle();
                break;
            case R.id.btn_stop_loss_profit:
                if (ButtonUtils.isFastDoubleClick(R.id.btn_stop_loss_profit, 1500)) return;
                if (tb != null && sbinfo != null) {
                    changeStopLossOrProfitFragment = new ChangeStopLossOrProfitFragment();
                    changeStopLossOrProfitFragment.show(getSupportFragmentManager(), "change");
                } else
                    showToast(getString(R.string.can_not_deal));

                break;
            case R.id.btn_position:
                if (ButtonUtils.isFastDoubleClick(R.id.btn_position, 1500)) return;
                showClosePositionPop(tb);
                break;
        }
    }

    /**
     * 展示平仓确定弹框
     *
     * @param tb
     */
    private void showClosePositionPop(final OrderPositionModel.DataBean.TraderOrderBean tb) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_close_position_remind_layout, null);
        TextView tv_order_num = contentView.findViewById(R.id.tv_order_num);
        TextView tv_expected_profit_loss = contentView.findViewById(R.id.tv_expected_profit_loss);
        TextView tv_open_position_pri = contentView.findViewById(R.id.tv_open_position_pri);
        TextView tv_current_price = contentView.findViewById(R.id.tv_current_price);
        TextView tv_cancle = contentView.findViewById(R.id.tv_cancle);
        TextView tv_confirm = contentView.findViewById(R.id.tv_confirm);
        final CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(0.7f)
                .create()
                .showAtLocation(rlFather, Gravity.CENTER, 0, 0);

        tv_order_num.setText(tb.getTicket());
        tv_expected_profit_loss.setText(BaseUtils.dealSymbol(tb.getProfit()));
        if (tb.getProfit() < 0)
            tv_expected_profit_loss.setTextColor(getResources().getColor(R.color.fall_color));
        else
            tv_expected_profit_loss.setTextColor(getResources().getColor(R.color.rise_color));

        tv_open_position_pri.setText(BaseUtils.getDigitsData(tb.getOpenPrice(), tb.getDigits()));
        if (tb.getCmd() == 0)
            tv_current_price.setText(BaseUtils.getDigitsData(tb.getBid(), tb.getDigits()));
        else
            tv_current_price.setText(BaseUtils.getDigitsData(tb.getAsk(), tb.getDigits()));
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPopWindow.dissmiss();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPopWindow.dissmiss();
                Map<String, String> map = new HashMap<>();
                map.put("ticket", tb.getTicket());
                map.put("userId", userId);
                map.put("accessToken", accessToken);
                map.put("symbol", tb.getSymbolEn());
                map.put("volume", String.valueOf(tb.getVolume()));
                map.put("cmd", String.valueOf(tb.getCmd()));
                presenter.getClosePositionBackInfo(map, className + "3");

            }
        });
    }


    /**
     * 实时数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean event) {
        if (event.getTagOne().equals(EventContents.REAL_TIME_DATA)) {
            String json = (String) event.getResponse();
            RealTimeBean realTimeBeanAll = new Gson().fromJson(json, RealTimeBean.class);
            if (realTimeBeanAll == null) return;
            String realSymbol = realTimeBeanAll.getSymbol();
            if (realSymbol.equals(symbol) || realSymbol.equals(profitCalCurrency))
                dealRealTimeData(realTimeBeanAll);
        }
    }

    private void dealRealTimeData(RealTimeBean realTimeBeanAll) {

        if (realTimeBeanAll.getSymbol().equals(symbol)) {

            if (kLineFragment != null)
                kLineFragment.setRealPrice(realTimeBeanAll);
            if (realTimeBeanAll.getSymbol().equals(symbol)) {
                currentCurrencyArray[0] = realTimeBeanAll.getAsk();
                currentCurrencyArray[1] = realTimeBeanAll.getBid();
            }

            if (realTimeBeanAll.getSymbol().equals(profitCalCurrency)) {
                profitCalCurrencyArray[0] = realTimeBeanAll.getAsk();
                profitCalCurrencyArray[1] = realTimeBeanAll.getBid();
            }

            if (changeStopLossOrProfitFragment != null) {
                changeStopLossOrProfitFragment.setRealPrice(realTimeBeanAll);
                Logger.e("changeStopLossOrProfitFragment", "not null");
            } else {
                Logger.e("changeStopLossOrProfitFragment", "null");
            }

            //计算盈亏
            calFloatProfit();
        }
    }

    /**
     * 计算浮动盈亏
     */
    private void calFloatProfit() {
        if (sbinfo != null) {
            mapb.setSymbolInfoBean(sbinfo);
            mapb.setLots(tb.getVolume());
            mapb.setCmd(tb.getCmd());
            mapb.setProfitCalCurrency(profitCalCurrencyArray);
            mapb.setOpenPrice(tb.getOpenPrice());
            if (tb.getCmd() == 0)
                mapb.setClosePrice(currentCurrencyArray[1]);
            else
                mapb.setClosePrice(currentCurrencyArray[0]);

            double profit = CalMarginAndProfitUtil.getProfit(mapb);

            tvProfitAndLoss.setText(BaseUtils.dealSymbol(profit));
            double fee = Arith.add(tb.getSwaps(), tb.getCommission());
            double realProfit = Arith.add(profit, fee);
            tvFloatProfit.setText(BaseUtils.dealSymbol(realProfit));
        }
    }


    @Override
    public void showRealTimeData(RealTimeQuoteDatas realTimeQuoteDatas) {
        quote = realTimeQuoteDatas.getData().getQuote();
        if (quote != null && quote.size() != 0) {
            for (int i = 0; i < quote.size(); i++) {
                if (quote.get(i).getSymbol().equals(profitCalCurrency)) {
                    profitCalCurrencyArray[0] = quote.get(i).getAsk();
                    profitCalCurrencyArray[1] = quote.get(i).getBid();
                }

                if (quote.get(i).getSymbol().equals(symbol)) {
                    currentCurrencyArray[0] = quote.get(i).getAsk();
                    currentCurrencyArray[1] = quote.get(i).getBid();
                }

            }
        }
        //计算盈亏
        calFloatProfit();
    }

    @Override
    public void setSymbolPriceData(SymbolPrice symbolPrice) {

    }

    @Override
    public void setSymbolCalInfo(SymbolInfo symbolInfo) {
        sbinfo = symbolInfo.getData().getSymbolInfo();
        String symbolMulti = symbol;
        profitCalCurrency = sbinfo.getProfitCalCurrency();

        symbolMulti += "," + profitCalCurrency;
        getSymbolNewPrice(symbolMulti);
        //************************止盈/止损步长***********************************//
        stopLossLever = Arith.div(1, Math.pow(10, digits));
        stopProfitLever = Arith.div(1, Math.pow(10, digits));
    }

    /**
     * 获取一个或多个symbol最新价格
     */
    private void getSymbolNewPrice(String symbolMulti) {
        Map<String, String> map = new HashMap<>();
        map.put("symbol", symbolMulti);
        Logger.e("symbol", symbolMulti);
        realTimeDataPresenter.getRealTimeQuotoDatas(map, className + "2");
    }

    /**
     * 修改止盈止损
     *
     * @param sl
     * @param tp
     */
    public void changeStopLossOrProfit(double sl, double tp) {

        tb.setSl(sl);
        tb.setTp(tp);

        if (sl == 0)
            tvStopLossPri.setText(getString(R.string.not_setting));
        else
            tvStopLossPri.setText(BaseUtils.getDigitsData(sl, digits));
        if (tp == 0)
            tvStopProfitPri.setText(getString(R.string.not_setting));
        else
            tvStopProfitPri.setText(BaseUtils.getDigitsData(tp, digits));

        //通知持仓列表页刷新
        EventBus.getDefault().post(new EventBean(EventContents.NEW_TRADING, null));


    }


    /**
     * 此处无用
     *
     * @param traderOrderBeanList
     */
    @Override
    public void setPositionList(List<OrderPositionModel.DataBean.TraderOrderBean> traderOrderBeanList) {

    }

    /**
     * 平仓返回结果
     *
     * @param closeTraderBean
     */
    @Override
    public void setClosePositionBackInfo(CloseTraderModel.DataBean.CloseTraderBean closeTraderBean) {
        //通知持仓列表页刷新
        EventBus.getDefault().post(new EventBean(EventContents.NEW_TRADING, null));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        quotationPresenter.detachView();
        realTimeDataPresenter.detachView();
        presenter.detachView();
        CustomHttpUtils.cancelHttp(className + "1");
        CustomHttpUtils.cancelHttp(className + "2");
        CustomHttpUtils.cancelHttp(className + "3");

    }


}
