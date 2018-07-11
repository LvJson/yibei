package com.ts.lys.yibei.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.google.gson.Gson;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.adapter.ViewPagerAdapter;
import com.ts.lys.yibei.bean.EventBean;
import com.ts.lys.yibei.bean.GetQuotesModel;
import com.ts.lys.yibei.bean.RealTimeBean;
import com.ts.lys.yibei.constant.EventContents;
import com.ts.lys.yibei.mvppresenter.AllSymbolPresenter;
import com.ts.lys.yibei.mvpview.IMarketFragmentView;
import com.ts.lys.yibei.utils.BaseUtils;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.ts.lys.yibei.utils.Logger;

import net.lucode.hackware.magicindicator.MagicIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by jcdev1 on 2018/6/8.
 */

public class MarketFragment extends BaseFragment implements IMarketFragmentView {


    private static final String[] tabName = {"自选", "外汇", "指数", "贵金属", "原油"};
    @Bind(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    private List<String> mDataList = Arrays.asList(tabName);

    private AllSymbolPresenter allSymbolPresenter;

    private List<Fragment> fragmentList = new ArrayList<>();

    private List<GetQuotesModel.DataBean.SymbolsBean> listColl = new ArrayList<>();//自选
    private List<GetQuotesModel.DataBean.SymbolsBean> list0 = new ArrayList<>();//外汇
    private List<GetQuotesModel.DataBean.SymbolsBean> list1 = new ArrayList<>();//指数
    private List<GetQuotesModel.DataBean.SymbolsBean> list2 = new ArrayList<>();//贵金属
    private List<GetQuotesModel.DataBean.SymbolsBean> list3 = new ArrayList<>();//能源

    private ArrayList<String> selfSelectSymbol = new ArrayList<>();


    @Override
    protected int getLayoutID() {
        EventBus.getDefault().register(this);
        return R.layout.fragment_market;
    }

    @Override
    protected void initBaseView() {
        initFragment();
        initListener();
        initData();
    }

    private void initFragment() {
        for (int i = 0; i < 5; i++) {
            AllSymbolMarketFragment allSymbolMarketFragment = new AllSymbolMarketFragment();
            Bundle bundle = new Bundle();
            bundle.putString("tag", tabName[i]);
            allSymbolMarketFragment.setArguments(bundle);
            fragmentList.add(allSymbolMarketFragment);
        }

        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), fragmentList));
        BaseUtils.setNavigator(getActivity(), mDataList, viewPager, magicIndicator, false);
    }

    private void initListener() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //滚动之后会有监听，position 是位置

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        allSymbolPresenter = new AllSymbolPresenter(this);
        allSymbolPresenter.attachView(this);
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("userId", userId);
        allSymbolPresenter.getCollectionSymbol(map, className + "1");
        allSymbolPresenter.getAllSymbol(map, className + "1");

    }

    /**
     * 所有产品的市场价
     *
     * @param allSymbol
     */
    @Override
    public void setAllSymbol(GetQuotesModel allSymbol) {
        if (getActivity() == null) return;
        list0.clear();
        list1.clear();
        list2.clear();
        list3.clear();
        List<GetQuotesModel.DataBean.SymbolsBean> sb = allSymbol.getData().getSymbols();
        if (allSymbol != null && sb.size() > 0) {
            for (int i = 0; i < sb.size(); i++) {
                switch (sb.get(i).getGroup()) {
                    case 0:
                        list0.add(sb.get(i));
                        break;
                    case 1:
                        list1.add(sb.get(i));
                        break;
                    case 2:
                        list2.add(sb.get(i));
                        break;
                    case 3:
                        list3.add(sb.get(i));
                        break;
                }
            }
        }

        ((AllSymbolMarketFragment) fragmentList.get(1)).setFirstList(list0);
        ((AllSymbolMarketFragment) fragmentList.get(2)).setFirstList(list1);
        ((AllSymbolMarketFragment) fragmentList.get(3)).setFirstList(list2);
        ((AllSymbolMarketFragment) fragmentList.get(4)).setFirstList(list3);
        if (list0 == null || list0.size() == 0)
            ((AllSymbolMarketFragment) fragmentList.get(1)).setErrorStatus(1);
        else
            ((AllSymbolMarketFragment) fragmentList.get(1)).setErrorStatus(2);

        if (list1 == null || list1.size() == 0)
            ((AllSymbolMarketFragment) fragmentList.get(2)).setErrorStatus(1);
        else
            ((AllSymbolMarketFragment) fragmentList.get(2)).setErrorStatus(2);

        if (list2 == null || list2.size() == 0)
            ((AllSymbolMarketFragment) fragmentList.get(3)).setErrorStatus(1);
        else
            ((AllSymbolMarketFragment) fragmentList.get(3)).setErrorStatus(2);

        if (list3 == null || list3.size() == 0)
            ((AllSymbolMarketFragment) fragmentList.get(4)).setErrorStatus(1);
        else
            ((AllSymbolMarketFragment) fragmentList.get(4)).setErrorStatus(2);

    }

    /**
     * 用户关注的产品市场价
     *
     * @param collectionSymbol
     */
    @Override
    public void setCollectionSymbol(GetQuotesModel collectionSymbol) {
        if (getActivity() == null) return;
        listColl.clear();
        List<GetQuotesModel.DataBean.SymbolsBean> sb = collectionSymbol.getData().getSymbols();
        listColl = sb;
        ((AllSymbolMarketFragment) fragmentList.get(0)).setFirstList(listColl);
        if (listColl == null || listColl.size() == 0)
            ((AllSymbolMarketFragment) fragmentList.get(0)).setErrorStatus(1);
        else
            ((AllSymbolMarketFragment) fragmentList.get(0)).setErrorStatus(2);

        for (int i = 0; i < sb.size(); i++) {
            selfSelectSymbol.add(sb.get(i).getSymbolEn());
        }

        for (int i = 0; i < fragmentList.size(); i++)
            ((AllSymbolMarketFragment) fragmentList.get(i)).setSelfSelectSymbol(selfSelectSymbol);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean event) {
        /**
         * 实时数据
         */
        if (event.getTagOne().equals(EventContents.REAL_TIME_DATA)) {
            String json = (String) event.getResponse();
            RealTimeBean realTimeBean = new Gson().fromJson(json, RealTimeBean.class);
            if (realTimeBean == null) return;
            for (Fragment fragment : fragmentList) {
                if (fragment != null) {
                    ((AllSymbolMarketFragment) fragment).setRealTimeData(realTimeBean);
                }
            }

        } else if (event.getTagOne().equals(EventContents.UPDATED_SELF_SYMBOL)) {
            Map<String, String> map = new HashMap<>();
            map.put("accessToken", accessToken);
            map.put("userId", userId);
            allSymbolPresenter.getCollectionSymbol(map, className + "1");
        } else if (event.getTagOne().equals(EventContents.NET_NOT_ERROR)) {
            for (int i = 0; i < fragmentList.size(); i++) {
                ((AllSymbolMarketFragment) fragmentList.get(i)).setErrorStatus(0);
                Logger.e("NET_NOT_ERROR", "收到消息");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        allSymbolPresenter.detachView();
        CustomHttpUtils.cancelHttp(className + "1");
        CustomHttpUtils.cancelHttp(className + "2");

    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("userId", userId);
        allSymbolPresenter.getCollectionSymbol(map, className + "1");
        allSymbolPresenter.getAllSymbol(map, className + "1");
    }

    @Override
    public void showToast(String content) {
        super.showToast(content);

    }
}
