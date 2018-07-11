package com.ts.lys.yibei.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.ui.activity.ChooseBrokerActivity;
import com.ts.lys.yibei.ui.activity.LoginActivity;
import com.ts.lys.yibei.ui.activity.WebViewActivity;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jcdev1 on 2018/6/8.
 */

public class MineFragment extends BaseFragment {

    @Bind(R.id.tv_account_equity)
    TextView tvAccountEquity;
    @Bind(R.id.tv_asset_balance)
    TextView tvAssetBalance;
    @Bind(R.id.tv_profit_rate)
    TextView tvProfitRate;
    @Bind(R.id.include_no_account)
    AutoLinearLayout includeNoAccount;
    @Bind(R.id.tv_account_style)
    TextView tvAccountStyle;
    @Bind(R.id.include_have_account)
    AutoLinearLayout includeHaveAccount;
    @Bind(R.id.tv_bi)
    TextView tvBi;
    @Bind(R.id.tv_lots)
    TextView tvLots;
    @Bind(R.id.tv_cumulative_income)
    TextView tvCumulativeIncome;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initBaseView() {


    }

    @OnClick({R.id.iv_customer, R.id.tv_open_real_account, R.id.ll_choose_account, R.id.ll_manager_account,
            R.id.btn_open_new_account, R.id.iv_head, R.id.ll_data_statis, R.id.ll_trade_report, R.id.ll_help_center, R.id.ll_feedback, R.id.ll_about_yibei})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_customer:
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
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.ll_data_statis:
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "https://rili-d.jin10.com/open.php");
                startActivity(intent);
                break;
            case R.id.ll_trade_report:
                break;
            case R.id.ll_help_center:
                break;
            case R.id.ll_feedback:
                break;
            case R.id.ll_about_yibei:
                break;
        }
    }
}
