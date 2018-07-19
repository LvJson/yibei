package com.ts.lys.yibei.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.adapter.OrderPositionAdapter;
import com.ts.lys.yibei.customeview.CustomPopWindow;
import com.ts.lys.yibei.ui.activity.PositionDetailActivity;

import butterknife.Bind;

/**
 * Created by jcdev1 on 2018/7/5.
 */

public class PositionFragment extends BaseFragment {
    @Bind(R.id.x_recycler)
    XRecyclerView xRecycler;
    @Bind(R.id.ll_father)
    LinearLayout llFather;

    private OrderPositionAdapter adapter;

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
        xRecycler.setAdapter(adapter = new OrderPositionAdapter(getActivity()));
    }


    private void initListener() {

        adapter.setOnItemClickListener(new OrderPositionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {

                Intent intent = new Intent(getActivity(), PositionDetailActivity.class);
                intent.putExtra("symbol", "EURUSD");
                intent.putExtra("symbolCn", "欧元美元");
                intent.putExtra("digits", 5);
                startActivity(intent);

            }

            @Override
            public void onClosePositionClick() {
                View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_close_position_remind_layout, null);

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
