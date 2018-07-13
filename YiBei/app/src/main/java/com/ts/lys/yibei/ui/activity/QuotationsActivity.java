package com.ts.lys.yibei.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jaeger.library.StatusBarUtil;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.EventBean;
import com.ts.lys.yibei.bean.RealTimeBean;
import com.ts.lys.yibei.bean.RealTimeQuoteDatas;
import com.ts.lys.yibei.bean.SymbolInfo;
import com.ts.lys.yibei.bean.SymbolPrice;
import com.ts.lys.yibei.constant.EventContents;
import com.ts.lys.yibei.constant.UrlContents;
import com.ts.lys.yibei.customeview.KeyboardLayout;
import com.ts.lys.yibei.mvppresenter.QuotationPresenter;
import com.ts.lys.yibei.mvppresenter.RealTimeDataPresenter;
import com.ts.lys.yibei.mvpview.IQuotationView;
import com.ts.lys.yibei.mvpview.IRealTimeView;
import com.ts.lys.yibei.ui.fragment.ComplexTradeFragment;
import com.ts.lys.yibei.ui.fragment.KLineFragment;
import com.ts.lys.yibei.ui.fragment.SimpleTradeFragment;
import com.ts.lys.yibei.utils.Arith;
import com.ts.lys.yibei.utils.BaseUtils;
import com.ts.lys.yibei.utils.ButtonUtils;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.ts.lys.yibei.utils.Logger;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by jcdev1 on 2018/6/19.
 */

public class QuotationsActivity extends BaseFragmentActivity implements IQuotationView, IRealTimeView {

