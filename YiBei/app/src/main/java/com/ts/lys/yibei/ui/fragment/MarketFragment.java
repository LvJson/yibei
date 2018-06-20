package com.ts.lys.yibei.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.adapter.ViewPagerAdapter;
import com.ts.lys.yibei.utils.BaseUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * Created by jcdev1 on 2018/6/8.
 */

public class MarketFragment extends BaseFragment {


    private static final String[] tabName = {"自选", "外汇", "指数", "贵金属", "原油"};
    @Bind(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    private List<String> mDataList = Arrays.asList(tabName);

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_market;
    }

    @Override
    protected void initBaseView() {

    }

    @Override
    public void initBase() {
        super.initBase();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new SelfSelectFragment());
        fragmentList.add(new SelfSelectFragment());
        fragmentList.add(new SelfSelectFragment());
        fragmentList.add(new SelfSelectFragment());
        fragmentList.add(new SelfSelectFragment());

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), fragmentList));
        BaseUtils.setNavigator(getActivity(), mDataList, viewPager, magicIndicator, false);

        initListener();

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
}
