package com.ts.lys.yibei.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.adapter.PendingAdapter;
import com.ts.lys.yibei.customeview.CustomPopWindow;

import butterknife.Bind;

/**
 * Created by jcdev1 on 2018/7/5.
 */

public class PendingFragment extends BaseFragment {
    @Bind(R.id.x_recycler)
    XRecyclerView xRecycler;
    @Bind(R.id.ll_father)
    LinearLayout llFather;

    private PendingAdapter adapter;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_position;
    }

    @Override
    protected void initBaseView() {

        initView();
        initListener();
    }

    private void initView() {

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        xRecycler.setLayoutManager(manager);
        xRecycler.setLoadingMoreEnabled(false);
        xRecycler.setPullRefreshEnabled(false);
        xRecycler.setAdapter(adapter = new PendingAdapter(getActivity()));

    }

    private void initListener() {

        adapter.setOnItemClickListener(new PendingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {

            }

            @Override
            public void onCanclePendingClick() {

                View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_base_remind_layout, null);

                TextView tvTitle = contentView.findViewById(R.id.tv_title);
                tvTitle.setText("您确定撤销（订单123456）挂单？");
                CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
                        .setView(contentView)
                        .enableBackgroundDark(true)
                        .setBgDarkAlpha(0.7f)
                        .create()
                        .showAtLocation(llFather, Gravity.CENTER, 0, 0);
            }
        });

    }
}
