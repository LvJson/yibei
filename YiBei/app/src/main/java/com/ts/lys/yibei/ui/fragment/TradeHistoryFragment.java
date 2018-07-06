package com.ts.lys.yibei.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.adapter.HistoryAdapterAdapter;

import butterknife.Bind;

/**
 * Created by jcdev1 on 2018/7/5.
 */

public class TradeHistoryFragment extends BaseFragment {
    @Bind(R.id.x_recycler)
    XRecyclerView xRecycler;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_position;
    }

    @Override
    protected void initBaseView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        xRecycler.setLayoutManager(manager);
        xRecycler.setLoadingMoreEnabled(false);
        xRecycler.setPullRefreshEnabled(false);
        xRecycler.setAdapter(new HistoryAdapterAdapter(getActivity()));
    }
}
