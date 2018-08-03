package com.ts.lys.yibei.ui.fragment;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.adapter.HistoryAdapterAdapter;
import com.ts.lys.yibei.bean.OrderHistoryModel;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.mvppresenter.TradeHistoryListPresenter;
import com.ts.lys.yibei.mvpview.ITradeHistoryFragmentView;
import com.ts.lys.yibei.utils.CustomHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jcdev1 on 2018/7/5.
 */

public class TradeHistoryFragment extends BaseFragment implements ITradeHistoryFragmentView {
    @Bind(R.id.x_recycler)
    XRecyclerView xRecycler;
    @Bind(R.id.ll_net_not_work)
    LinearLayout llNetNotWork;
    @Bind(R.id.ll_not_data)
    LinearLayout llNotData;

    private int page = 1;
    private int pagesize = 10;
    /**
     * 历史列表集合数
     */
    private int size = 0;
    private TradeHistoryListPresenter presenter = new TradeHistoryListPresenter(this);
    private HistoryAdapterAdapter adapter;
    private Map<String, String> map = new HashMap<>();
    private List<OrderHistoryModel.DataBean.HistoryOrderBean> historyOrder = new ArrayList<>();
    private Handler handler = new Handler();
    private OrderFragment parentFragment;

    private boolean isFirstIn = false;


    @Override
    protected int getLayoutID() {
        isFirstIn = true;
        return R.layout.fragment_position;
    }

    @Override
    protected void initBaseView() {
        initView();
        initData();
        initListener();
    }


    private void initView() {
        parentFragment = (OrderFragment) getParentFragment();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        xRecycler.setLayoutManager(manager);
        xRecycler.setLoadingMoreEnabled(true);
        xRecycler.setPullRefreshEnabled(false);
        xRecycler.setAdapter(adapter = new HistoryAdapterAdapter(getActivity()));
    }

    private void initData() {

        presenter.attachView(this);
        map.put("userId", userId);
        map.put("accessToken", accessToken);
        map.put("page", String.valueOf(page));
        map.put("pagesize", String.valueOf(pagesize));
        presenter.getTradeHistoryList(map, className);

    }


    private void initListener() {

        xRecycler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        map.put("page", String.valueOf(page));
                        map.put("pagesize", String.valueOf(pagesize));
                        presenter.getTradeHistoryList(map, className);
                    }
                }, 1000);

            }
        });
    }


    @Override
    public void setTradeHistoryList(OrderHistoryModel orderHistoryModel) {
        isFirstIn = false;
        parentFragment.setRefleshEnable(false);
        List<OrderHistoryModel.DataBean.HistoryOrderBean> beanList = orderHistoryModel.getData().getHistoryOrder();

        if (beanList.size() != 0) {
            this.historyOrder.addAll(size, beanList);
            size += beanList.size();
            adapter.setData(this.historyOrder);
            xRecycler.refreshComplete();
            xRecycler.setLoadingMoreEnabled(true);
        } else {
            xRecycler.setNoMore(true);
            xRecycler.loadMoreComplete();
            xRecycler.setLoadingMoreEnabled(false);
            showToast(getString(R.string.loading_all));
        }

        if (this.historyOrder == null || this.historyOrder.size() == 0) {
            setErrorStatus(1);
            return;
        } else
            setErrorStatus(2);

    }

    @Override
    public void showToast(String content) {
        super.showToast(content);
        isFirstIn = false;
        parentFragment.setRefleshEnable(false);
        if (content.equals(BaseContents.NET_ERROR))
            setErrorStatus(0);
    }

    @OnClick(R.id.tv_reload)
    public void onViewClicked() {
        refreshData();
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
//        CustomHttpUtils.cancelHttp(className);
        if (!isFirstIn) {
            getUserIdAndToken();
            if (TextUtils.isEmpty(userId)) return;
            page = 1;
            size = 0;
            if (historyOrder.size() > 0)
                historyOrder.clear();
            map.put("userId", userId);
            map.put("accessToken", accessToken);
            map.put("page", String.valueOf(page));
            map.put("pagesize", String.valueOf(pagesize));
            presenter.getTradeHistoryList(map, className);
        }
        isFirstIn = false;


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
            xRecycler.setVisibility(View.GONE);
        } else if (errorStatus == 1) {//数据为空
            llNetNotWork.setVisibility(View.GONE);
            llNotData.setVisibility(View.VISIBLE);
            xRecycler.setVisibility(View.GONE);
        } else {
            llNetNotWork.setVisibility(View.GONE);
            llNotData.setVisibility(View.GONE);
            xRecycler.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        CustomHttpUtils.cancelHttp(className);
    }
}
