package com.ts.lys.yibei.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.adapter.ViewPagerAdapter;
import com.ts.lys.yibei.utils.BaseUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by jcdev1 on 2018/6/8.
 */

public class InfomationFragment extends BaseFragment {
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    List<Fragment> fragmentList = new ArrayList<>();
    private TextView tabText;
    public String tabTitles[] = new String[2];


    @Override
    protected int getLayoutID() {
        return R.layout.fragment_infomation;
    }

    @Override
    protected void initBaseView() {

        initView();
    }

    private void initView() {
        tabTitles[0] = getString(R.string.financial_calendar);
        tabTitles[1] = getString(R.string.infomation);
        fragmentList.clear();

        FinancialCalendarFragment financialCalendarFragment = new FinancialCalendarFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("url", "https://rili-d.jin10.com/open.php");
        financialCalendarFragment.setArguments(bundle1);
        fragmentList.add(financialCalendarFragment);

        WebViewFragment webViewFragment = new WebViewFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("url", "https://rili-d.jin10.com/open.php");
        webViewFragment.setArguments(bundle2);
        fragmentList.add(webViewFragment);

        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), fragmentList));
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabText = new TextView(getActivity());
            if (tabLayout.getTabAt(i) != null && tabLayout.getTabAt(i).isSelected()) {
                tabText.setTextColor(BaseUtils.getColor(getActivity(), R.color.white));
                tabText.setTextSize(22);
            } else {
                tabText.setTextColor(BaseUtils.getColor(getActivity(), R.color.white));
                tabText.setTextSize(16);
            }
            tabLayout.getTabAt(i).setCustomView(tabText);
            tabText.setText(tabTitles[i]);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setTabColorAndSize(tab, 22);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setTabColorAndSize(tab, 16);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setTabColorAndSize(TabLayout.Tab tab, int textSize) {
        TextView textView = (TextView) tab.getCustomView();
        if (textView == null) return;
        textView.setTextSize(textSize);
        textView.setText(tabTitles[tab.getPosition()]);
        tabText.setTextColor(getResources().getColor(R.color.white));
    }
}
