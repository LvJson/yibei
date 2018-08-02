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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.adapter.SelfSelectAdapter;
import com.ts.lys.yibei.bean.EventBean;
import com.ts.lys.yibei.bean.GetQuotesModel;
import com.ts.lys.yibei.bean.RealTimeBean;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.constant.EventContents;
import com.ts.lys.yibei.constant.UrlContents;
import com.ts.lys.yibei.customeview.RecycleViewDivider;
import com.ts.lys.yibei.ui.activity.QuotationsActivity;
import com.ts.lys.yibei.utils.BaseUtils;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.ts.lys.yibei.utils.Logger;
import com.ts.lys.yibei.utils.SpUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by jcdev1 on 2018/6/15.
 */

public class AllSymbolMarketFragment extends BaseFragment {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    SelfSelectAdapter selfSelectAdapter;
    @Bind(R.id.ll_net_not_work)
    AutoLinearLayout llNetNotWork;
    @Bind(R.id.ll_not_data)
    AutoLinearLayout llNotData;
    private String tag;

    private int position = -1;//待删除条目位置
    private MyDialogFragment myDialogFragment;

    private ArrayList<String> selfSelectSymbol = new ArrayList<>();

    private MarketFragment marketFragment;

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
//        recyclerView.setItemAnimator(new SlideInLeftAnimator());//item 移除动画
//        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);//取消item默认动画

    }

    //"自选", "外汇", "指数", "贵金属", "原油"
    private void initData() {
        tag = getArguments().getString("tag");
        marketFragment = (MarketFragment) getParentFragment();
    }

    private void initListener() {

        selfSelectAdapter.setOnItemClickListenerr(new SelfSelectAdapter.OnItemClickListenerr() {
            @Override
            public void onSingleClick(GetQuotesModel.DataBean.SymbolsBean model, int position) {
                Intent intent = new Intent(getActivity(), QuotationsActivity.class);
                intent.putExtra("symbol", model.getSymbolEn());
                intent.putExtra("symbolCn", model.getSymbolCn());
                intent.putExtra("tag", tag);
                intent.putExtra("digits", model.getDigits());
                intent.putStringArrayListExtra("symbolList", selfSelectSymbol);
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
        if (firstList == null) return;
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

    /**
     * 自选symbol集合
     *
     * @param list
     */
    public void setSelfSelectSymbol(ArrayList<String> list) {
        if (list != null && list.size() > 0)
            this.selfSelectSymbol = list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        CustomHttpUtils.cancelHttp(className);
    }

    @OnClick(R.id.tv_reload)
    public void onViewClicked() {
        marketFragment.refreshData();
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
         * 长安删除
         */
        if (event.getTagOne().equals(EventContents.LONG_CLICK) && tag.equals("自选")) {
            selfSelectSymbol.remove(position);
            ditySymbol(selfSelectSymbol, position);
        }
    }

    /**
     * 增减自选
     */
    private void ditySymbol(ArrayList<String> symbol, final int position) {
        accessToken = SpUtils.getString(getActivity(), BaseContents.ACCESS_TOKEN, "");
        userId = SpUtils.getString(getActivity(), BaseContents.USERID, "");
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < symbol.size(); i++) {

            if (i == 0)
                stringBuffer.append(symbol.get(i));
            else {
                stringBuffer.append(",");
                stringBuffer.append(symbol.get(i));
            }
        }
        showCustomProgress();
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("accessToken", accessToken);
        map.put("symbol", stringBuffer.toString());
        CustomHttpUtils.getServiceDatas(map, UrlContents.DEAL_SYMBOL_DIYSYMBOL, className, new CustomHttpUtils.ServiceStatus() {
            @Override
            public void faild(Call call, Exception e, int id) {
                disCustomProgress();
                myDialogFragment.dismiss();
                showToast(getString(R.string.network_error));
            }

            @Override
            public void success(String response, int id) {
                if (response != null) {
                    JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                    String errCode = "";
                    String errMsg = "";
                    if (!jsonObject.get("err_code").isJsonNull())
                        errCode = jsonObject.get("err_code").getAsString();
                    if (!jsonObject.get("err_msg").isJsonNull())
                        errMsg = jsonObject.get("err_msg").getAsString();

                    if (errCode.equals("0")) {
                        if (position != -1) {
                            selfSelectAdapter.removeItem(position);
                            marketFragment.refreshData();
                        }
                        showToast(getString(R.string.cancle_collection_success));
                    } else
                        showToast(errMsg);
                }
                myDialogFragment.dismiss();
                disCustomProgress();
            }
        });

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
            recyclerView.setVisibility(View.GONE);
        } else if (errorStatus == 1) {//数据为空
            llNetNotWork.setVisibility(View.GONE);
            llNotData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            llNetNotWork.setVisibility(View.GONE);
            llNotData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

}