    @Bind(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @Bind(R.id.frame_layout_k_line)
    FrameLayout frameLayoutKLine;
    @Bind(R.id.frame_layout_trade)
    FrameLayout frameLayoutTrade;
    @Bind(R.id.scroll_view)
    public ScrollView scrollView;
    @Bind(R.id.keyboard_layout)
    public KeyboardLayout keyboardLayout;
    @Bind(R.id.ll_scroll_content)
    public LinearLayout llScrollContent;


    @Bind(R.id.rl_simple)
    RelativeLayout rlSimple;
    @Bind(R.id.ll_complex)
    LinearLayout llComplex;
    @Bind(R.id.btn_buy_or_sell)
    TextView btnBuyOrSell;
    @Bind(R.id.tv_buy_in)
    TextView tvBuyIn;
    @Bind(R.id.tv_sell_out)
    TextView tvSellOut;
    @Bind(R.id.tv_en_name)
    TextView tvEnName;
    @Bind(R.id.tv_ch_name)
    TextView tvChName;
    @Bind(R.id.tv_mark_price)
    TextView tvMarkPrice;
    @Bind(R.id.tv_price_var)
    TextView tvPriceVar;
    @Bind(R.id.tv_rate_of_change)
    TextView tvRateOfChange;
    @Bind(R.id.iv_collection)
    ImageView ivCollection;
    @Bind(R.id.tv_spread)
    TextView tvSpread;
    @Bind(R.id.ll_head)
    RelativeLayout llHead;

    private String[] CHANNELS;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private FragmentContainerHelper mFragmentContainerHelper = new FragmentContainerHelper();
    private KLineFragment kLineFragment;
    private SimpleTradeFragment simpleTradeFragment;
    private ComplexTradeFragment complexTradeFragment;

    public String symbol;//只用于参数调用
    private String symbolCN;//只用于展示
    private String symbolEN;//只用于展示

    public String marginCalCurrency;//出现交叉盘时计算保证金需使用的货币对
    public String profitCalCurrency;//出现交叉盘时计算预计收益需要使用的货币对

    private QuotationPresenter quotationPresenter;
    private RealTimeDataPresenter realTimeDataPresenter;

    /**
     * 所有自选项集合
     */
    private ArrayList<String> symbolList;
    /**
     * 昨日收盘价
     */
    private double yestodayClosePrice = -1;
    /**
     * 价格小数点后位数
     */
    public int digits = 2;

    /**
     * 收藏状态
     */
    private boolean collectionStatus = false;


    /**
     * 0:简单 or 1:高级
     */
    public int simpleOrComplex = 0;
    /**
     * 0:市价交易 or 1:挂单交易
     */
    public int marketOrPendingTrade = 0;
    /**
     * 0:市价买入 or 1:市价卖出
     */
    public int marketDealStatus = 0;
    /**
     * 0:挂单买入 or 1:挂单卖出
     */
    public int pendingDealStatus = 0;

    /**
     * 第一次进入时请求一次所需产品的实时价格做缓存：因为部分品种数据波动小导致没有价格推送过来
     */
    public List<RealTimeQuoteDatas.DataBean.QuoteBean> quote;

    /**
     * 品种保证金及止盈止损数据接口
     */
    public SymbolInfo.DataBean.SymbolInfoBean sbinfo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotations);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        initFragments();
        initMagicIndicator();
        mFragmentContainerHelper.handlePageSelected(0, false);
        switchPages(0);
        setBackButton();
        StatusBarUtil.setColor(this, getResources().getColor(R.color.eight_text_color), 0);

    }


    private void initListener() {

        keyboardLayout.setOnkbdStateListener(new KeyboardLayout.onKybdsChangeListener() {
            @Override
            public void onKeyBoardStateChange(int state) {
                if (simpleTradeFragment != null)
                    simpleTradeFragment.setKeyboardStatus(state);
                if (complexTradeFragment != null)
                    complexTradeFragment.setKeyboardStatus(state);
            }
        });
    }

    private void initData() {
        symbol = getIntent().getStringExtra("symbol");
        symbolEN = getIntent().getStringExtra("symbol");
        symbolCN = getIntent().getStringExtra("symbolCn");
        digits = getIntent().getIntExtra("digits", 2);
        symbolList = getIntent().getStringArrayListExtra("symbolList");
        Logger.e("symbolList", symbolList.size() + "");
        tvChName.setText(symbolCN);
        tvEnName.setText(symbolEN);
        String tags = getIntent().getStringExtra("tag");
        if (tags.equals("自选")) {
            ivCollection.setImageResource(R.mipmap.kline_have_collection);
        } else
            ivCollection.setImageResource(R.mipmap.kline_not_collection);

        realTimeDataPresenter = new RealTimeDataPresenter(this);
        realTimeDataPresenter.attachView(this);

        quotationPresenter = new QuotationPresenter(this);
        quotationPresenter.attachView(this);
        Map<String, String> map1 = new HashMap<>();
        map1.put("accessToken", accessToken);
        map1.put("userId", userId);
        map1.put("symbol", symbol);
        quotationPresenter.getSymbolCalInfo(map1, className + "1");
        Map<String, String> map2 = new HashMap<>();
        map2.put("accessToken", accessToken);
        map2.put("symbol", symbol);
        quotationPresenter.getSymbolPriceData(map2, className + "2");


    }

    private void initFragments() {
        CHANNELS = new String[]{getString(R.string.simple), getString(R.string.senior)};

        simpleTradeFragment = new SimpleTradeFragment();
        complexTradeFragment = new ComplexTradeFragment();
        mFragments.add(simpleTradeFragment);
        mFragments.add(complexTradeFragment);

        kLineFragment = new KLineFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_layout_k_line, kLineFragment);
        transaction.commit();
    }


    /**
     * 初始化指示器
     */
    private void initMagicIndicator() {
        magicIndicator.setBackgroundResource(R.drawable.round_indicator_bg);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return CHANNELS.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(CHANNELS[index]);
                clipPagerTitleView.setTextColor(Color.parseColor("#ffffff"));
                clipPagerTitleView.setClipColor(getResources().getColor(R.color.one_text_color));
                int offX = (int) (BaseUtils.getScreenWidth(context) * 0.1173);
                clipPagerTitleView.setPadding(offX, 0, offX, 0);
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFragmentContainerHelper.handlePageSelected(index);
                        switchPages(index);
                        if (index == 0) {
                            simpleOrComplex = 0;
                            EventBus.getDefault().post(new EventBean(EventContents.BUTTON_STATUS_CHANGE, null));
                        } else {
                            simpleOrComplex = 1;
                            EventBus.getDefault().post(new EventBean(EventContents.BUTTON_STATUS_CHANGE, null));
                        }
                    }
                });
                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
