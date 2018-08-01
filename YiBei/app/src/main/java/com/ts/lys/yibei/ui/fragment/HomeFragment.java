package com.ts.lys.yibei.ui.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.adapter.HotForeignAdapter;
import com.ts.lys.yibei.adapter.HotRecommendAdapter;
import com.ts.lys.yibei.bean.EventBean;
import com.ts.lys.yibei.bean.IndexBean;
import com.ts.lys.yibei.bean.RealTimeBean;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.constant.EventContents;
import com.ts.lys.yibei.customeview.RecycleViewDivider;
import com.ts.lys.yibei.customeview.ScrollRecyclerView;
import com.ts.lys.yibei.customeview.SwitcherView;
import com.ts.lys.yibei.customeview.Xcircleindicator;
import com.ts.lys.yibei.mvppresenter.HomePresenter;
import com.ts.lys.yibei.mvpview.IHomeFragmentView;
import com.ts.lys.yibei.ui.activity.MainActivity;
import com.ts.lys.yibei.ui.activity.QuotationsActivity;
import com.ts.lys.yibei.utils.BaseUtils;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.ts.lys.yibei.utils.Logger;
import com.ts.lys.yibei.utils.NetworkImageHolderView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jcdev1 on 2018/6/8.
 */

public class HomeFragment extends BaseFragment implements IHomeFragmentView {
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
    @Bind(R.id.ll_net_not_work)
    LinearLayout llNetNotWork;
    @Bind(R.id.ll_not_data)
    LinearLayout llNotData;

    private ArrayList<String> bannerUrl = new ArrayList<>();
    private HotForeignAdapter hotForeignAdapter;//热门外汇
    private HotRecommendAdapter hotRecommendAdapter;//热门推荐
    private int positionTag = -1;//热门外汇所处页面

    private List<IndexBean.DataBean.BannerBean> banner;

    private HomePresenter presenter = new HomePresenter(this);
    public boolean isShowRealData;


    @Override
    protected int getLayoutID() {
        EventBus.getDefault().register(this);
        return R.layout.fragment_home;
    }

    @Override
    protected void initBaseView() {
        presenter.attachView(this);
        initBanner();
        initHotForeignAdapter();
        initHotRecommendAdapter();
        initData();
        initListener();
    }


    /**
     * 轮播图
     */
    private void initBanner() {
        setBannerHight();
        bannerUrl.add("");//可在没网的情况下可出现占位图
        convenientBanner.setPages(
                new CBViewHolderCreator() {
                    @Override
                    public NetworkImageHolderView createHolder(View itemView) {
                        return new NetworkImageHolderView(itemView, getActivity(), 2.027f);
                    }

                    @Override
                    public int getLayoutId() {
                        return R.layout.home_banner_layout;
                    }
                }, bannerUrl)
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
        float height = width / 2.027f;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) convenientBanner.getLayoutParams();
        layoutParams.height = (int) height;
        convenientBanner.setLayoutParams(layoutParams);

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
     * 热门推荐
     */
    private void initHotRecommendAdapter() {

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        scrollRecyclerView.setLayoutManager(manager);
        scrollRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayout.VERTICAL, BaseUtils.dip2px(getActivity(), 1), getResources().getColor(R.color.bg_or_division_color)));
        scrollRecyclerView.setAdapter(hotRecommendAdapter = new HotRecommendAdapter(getActivity()));

    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("accessToken", accessToken);
        presenter.getHomeData(map, className);

    }

    private void initListener() {

        swipeRefreshh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

    }

    @OnClick({R.id.tv_see_all, R.id.ll_notice, R.id.ll_more, R.id.tv_reload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_see_all:
                ((MainActivity) getActivity()).goSomeTab("订单", 2);
                break;
            case R.id.ll_notice:
                break;
            case R.id.ll_more:
                startActivity(new Intent(getActivity(), QuotationsActivity.class));
                break;
            case R.id.tv_reload:
                refreshData();
                break;
        }
    }

    /**
     * 首页所有数据
     *
     * @param indexBean
     */
    @Override
    public void showHomeData(IndexBean.DataBean indexBean) {

        if (indexBean != null) {
            List<IndexBean.DataBean.BannerBean> banner = indexBean.getBanner();
            List<IndexBean.DataBean.HotsBean> hots = indexBean.getHots();
            List<IndexBean.DataBean.NoticeBean> notice = indexBean.getNotice();
            List<IndexBean.DataBean.NewsBean> news = indexBean.getNews();
            /**
             * 展示数据
             */
            setBannerData(banner);
            hotForeignAdapter.setData(hots);
            initNoticeView(notice);
            hotRecommendAdapter.setData(news);
        }

        if (indexBean == null || indexBean.getNews() == null || indexBean.getNews().size() == 0)
            setErrorStatus(1);
        else
            setErrorStatus(2);


        if (swipeRefreshh != null)
            swipeRefreshh.setRefreshing(false);
    }

    /**
     * banner
     *
     * @param banner
     */
    private void setBannerData(List<IndexBean.DataBean.BannerBean> banner) {
        this.banner = banner;
        bannerUrl.clear();
        for (IndexBean.DataBean.BannerBean bannerBean : banner) {
            bannerUrl.add(bannerBean.getImage());
        }
        convenientBanner.notifyDataSetChanged();

    }

    /**
     * 通知
     */
    private void initNoticeView(List<IndexBean.DataBean.NoticeBean> noticeList) {
        if (noticeList == null || noticeList.size() == 0) return;

        ArrayList<String> noticeDta = new ArrayList<>();
        for (IndexBean.DataBean.NoticeBean nb : noticeList) {
            noticeDta.add(nb.getTitle());
        }

        switcherView.setResource(noticeDta);
        switcherView.startRolling();
        switcherView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("点击了第" + switcherView.getCurrentIndex() + "条数据");
            }
        });

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
            hotForeignAdapter.setUpdateModel(realTimeBean);

        } else if (tagOne.equals(EventContents.HOME_CLICK)) {
            isShowRealData = true;

        } else if (tagOne.equals(EventContents.HOME_NOT_CLICK)) {
            isShowRealData = false;
        }
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        getUserIdAndToken();
        if (TextUtils.isEmpty(userId)) return;
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("accessToken", accessToken);
        presenter.getHomeData(map, className);
    }

    @Override
    public void showToast(String content) {
        super.showToast(content);
        if (content.equals(BaseContents.NET_ERROR)) {
            setErrorStatus(0);
        }

        if (swipeRefreshh != null)
            swipeRefreshh.setRefreshing(false);
    }

    /**
     * 展示网络错误或者无数据界面
     *
     * @param errorStatus
     */
    public void setErrorStatus(int errorStatus) {

        if (errorStatus == 0) {//网络错误
            llNetNotWork.setVisibility(View.VISIBLE);
            llNotData.setVisibility(View.GONE);
            scrollRecyclerView.setVisibility(View.GONE);
        } else if (errorStatus == 1) {//数据为空
            llNetNotWork.setVisibility(View.GONE);
            llNotData.setVisibility(View.VISIBLE);
            scrollRecyclerView.setVisibility(View.GONE);
        } else {
            llNetNotWork.setVisibility(View.GONE);
            llNotData.setVisibility(View.GONE);
            scrollRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        isShowRealData = false;
        convenientBanner.startTurning(4000);
    }

    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        isShowRealData = false;
        convenientBanner.stopTurning();
    }

    @Override
    public void onStop() {
        super.onStop();
        isShowRealData = false;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        CustomHttpUtils.cancelHttp(className);
        presenter.detachView();
        switcherView.destroySwitcher();
    }
}
