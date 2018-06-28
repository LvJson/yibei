package com.ts.lys.yibei.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.adapter.SelfSelectAdapter;
import com.ts.lys.yibei.bean.EventBean;
import com.ts.lys.yibei.bean.GetQuotesModel;
import com.ts.lys.yibei.bean.RealTimeBean;
import com.ts.lys.yibei.constant.EventContents;
import com.ts.lys.yibei.customeview.RecycleViewDivider;
import com.ts.lys.yibei.ui.activity.QuotationsActivity;
import com.ts.lys.yibei.utils.BaseUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * Created by jcdev1 on 2018/6/15.
 */

public class AllSymbolMarketFragment extends BaseFragment {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    SelfSelectAdapter selfSelectAdapter;
    private String tag;

    private int position = -1;//待删除条目位置
    private MyDialogFragment myDialogFragment;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_self_select;
    }

    @Override
    protected void initBaseView() {
        EventBus.getDefault().register(this);
        initData();
        initAdapter();
        initListener();

    }

    private void initAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayout.VERTICAL, BaseUtils.dip2px(getActivity(), 1), getResources().getColor(R.color.bg_or_division_color)));
        recyclerView.setAdapter(selfSelectAdapter = new SelfSelectAdapter(getActivity(), tag));
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
    }

    //"自选", "外汇", "指数", "贵金属", "原油"
    private void initData() {
        tag = getArguments().getString("tag");

    }

    private void initListener() {

        selfSelectAdapter.setOnItemClickListenerr(new SelfSelectAdapter.OnItemClickListenerr() {
            @Override
            public void onSingleClick(GetQuotesModel.DataBean.SymbolsBean model, int position) {
                Intent intent = new Intent(getActivity(), QuotationsActivity.class);
                intent.putExtra("symbol", model.getSymbolEn());
                intent.putExtra("symbolCn", model.getSymbolCn());
                intent.putExtra("tag", 0);
                intent.putExtra("digits", model.getDigits());
                startActivity(intent);
            }
        });

        selfSelectAdapter.setOnLongClickListener(new SelfSelectAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(GetQuotesModel.DataBean.SymbolsBean model, int position) {
                AllSymbolMarketFragment.this.position = position;
                myDialogFragment = new MyDialogFragment();
                myDialogFragment.show(getFragmentManager(), "longclick");
            }
        });
    }


    /**
     * 初始数据
     *
     * @param firstList
     */
    public void setFirstList(List<GetQuotesModel.DataBean.SymbolsBean> firstList) {
        selfSelectAdapter.setData(firstList);

        if (tag.equals("自选")) {
        } else if (tag.equals("外汇")) {
        } else if (tag.equals("指数")) {
        } else if (tag.equals("贵金属")) {
        } else if (tag.equals("原油")) {
        }

    }


    /**
     * 设置实时数据
     *
     * @param realTimeBean
     */
    public void setRealTimeData(RealTimeBean realTimeBean) {
        selfSelectAdapter.setUpdateModel(realTimeBean);

        if (tag.equals("自选")) {

        } else if (tag.equals("外汇")) {


        } else if (tag.equals("指数")) {


        } else if (tag.equals("贵金属")) {


        } else if (tag.equals("原油")) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public static class MyDialogFragment extends DialogFragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.dialog_market_fragment, container);
            view.findViewById(R.id.tv_cancle_collection).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new EventBean(EventContents.LONG_CLICK, null));
                }
            });
            return view;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean event) {
        /**
         * 实时数据
         */
        if (event.getTagOne().equals(EventContents.LONG_CLICK)) {
            if (position != -1) {
                myDialogFragment.dismiss();
                selfSelectAdapter.removeItem(position);
            }

        }
    }


}
