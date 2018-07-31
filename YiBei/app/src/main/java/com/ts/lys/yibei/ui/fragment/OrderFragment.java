package com.ts.lys.yibei.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.adapter.ViewPagerAdapter;
import com.ts.lys.yibei.bean.EventBean;
import com.ts.lys.yibei.bean.RealTimeBean;
import com.ts.lys.yibei.bean.TraderAccInfoModel;
import com.ts.lys.yibei.constant.EventContents;
import com.ts.lys.yibei.mvppresenter.TraderAccInfoPresenter;
import com.ts.lys.yibei.mvpview.IOrderFragmentView;
import com.ts.lys.yibei.utils.Arith;
import com.ts.lys.yibei.utils.BaseUtils;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.ts.lys.yibei.utils.Logger;

import net.lucode.hackware.magicindicator.MagicIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by jcdev1 on 2018/6/8.
 */

public class OrderFragment extends BaseFragment implements IOrderFragmentView {
    @Bind(R.id.tv_float_loss_profit)
    TextView tvFloatLossProfit;
    @Bind(R.id.tv_useful_margin)
    TextView tvUsefulMargin;
    @Bind(R.id.tv_have_used_margin)
    TextView tvHaveUsedMargin;
    @Bind(R.id.tv_account_worth)
    TextView tvAccountWorth;
    @Bind(R.id.tv_account_balance)
    TextView tvAccountBalance;
    @Bind(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    List<Fragment> fragmentList = new ArrayList<>();

    public boolean isShowRealData;

    private TraderAccInfoPresenter presenter = new TraderAccInfoPresenter(this);

    private TraderAccInfoModel.DataBean tradeAccInfo;

    /**
     * Viewpager当前位置
     */
    private int viewPagePosi = 0;

    @Override
    protected int getLayoutID() {
        EventBus.getDefault().register(this);
        return R.layout.fragment_order;
    }

    @Override
    protected void initBaseView() {

        initView();
        initData();
        initListener();

    }

    private void initView() {

        fragmentList.add(new PositionFragment());
        fragmentList.add(new PendingFragment());
        fragmentList.add(new TradeHistoryFragment());

        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), fragmentList));

        List<String> stringList = new ArrayList<>();
        stringList.add(getString(R.string.position));
        stringList.add(getString(R.string.pending));
        stringList.add(getString(R.string.history));

        BaseUtils.setNavigator(getActivity(), stringList, viewPager, magicIndicator, true);
    }

    private void initData() {
        if (TextUtils.isEmpty(userId)) return;
        presenter.attachView(this);
        Map<String, String> map = new HashMap<>();
        Logger.e("userId", userId);
        map.put("userId", userId);
        map.put("accessToken", accessToken);
        presenter.getTraderAccInfo(map, className);

    }


    private void initListener() {

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPagePosi = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (fragmentList.get(viewPagePosi) != null) {
                    if (viewPagePosi == 0) {// 刷新持仓列表
                        ((PositionFragment) fragmentList.get(viewPagePosi)).refreshData();
                    } else if (viewPagePosi == 1) {// 刷新挂单列表
                        ((PendingFragment) fragmentList.get(viewPagePosi)).refreshData();
                    } else if (viewPagePosi == 2) {// 刷新交易历史列表
                        ((TradeHistoryFragment) fragmentList.get(viewPagePosi)).refreshData();
                    }
                }


            }
        });
    }

    public void setRefleshEnable(boolean able) {

        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(able);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean event) {
        /**
         * 实时数据
         */

        String tagOne = event.getTagOne();

        if (tagOne.equals(EventContents.REAL_TIME_DATA)) {
            if (!isShowRealData) return;
            String json = (String) event.getResponse();
            RealTimeBean realTimeBean = new Gson().fromJson(json, RealTimeBean.class);
            if (realTimeBean == null) return;
            if (fragmentList.get(0) != null)
                ((PositionFragment) fragmentList.get(0)).setRealData(realTimeBean);
            if (fragmentList.get(1) != null)
                ((PendingFragment) fragmentList.get(1)).setRealData(realTimeBean);


        } else if (tagOne.equals(EventContents.ORDER_CLICK)) {
            isShowRealData = true;

        } else if (tagOne.equals(EventContents.ORDER_NOT_CLICK)) {
            isShowRealData = false;
        }
    }

    @Override
    public void setTradeAccInfo(TraderAccInfoModel.DataBean tradeAccInfo) {
        this.tradeAccInfo = tradeAccInfo;
        tvAccountWorth.setText("$" + BaseUtils.getDigitsData(tradeAccInfo.getEquity(), 2));//账户净值
        tvUsefulMargin.setText("$" + BaseUtils.getDigitsData(tradeAccInfo.getMarginFree(), 2));//可用保证金
        tvHaveUsedMargin.setText("$" + BaseUtils.getDigitsData(tradeAccInfo.getMargin(), 2));//已用保证金
        tvAccountBalance.setText("$" + BaseUtils.getDigitsData(tradeAccInfo.getBalance(), 2));//账户余额
        tvFloatLossProfit.setText(BaseUtils.dealSymbol(tradeAccInfo.getTotalProfit() == null ? 0 : tradeAccInfo.getTotalProfit()));//总的浮动盈亏

    }

    /**
     * 实时修改头部数据
     *
     * @param profitSum
     */
    public void updataHeadData(double profitSum) {

        tvFloatLossProfit.setText(BaseUtils.dealSymbol(profitSum));

        double eq = Arith.round(Arith.add(tradeAccInfo.getBalance(), profitSum), 2);
        tvAccountWorth.setText("$" + BaseUtils.getDigitsData(eq, 2));//账户净值
        tvUsefulMargin.setText("$" + BaseUtils.getDigitsData(Arith.sub(eq, tradeAccInfo.getMargin()), 2));//可用保证金


    }

    /**
     * viewPager滚动到指定位置
     *
     * @param position
     */
    public void setCurrentPosition(int position) {
        if (viewPager != null)
            viewPager.setCurrentItem(position, true);

        if (fragmentList.get(position) != null) {
            if (position == 0) {// 刷新持仓列表
                ((PositionFragment) fragmentList.get(position)).refreshData();
                refreshData();
            } else if (position == 1) {// 刷新挂单列表
                ((PendingFragment) fragmentList.get(position)).refreshData();
            } else if (position == 2) {// 刷新交易历史列表
                ((TradeHistoryFragment) fragmentList.get(position)).refreshData();
            }
        }

    }

    /**
     * 刷新订单上部数据
     */
    public void refreshData() {

        Map<String, String> map = new HashMap<>();
        Logger.e("userId", userId);
        map.put("userId", userId);
        map.put("accessToken", accessToken);
        presenter.getTraderAccInfo(map, className);
    }

    @Override
    public void onResume() {
        super.onResume();
        isShowRealData = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isShowRealData = false;

    }

    @Override
    public void onStop() {
        super.onStop();
        isShowRealData = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        presenter.detachView();
        CustomHttpUtils.cancelHttp(className);
    }
}
