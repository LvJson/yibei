package com.ts.lys.yibei.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.adapter.AccountChooseAdapter;
import com.ts.lys.yibei.bean.AccountBean;
import com.ts.lys.yibei.bean.BrokerCheckListBean;
import com.ts.lys.yibei.bean.EventBean;
import com.ts.lys.yibei.bean.UserAccInfoModel;
import com.ts.lys.yibei.constant.EventContents;
import com.ts.lys.yibei.customeview.CustomPopWindow;
import com.ts.lys.yibei.mvppresenter.AccInfoPresenter;
import com.ts.lys.yibei.mvpview.IMineFragmentView;
import com.ts.lys.yibei.ui.activity.AccountLoginActivity;
import com.ts.lys.yibei.ui.activity.ChooseBrokerActivity;
import com.ts.lys.yibei.ui.activity.PersonInfoActivity;
import com.ts.lys.yibei.ui.activity.TradeReportActivity;
import com.ts.lys.yibei.ui.activity.WebViewActivity;
import com.ts.lys.yibei.utils.AppUtils;
import com.ts.lys.yibei.utils.BaseUtils;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jcdev1 on 2018/6/8.
 */

public class MineFragment extends BaseFragment implements IMineFragmentView {

    @Bind(R.id.tv_account_equity)
    TextView tvAccountEquity;
    @Bind(R.id.tv_asset_balance)
    TextView tvAssetBalance;
    @Bind(R.id.tv_profit_rate)
    TextView tvProfitRate;
    @Bind(R.id.tv_account_style)
    TextView tvAccountStyle;


    @Bind(R.id.include_no_account)
    AutoLinearLayout includeNoAccount;
    @Bind(R.id.include_have_account)
    AutoLinearLayout includeHaveAccount;
    @Bind(R.id.ll_have_real_account)
    LinearLayout llHaverealAccount;


    @Bind(R.id.tv_bi)
    TextView tvBi;
    @Bind(R.id.tv_lots)
    TextView tvLots;
    @Bind(R.id.tv_cumulative_income)
    TextView tvCumulativeIncome;
    @Bind(R.id.ll_father)
    LinearLayout llFather;
    @Bind(R.id.tv_account_num)
    TextView tvAccountNum;

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private AccInfoPresenter presenter = new AccInfoPresenter(this);

    private UserAccInfoModel.DataBean accountInfo;
    private BrokerCheckListBean.DataBean accList;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initBaseView() {
        initData();
        initListener();

    }


    private void initData() {
        if (TextUtils.isEmpty(userId)) return;
        presenter.attachView(this);
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("accessToken", accessToken);
        presenter.getAccInfo(map, className);

        presenter.getAccountList(map, className + "3");


    }