//                float navigatorHeight = context.getResources().getDimension(R.dimen.common_navigator_height);
                float heightPx = (float) (BaseUtils.getScreenHeight(context) * 0.041979);
//                float navigatorHeight = BaseUtils.px2dp(context, heightPx);
                float borderWidth = UIUtil.dip2px(context, 1);
//                float lineHeight = navigatorHeight - 2 * borderWidth;
                indicator.setLineHeight(heightPx);
                indicator.setRoundRadius(BaseUtils.dip2px(context, 4));

                indicator.setYOffset(borderWidth);

                indicator.setColors(Color.parseColor("#ffffff"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        mFragmentContainerHelper.attachMagicIndicator(magicIndicator);
    }

    /**
     * 动态变换交易界面
     *
     * @param index
     */
    private void switchPages(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment;
        for (int i = 0, j = mFragments.size(); i < j; i++) {
            if (i == index) {
                continue;
            }
            fragment = mFragments.get(i);
            if (fragment.isAdded()) {
                fragmentTransaction.hide(fragment);
            }
        }
        fragment = mFragments.get(index);
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.frame_layout_trade, fragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean event) {
        /**
         * 底部按钮状态改变
         */
        if (event.getTagOne().equals(EventContents.BUTTON_STATUS_CHANGE)) {
            if (simpleOrComplex == 0) {
                setBottomStatus(0);
            } else {
                setBottomStatus(1);

                if (marketOrPendingTrade == 0) {
                    if (marketDealStatus == 0)
                        btnBuyOrSell.setText(getString(R.string.cl_buy));
                    else
                        btnBuyOrSell.setText(getString(R.string.cl_sell));
                } else if (marketOrPendingTrade == 1) {

                    if (pendingDealStatus == 0)
                        btnBuyOrSell.setText(getString(R.string.cl_buy));
                    else
                        btnBuyOrSell.setText(getString(R.string.cl_sell));
                }
            }

        } else if (event.getTagOne().equals(EventContents.REAL_TIME_DATA)) {
            String json = (String) event.getResponse();
            RealTimeBean realTimeBeanAll = new Gson().fromJson(json, RealTimeBean.class);
            if (realTimeBeanAll == null) return;
            String realSymbol = realTimeBeanAll.getSymbol();
            if (realSymbol.equals(symbol) || realSymbol.equals(marginCalCurrency) || realSymbol.equals(profitCalCurrency))
                showRealTimeData(realTimeBeanAll);
        }
    }

    /**
     * 控制底部按钮展示与隐藏
     */
    private void setBottomStatus(int tag) {
        rlSimple.setVisibility(View.GONE);
        llComplex.setVisibility(View.GONE);
        switch (tag) {
            case 0:
                rlSimple.setVisibility(View.VISIBLE);
                break;
            case 1:
                llComplex.setVisibility(View.VISIBLE);
                break;
        }

    }


    @OnClick({R.id.tv_buy_in, R.id.tv_sell_out, R.id.btn_buy_or_sell, R.id.iv_collection})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.tv_buy_in:
                if (!ButtonUtils.isFastDoubleClick(R.id.tv_buy_in, 1500)) {
                    if (simpleTradeFragment != null)
                        simpleTradeFragment.buyIn();
                }
                break;
            case R.id.tv_sell_out:
                if (!ButtonUtils.isFastDoubleClick(R.id.tv_sell_out, 1500)) {
                    if (simpleTradeFragment != null)
                        simpleTradeFragment.sellOut();
                }
                break;
            case R.id.btn_buy_or_sell:
                if (!ButtonUtils.isFastDoubleClick(R.id.btn_buy_or_sell, 1500)) {
                    if (marketOrPendingTrade == 0) {
                        if (marketDealStatus == 0) {
                            if (complexTradeFragment != null)
                                complexTradeFragment.complexBuyIn();
                            Logger.e(TAG, "复杂-市价-买入");
                        } else {
                            if (complexTradeFragment != null)
                                complexTradeFragment.complexSellOut();
                            Logger.e(TAG, "复杂-市价-卖出");
                        }
                    } else if (marketOrPendingTrade == 1) {
                        if (pendingDealStatus == 0) {
                            if (complexTradeFragment != null)
                                complexTradeFragment.complexPendingBuyIn();
                            Logger.e(TAG, "复杂-挂单-买入");
                        } else {
                            if (complexTradeFragment != null)
                                complexTradeFragment.complexPendingSellOut();
                            Logger.e(TAG, "复杂-挂单-卖出");
                        }
                    }
                }
                break;
            case R.id.iv_collection:
                if (ButtonUtils.isFastDoubleClick(R.id.iv_collection, 1500)) return;
                if (collectionStatus) {
                    //TODO 取消收藏
                    collectionStatus = false;
                    for (int i = 0; i < symbolList.size(); i++) {
                        if (symbolList.get(i).equals(symbol)) {
                            symbolList.remove(i);
                            break;
                        }
                    }
                    ditySymbol(symbolList);

                } else {
                    //TODO 添加收藏
                    collectionStatus = true;
                    symbolList.add(symbol);
                    ditySymbol(symbolList);
                }
                break;
        }
    }


    /**
     * 处理接收到的实时数据
     *
     * @param realTimeBean
     */
    private void showRealTimeData(RealTimeBean realTimeBean) {

        if (realTimeBean.getSymbol().equals(symbol)) {

            setChangeRate(realTimeBean);
            if (kLineFragment != null)
                kLineFragment.setRealPrice(realTimeBean);
        }

        if (simpleTradeFragment != null)
            simpleTradeFragment.setRealPrice(realTimeBean);

        if (complexTradeFragment != null)
            complexTradeFragment.setRealPrice(realTimeBean);

    }

    /**
     * 实时价格及其变化率
     *
     * @param realTimeBean
     */
    private void setChangeRate(RealTimeBean realTimeBean) {

        tvMarkPrice.setText(BaseUtils.getDigitsData(realTimeBean.getMarket(), digits));
        tvBuyIn.setText(getString(R.string.purchase) + BaseUtils.getDigitsData(realTimeBean.getAsk(), digits));
        tvSellOut.setText(getString(R.string.sell_out) + BaseUtils.getDigitsData(realTimeBean.getBid(), digits));
        tvSpread.setText(Arith.format(realTimeBean.getAsk(), realTimeBean.getBid(), digits));

        if (yestodayClosePrice != -1) {
            double d1 = Arith.sub(realTimeBean.getMarket(), yestodayClosePrice);
            double d2 = Arith.mul(Arith.div(d1, yestodayClosePrice, 5), 100.0);
            if (d1 > 0) {
                tvRateOfChange.setText("+" + BaseUtils.getDigitsData(d2, 2) + "%");
                tvPriceVar.setText("+" + BaseUtils.getDigitsData(d1, digits == -1 ? 2 : digits));
                llHead.setBackgroundColor(getResources().getColor(R.color.rise_color));
                StatusBarUtil.setColor(this, getResources().getColor(R.color.rise_color), 0);
            } else {
                tvRateOfChange.setText(BaseUtils.getDigitsData(d2, 2) + "%");
                tvPriceVar.setText(BaseUtils.getDigitsData(d1, digits == -1 ? 2 : digits));
                llHead.setBackgroundColor(getResources().getColor(R.color.fall_color));
                StatusBarUtil.setColor(this, getResources().getColor(R.color.fall_color), 0);

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        CustomHttpUtils.cancelHttp(className + "1");
        CustomHttpUtils.cancelHttp(className + "2");
        CustomHttpUtils.cancelHttp(className + "3");
        CustomHttpUtils.cancelHttp(className + "4");

        quotationPresenter.detachView();
        realTimeDataPresenter.detachView();

    }

    /**
     * 产品今开，昨收，最高，最低数据
     *
     * @param symbolPrice
     */
    @Override
    public void setSymbolPriceData(SymbolPrice symbolPrice) {
        if (symbolPrice != null)
            yestodayClosePrice = symbolPrice.getData().getPrice().getYesterdayClosePrice();

    }

    /**
     * 品种保证金及止盈止损数据接口
     *
     * @param symbolInfo
     */
    @Override
    public void setSymbolCalInfo(SymbolInfo symbolInfo) {
        sbinfo = symbolInfo.getData().getSymbolInfo();
        String symbolMulti = symbol;
        marginCalCurrency = sbinfo.getMarginCalCurrency();
        profitCalCurrency = sbinfo.getProfitCalCurrency();

        if (simpleTradeFragment != null)
            simpleTradeFragment.setSymbolCalInfo(sbinfo);

        symbolMulti += "," + marginCalCurrency + "," + profitCalCurrency;
        getSymbolNewPrice(symbolMulti);
    }

    /**
     * 获取一个或多个symbol最新价格
     */
    private void getSymbolNewPrice(String symbolMulti) {
        Map<String, String> map = new HashMap<>();
        map.put("symbol", symbolMulti);
        Logger.e("symbol", symbolMulti);
        realTimeDataPresenter.getRealTimeQuotoDatas(map, className + "3");
    }

    /**
     * 第一次进入时请求一次所需产品的实时价格做缓存
     *
     * @param realTimeQuoteDatas
     */
    @Override
    public void showRealTimeData(RealTimeQuoteDatas realTimeQuoteDatas) {
        quote = realTimeQuoteDatas.getData().getQuote();
        for (int i = 0; i < quote.size(); i++) {
            RealTimeQuoteDatas.DataBean.QuoteBean quoteBean = quote.get(i);
            if (quoteBean.getSymbol().equals(symbol)) {
                RealTimeBean realTimeBean = new RealTimeBean();
                realTimeBean.setAsk(quoteBean.getAsk());
                realTimeBean.setBid(quoteBean.getBid());
                realTimeBean.setMarket(quoteBean.getMarket());
                realTimeBean.setSymbol(quoteBean.getSymbol());
                realTimeBean.setTime(quoteBean.getTime());
                setChangeRate(realTimeBean);
            }
        }

        if (simpleTradeFragment != null)
            simpleTradeFragment.setFirstRealTimePrice(quote);
    }

    /**
     * 增减自选
     */
    private void ditySymbol(ArrayList<String> symbol) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < symbol.size(); i++) {

            if (i == 0)
                stringBuffer.append(symbol.get(i));
            else {
                stringBuffer.append(",");
                stringBuffer.append(symbol.get(i));
            }

        }
        showCustomProgress();
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("symbol", stringBuffer.toString());
        CustomHttpUtils.getServiceDatas(map, UrlContents.DEAL_SYMBOL_DIYSYMBOL, className + "4", new CustomHttpUtils.ServiceStatus() {
            @Override
            public void faild(Call call, Exception e, int id) {
                disCustomProgress();
                showToast(getString(R.string.network_error));
            }

            @Override
            public void success(String response, int id) {
                if (response != null) {
                    JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                    String errCode = "";
                    String errMsg = "";
                    if (!jsonObject.get("err_code").isJsonNull())
                        errCode = jsonObject.get("err_code").getAsString();
                    if (!jsonObject.get("err_msg").isJsonNull())
                        errMsg = jsonObject.get("err_msg").getAsString();

                    if (errCode.equals("0")) {
                        //通知更新行情自选列表
                        EventBus.getDefault().post(new EventBean(EventContents.UPDATED_SELF_SYMBOL, null));
                        if (collectionStatus) {
                            showToast(getString(R.string.add_collection_success));
                            ivCollection.setImageResource(R.mipmap.kline_have_collection);
                        } else {
                            showToast(getString(R.string.cancle_collection_success));
                            ivCollection.setImageResource(R.mipmap.kline_not_collection);
                        }
                    } else
                        showToast(errMsg);
                }
                disCustomProgress();
            }
        });

    }
}
