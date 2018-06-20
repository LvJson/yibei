package com.ts.lys.yibei.ui.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.adapter.HotForeignAdapter;
import com.ts.lys.yibei.adapter.HotRecommendAdapter;
import com.ts.lys.yibei.customeview.RecycleViewDivider;
import com.ts.lys.yibei.customeview.ScrollRecyclerView;
import com.ts.lys.yibei.customeview.SwitcherView;
import com.ts.lys.yibei.customeview.Xcircleindicator;
import com.ts.lys.yibei.ui.activity.QuotationsActivity;
import com.ts.lys.yibei.utils.BaseUtils;
import com.ts.lys.yibei.utils.Logger;
import com.ts.lys.yibei.utils.NetworkImageHolderView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jcdev1 on 2018/6/8.
 */

public class HomeFragment extends BaseFragment {
    @Bind(R.id.convenient_banner)
    ConvenientBanner convenientBanner;
    @Bind(R.id.recycler_horizon)
    RecyclerView centerSnapRecyclerView;
    @Bind(R.id.xcircle_indicator)
    Xcircleindicator xcircleIndicator;
    @Bind(R.id.switcher_view)
    SwitcherView switcherView;
    @Bind(R.id.scroll_recycler_view)
    ScrollRecyclerView scrollRecyclerView;
    @Bind(R.id.scroll_view)
    ScrollView scrollView;
    @Bind(R.id.swipeRefreshh)
    SwipeRefreshLayout swipeRefreshh;

    private ArrayList<String> images = new ArrayList<>();
    private HotForeignAdapter hotForeignAdapter;//热门外汇
    private HotRecommendAdapter hotRecommendAdapter;//热门推荐
    private int positionTag = -1;//热门外汇所处页面

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initBaseView() {
    }

    @Override
    public void initBase() {
        super.initBase();
        initBanner();
        initHotForeignAdapter();
        initNoticeView();
        initHotRecommendAdapter();
    }


    /**
     * 轮播图
     */
    private void initBanner() {
        setBannerHight();
        loadTestDatas();
        convenientBanner.setPages(
                new CBViewHolderCreator() {
                    @Override
                    public NetworkImageHolderView createHolder(View itemView) {
                        return new NetworkImageHolderView(itemView, getActivity(), 1.66f);
                    }

                    @Override
                    public int getLayoutId() {
                        return R.layout.home_banner_layout;
                    }
                }, images)
                .setPageIndicator(new int[]{R.mipmap.home_point_normal, R.mipmap.home_point_choose})
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Logger.e("position", position + "");
                    }
                });
    }

    /**
     * 根据图片动态设置容器高度
     */
    private void setBannerHight() {
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        float height = width / 1.66f;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) convenientBanner.getLayoutParams();
        layoutParams.height = (int) height;
        convenientBanner.setLayoutParams(layoutParams);

    }

    /**
     * 加入测试Views
     */
    private void loadTestDatas() {
        images.add("http://img2.3lian.com/2014/f2/37/d/40.jpg");
        images.add("http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg");
        images.add("http://img2.3lian.com/2014/f2/37/d/39.jpg");
    }


    /**
     * 热门外汇
     */
    private void initHotForeignAdapter() {
        xcircleIndicator.setPageTotalCount(3);
        xcircleIndicator.setCurrentPage(0);
        LinearLayoutManager layoutManagerCenter
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        centerSnapRecyclerView.setLayoutManager(layoutManagerCenter);
        hotForeignAdapter = new HotForeignAdapter(getActivity());
        centerSnapRecyclerView.setAdapter(hotForeignAdapter);
        SnapHelper snapHelperCenter = new LinearSnapHelper();
        snapHelperCenter.attachToRecyclerView(centerSnapRecyclerView);
        centerSnapRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                if (manager instanceof LinearLayoutManager) {
                    int findFirstVisibleItemPosition = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
                    if (findFirstVisibleItemPosition != positionTag) {
                        Log.e("firstPosition", findFirstVisibleItemPosition + "");
                        xcircleIndicator.setCurrentPage(findFirstVisibleItemPosition);
                        positionTag = findFirstVisibleItemPosition;
                    }
                }
            }
        });
    }

    /**
     * 通知
     */
    private void initNoticeView() {
        ArrayList<String> noticeDta = new ArrayList<>();
        noticeDta.add("第一条通知!");
        noticeDta.add("第二条通知!!");
        noticeDta.add("第三条通知!!!易贝将于2018年5月24日上线，敬请期待!!!!!!!!!!!!!!!!!");
        switcherView.setResource(noticeDta);
        switcherView.startRolling();
        switcherView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("点击了第" + switcherView.getCurrentItem() + "条数据");
            }
        });

    }

    /**
     * 热门推荐
     */
    private void initHotRecommendAdapter() {

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        scrollRecyclerView.setLayoutManager(manager);
        scrollRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayout.VERTICAL, BaseUtils.dip2px(getActivity(), 1), getResources().getColor(R.color.bg_or_division_color)));
        scrollRecyclerView.setAdapter(hotRecommendAdapter = new HotRecommendAdapter(getActivity()));

    }

    @OnClick({R.id.tv_see_all, R.id.ll_notice, R.id.ll_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_see_all:
                break;
            case R.id.ll_notice:
                break;
            case R.id.ll_more:
                startActivity(new Intent(getActivity(), QuotationsActivity.class));
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        convenientBanner.startTurning(4000);
    }

    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        convenientBanner.stopTurning();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        switcherView.destroySwitcher();
    }
}
