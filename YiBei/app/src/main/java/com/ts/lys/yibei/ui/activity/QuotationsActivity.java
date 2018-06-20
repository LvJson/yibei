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
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.jaeger.library.StatusBarUtil;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.customeview.KeyboardLayout;
import com.ts.lys.yibei.ui.fragment.ComplexTradeFragment;
import com.ts.lys.yibei.ui.fragment.SimpleTradeFragment;
import com.ts.lys.yibei.utils.BaseUtils;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jcdev1 on 2018/6/19.
 */

public class QuotationsActivity extends BaseFragmentActivity {

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


    private String[] CHANNELS;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private FragmentContainerHelper mFragmentContainerHelper = new FragmentContainerHelper();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotations);
        ButterKnife.bind(this);

        initView();
        initListener();
    }

    private void initView() {
        initFragments();
        initMagicIndicator();
        mFragmentContainerHelper.handlePageSelected(0, false);
        switchPages(0);
        setBackButton();
        StatusBarUtil.setColor(this, getResources().getColor(R.color.rise_color), 0);
    }


    private void initListener() {

    }

    private void initFragments() {
        CHANNELS = new String[]{getString(R.string.simple), getString(R.string.senior)};

        SimpleTradeFragment simpleTradeFragment = new SimpleTradeFragment();
        ComplexTradeFragment complexTradeFragment = new ComplexTradeFragment();
        mFragments.add(simpleTradeFragment);
        mFragments.add(complexTradeFragment);

        ComplexTradeFragment complexTradeFragment2 = new ComplexTradeFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_layout_k_line, complexTradeFragment2);
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
}
