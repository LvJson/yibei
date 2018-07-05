package com.ts.lys.yibei.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.adapter.ViewPagerAdapter;
import com.ts.lys.yibei.utils.BaseUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by jcdev1 on 2018/6/8.
 */

public class OrderFragment extends BaseFragment {
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

    List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_order;
    }

    @Override
    protected void initBaseView() {

        initView();

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
}
