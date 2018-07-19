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
import com.ts.lys.yibei.bean.EventBean;
import com.ts.lys.yibei.bean.RealTimeBean;
import com.ts.lys.yibei.bean.RealTimeQuoteDatas;
import com.ts.lys.yibei.bean.SymbolInfo;
import com.ts.lys.yibei.bean.SymbolPrice;
import com.ts.lys.yibei.constant.EventContents;
import com.ts.lys.yibei.customeview.CustomPopWindow;
import com.ts.lys.yibei.mvppresenter.QuotationPresenter;
import com.ts.lys.yibei.mvppresenter.RealTimeDataPresenter;
import com.ts.lys.yibei.mvpview.IQuotationView;
import com.ts.lys.yibei.mvpview.IRealTimeView;
import com.ts.lys.yibei.ui.fragment.ChangeStopLossOrProfitFragment;
import com.ts.lys.yibei.ui.fragment.KLineFragment;
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

public class PositionDetailActivity extends BaseFragmentActivity implements IQuotationView, IRealTimeView {

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
    private double[] currentCurrencyArray = new double[]{0, 0};
    /**
     * 计算预计收益时使用：提供转换货币的ask，bid值
     */
    private double[] profitCalCurrencyArray = new double[]{0, 0};

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


    private void initData() {
        quotationPresenter.attachView(this);
        realTimeDataPresenter.attachView(this);

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
                llMore.measure(0, 0);
                HiddenAnimUtils.newInstance(getApplicationContext(), llMore, ivArrow, llMore.getMeasuredHeight()).toggle();
                break;
            case R.id.btn_stop_loss_profit:
                ChangeStopLossOrProfitFragment changeStopLossOrProfitFragment = new ChangeStopLossOrProfitFragment();
                changeStopLossOrProfitFragment.show(getSupportFragmentManager(), "change");
                break;
            case R.id.btn_position:
                showPositionPop();
                break;
        }
    }

    /**
     * 平仓确认
     */
    private void showPositionPop() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_close_position_remind_layout, null);

        CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(0.7f)
                .create()
                .showAtLocation(rlFather, Gravity.CENTER, 0, 0);
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

            //TODO 计算盈亏

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
        //TODO 计算盈亏

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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        quotationPresenter.detachView();
        realTimeDataPresenter.detachView();
        CustomHttpUtils.cancelHttp(className + "1");
        CustomHttpUtils.cancelHttp(className + "2");

    }
}