    private void initListener() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

    }

    @OnClick({R.id.iv_customer, R.id.tv_open_real_account, R.id.ll_choose_account, R.id.ll_manager_account,
            R.id.btn_open_new_account, R.id.iv_head, R.id.ll_data_statis, R.id.ll_trade_report, R.id.ll_help_center, R.id.ll_feedback, R.id.ll_about_yibei})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_customer:
                if (accList != null)
                    switchAccount();
                break;
            case R.id.tv_open_real_account:
                startActivity(new Intent(getActivity(), ChooseBrokerActivity.class));
                break;
            case R.id.ll_choose_account:
                break;
            case R.id.ll_manager_account:
                break;
            case R.id.btn_open_new_account:
                break;
            case R.id.iv_head:
                startActivity(new Intent(getActivity(), AccountLoginActivity.class));
                break;
            case R.id.ll_data_statis:
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "https://rili-d.jin10.com/open.php");
                startActivity(intent);
                break;
            case R.id.ll_trade_report:
                startActivity(new Intent(getActivity(), TradeReportActivity.class));
                break;
            case R.id.ll_help_center:
                break;
            case R.id.ll_feedback:
                startActivity(new Intent(getActivity(), PersonInfoActivity.class));
                break;
            case R.id.ll_about_yibei:
                break;
        }
    }

    /**
     * 切换账户
     */
    private void switchAccount() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_switch_account_layout, null);
        AccountChooseAdapter adapter = new AccountChooseAdapter(getContext());
        RecyclerView recyclerView = contentView.findViewById(R.id.recycler_view);
        TextView tvCancel = contentView.findViewById(R.id.tv_cancel);
        LinearLayout llDemoAcc = contentView.findViewById(R.id.ll_demo_acc);
        TextView tvExplain = contentView.findViewById(R.id.tv_explain);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        List<AccountBean> abList = new ArrayList<>();


        final CustomPopWindow popupWindowBuilder = new CustomPopWindow.PopupWindowBuilder(getContext())
                .setView(contentView)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(0.7f)
                .size(BaseUtils.getScreenWidth(getContext()), ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.mypopwindow_anim_style)
                .create()
                .showAtLocation(llFather, Gravity.BOTTOM, 0, 0);
        String explian = "";
        if (accList.getSmart().getStatus() == 0)
            explian = accountInfo.getDestroyDay() + getString(R.string.have_maturity);
        else
            explian = getString(R.string.maturity) + "：" + accList.getSmart().getDestory();
        tvExplain.setText(explian);


        llDemoAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (accList.getSmart().getStatus() == 0) {
                    showToast(getString(R.string.have_maturity));
                } else {
                    if (!accList.getSmart().getNow())
                        switchAccount(accList.getSmart().getAccType());
                }
                popupWindowBuilder.dissmiss();
            }
        });

        adapter.setData(accList.getBroker());
        adapter.setOnItemClickListener(new AccountChooseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BrokerCheckListBean.DataBean.BrokerBean ab) {
                if (ab.getStatus() == 0) {
                    showToast(getString(R.string.have_maturity));
                } else if (!ab.getNow()) {
                    MineFragment.this.switchAccount(ab.getAccType());
                }
                popupWindowBuilder.dissmiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindowBuilder.dissmiss();
            }
        });


    }

    /**
     * 切换账户
     *
     * @param accType
     */
    private void switchAccount(int accType) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("accessToken", accessToken);
        map.put("accType", String.valueOf(accType));
        presenter.getSwitchAccBackInfo(map, className + "2");
    }


    /**
     * 账户基本信息
     *
     * @param accountInfo
     */
    @Override
    public void showAccountInfo(UserAccInfoModel.DataBean accountInfo) {

        if (accountInfo != null) {
            this.accountInfo = accountInfo;
            String phone = accountInfo.getTelephone();
            tvAccountNum.setText(phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length()));
            tvAccountEquity.setText(BaseUtils.dealSymbol(accountInfo.getEquity()));
            tvProfitRate.setText(AppUtils.getDigitsData(accountInfo.getYieldRate(), 2) + "%");
            tvBi.setText(String.valueOf(accountInfo.getTotalCount()));
            tvLots.setText(BaseUtils.getDigitsData(accountInfo.getTotalVolume(), 2));
            tvCumulativeIncome.setText(BaseUtils.dealSymbol(accountInfo.getProfitCount()));

        }

        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }


    /**
     * 切换账户返回信息
     *
     * @param state
     */
    @Override
    public void showSwitchState(boolean state) {
        if (state == true)
            EventBus.getDefault().post(new EventBean(EventContents.ALL_MAIN_REFRESH, null));

    }

    /**
     * 账户列表
     *
     * @param dataBean
     */
    @Override
    public void showAccountList(BrokerCheckListBean.DataBean dataBean) {
        if (dataBean != null)
            this.accList = dataBean;

    }

    /**
     * 刷新数据
     */
    public void refreshData() {

        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("accessToken", accessToken);
        presenter.getAccInfo(map, className);
        presenter.getAccountList(map, className + "3");

    }

    @Override
    public void showToast(String content) {
        super.showToast(content);
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.detachView();
        CustomHttpUtils.cancelHttp(className);
        CustomHttpUtils.cancelHttp(className + "2");
        CustomHttpUtils.cancelHttp(className + "3");

    }
}
