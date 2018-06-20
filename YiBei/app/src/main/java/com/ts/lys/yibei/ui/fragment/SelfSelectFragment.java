package com.ts.lys.yibei.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.adapter.SelfSelectAdapter;
import com.ts.lys.yibei.customeview.RecycleViewDivider;
import com.ts.lys.yibei.utils.BaseUtils;

import butterknife.Bind;

/**
 * Created by jcdev1 on 2018/6/15.
 */

public class SelfSelectFragment extends BaseFragment {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    SelfSelectAdapter selfSelectAdapter;



    @Override
    protected int getLayoutID() {
        return R.layout.fragment_self_select;
    }

    @Override
    protected void initBaseView() {

    }

    @Override
    public void initBase() {
        super.initBase();

        initAdapter();
    }

    private void initAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayout.VERTICAL, BaseUtils.dip2px(getActivity(), 1), getResources().getColor(R.color.bg_or_division_color)));
        recyclerView.setAdapter(selfSelectAdapter = new SelfSelectAdapter(getActivity()));
    }
}
