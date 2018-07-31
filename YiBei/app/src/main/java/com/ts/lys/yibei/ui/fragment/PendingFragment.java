package com.ts.lys.yibei.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.adapter.PendingAdapter;
import com.ts.lys.yibei.bean.OrderPendingModel;
import com.ts.lys.yibei.bean.RealTimeBean;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.customeview.CustomPopWindow;
import com.ts.lys.yibei.mvppresenter.PendingListPresenter;
import com.ts.lys.yibei.mvpview.IPendingFragmentView;
import com.ts.lys.yibei.ui.activity.PendingDetailActivity;
import com.ts.lys.yibei.utils.CustomHttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jcdev1 on 2018/7/5.
 */

public class PendingFragment extends BaseFragment implements IPendingFragmentView {
    @Bind(R.id.x_recycler)
    XRecyclerView xRecycler;
    @Bind(R.id.ll_father)
    LinearLayout llFather;
    @Bind(R.id.ll_net_not_work)
    LinearLayout llNetNotWork;
    @Bind(R.id.ll_not_data)
    LinearLayout llNotData;

    private PendingAdapter adapter;
    private PendingListPresenter presenter = new PendingListPresenter(this);
    List<OrderPendingModel.DataBean.PendOrderBean> pendingList;
    private OrderFragment parentFragment;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_position;
    }

    @Override
    protected void initBaseView() {

        initView();
        initListener();
        initData();
    }

    private void initView() {
        parentFragment = (OrderFragment) getParentFragment();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        xRecycler.setLayoutManager(manager);
        xRecycler.setLoadingMoreEnabled(false);
        xRecycler.setPullRefreshEnabled(false);
        xRecycler.setAdapter(adapter = new PendingAdapter(getActivity()));

    }

    private void initListener() {

        adapter.setOnItemClickListener(new PendingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(OrderPendingModel.DataBean.PendOrderBean pb) {
                Intent intent = new Intent(getActivity(), PendingDetailActivity.class);
                intent.putExtra("symbol", pb.getSymbolEn());
                intent.putExtra("symbolCn", pb.getSymbolCn());
                intent.putExtra("digits", pb.getDigits());
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", pb);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onCanclePendingClick(final OrderPendingModel.DataBean.PendOrderBean pb) {
                showCancelPendingPop(pb);
            }
        });

    }

    /**
     * 取消挂单
     *
     * @param pb
     */
    private void showCancelPendingPop(final OrderPendingModel.DataBean.PendOrderBean pb) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_base_remind_layout, null);
        TextView tvTitle = contentView.findViewById(R.id.tv_title);
        TextView tvCancel = contentView.findViewById(R.id.tv_cancle);
        TextView tvConfirm = contentView.findViewById(R.id.tv_confirm);
        tvTitle.setText(getString(R.string.cancel_pending) + "（" + getString(R.string.order) + pb.getTicket() + "）" + getString(R.string.pending) + "?");
        final CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
                .setView(contentView)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(0.7f)
                .create()
                .showAtLocation(llFather, Gravity.CENTER, 0, 0);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPopWindow.dissmiss();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPopWindow.dissmiss();
                Map<String, String> map = new HashMap<>();
                map.put("ticket", pb.getTicket());
                map.put("userId", userId);
                map.put("accessToken", accessToken);
                map.put("symbol", pb.getSymbolEn());
                map.put("volume", String.valueOf(pb.getVolume()));
                map.put("cmd", String.valueOf(pb.getCmd()));
                map.put("openPrice", String.valueOf(pb.getOpenPrice()));
                presenter.getCancelPendingBackInfo(map, className + "2");
            }
        });
    }

    private void initData() {
        presenter.attachView(this);
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("accessToken", accessToken);
        presenter.getPendingList(map, className + "1");
    }


    @Override
    public void setPendingList(List<OrderPendingModel.DataBean.PendOrderBean> pendingList) {
        parentFragment.setRefleshEnable(false);
        if (pendingList == null || pendingList.size() == 0) {
            setErrorStatus(1);
            this.pendingList = null;
            return;
        } else
            setErrorStatus(2);
        this.pendingList = pendingList;
        adapter.setData(pendingList);
    }

    @Override
    public void setCancelPendingBackInfo(boolean result) {
        refreshData();
    }


    /**
     * 实时价格
     *
     * @param realTimeBean
     */
    private double[] price = new double[3];

    public void setRealData(RealTimeBean realTimeBean) {
        if (realTimeBean != null && pendingList != null && pendingList.size() > 0) {

            for (int i = 0; i < pendingList.size(); i++) {
                OrderPendingModel.DataBean.PendOrderBean pob = pendingList.get(i);
                if (pob.getSymbolEn().equals(realTimeBean.getSymbol())) {

                    if ((pob.getCmd() == 2 || pob.getCmd() == 4) && pob.getMarket() == realTimeBean.getBid()) {
                        continue;
                    } else if (pob.getMarket() == realTimeBean.getAsk()) {
                        continue;
                    }

                    price[0] = realTimeBean.getAsk();
                    price[1] = realTimeBean.getBid();
                    price[2] = realTimeBean.getMarket();

                    if (pob.getCmd() == 2 || pob.getCmd() == 4)
                        pob.setMarket(realTimeBean.getBid());
                    else
                        pob.setMarket(realTimeBean.getAsk());
                    adapter.update(i, price);

                }

            }
        }

    }

    @Override
    public void showToast(String content) {
        super.showToast(content);
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

        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("accessToken", accessToken);
        presenter.getPendingList(map, className + "1");

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
        CustomHttpUtils.cancelHttp(className + "1");
        CustomHttpUtils.cancelHttp(className + "2");
    }
}
