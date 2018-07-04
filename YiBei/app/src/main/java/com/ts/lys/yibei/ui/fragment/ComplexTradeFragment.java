package com.ts.lys.yibei.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.adapter.ViewPagerAdapter;
import com.ts.lys.yibei.bean.EventBean;
import com.ts.lys.yibei.bean.RealTimeBean;
import com.ts.lys.yibei.constant.EventContents;
import com.ts.lys.yibei.ui.activity.QuotationsActivity;
import com.ts.lys.yibei.utils.BaseUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * Created by jcdev1 on 2018/6/19.
 */

public class ComplexTradeFragment extends BaseFragment {
    @Bind(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

    QuotationsActivity parentActivity;
    private MarketTradeFragment marketTradeFragment;
    private PendingTradeFragment pendingTradeFragment;


    @Override
    protected int getLayoutID() {
        return R.layout.fragment_complex_trade;
    }

    @Override
    protected void initBaseView() {

        parentActivity = (QuotationsActivity) getActivity();
        initFragment();
        initListener();
    }

    private void initFragment() {
        String[] tabName0 = {getString(R.string.market_trade), getString(R.string.pending_trade)};
        List<String> tabName1 = Arrays.asList(tabName0);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(marketTradeFragment = new MarketTradeFragment());
        fragmentList.add(pendingTradeFragment = new PendingTradeFragment());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), fragmentList));
        BaseUtils.setNavigator(getActivity(), tabName1, viewPager, magicIndicator, true);

    }

    private void initListener() {

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    parentActivity.marketOrPendingTrade = 0;
                    EventBus.getDefault().post(new EventBean(EventContents.BUTTON_STATUS_CHANGE, null));
                } else if (position == 1) {
                    parentActivity.marketOrPendingTrade = 1;
                    EventBus.getDefault().post(new EventBean(EventContents.BUTTON_STATUS_CHANGE, null));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 软键盘状态
     *
     * @param state
     */
    public void setKeyboardStatus(int state) {
        if (marketTradeFragment != null)
            marketTradeFragment.setKeyboardStatus(state);
        if (pendingTradeFragment != null)
            pendingTradeFragment.setKeyboardStatus(state);
    }


    /**
     * 接收顺序 1
     * <p>
     * 接收实时价格
     *
     * @param quoteBeen
     */
    public void setRealPrice(RealTimeBean quoteBeen) {
        if (marketTradeFragment != null)
            marketTradeFragment.setRealPrice(quoteBeen);
        if (pendingTradeFragment != null)
            pendingTradeFragment.setRealPrice(quoteBeen);
    }

    /**
     * 复杂-市价-买入
     */
    public void complexBuyIn() {
        if (marketTradeFragment != null)
            marketTradeFragment.complexBuyIn();
    }

    /**
     * 复杂-市价-卖出
     */
    public void complexSellOut() {
        if (marketTradeFragment != null)
            marketTradeFragment.complexSellOut();
    }

    /**
     * 复杂-挂单-买入
     */
    public void complexPendingBuyIn() {

        if (pendingTradeFragment != null)
            pendingTradeFragment.complexPendingBuyIn();
    }

    /**
     * 复杂-挂单-卖出
     */
    public void complexPendingSellOut() {
        if (pendingTradeFragment != null)
            pendingTradeFragment.complexPendingSellOut();
    }


}
