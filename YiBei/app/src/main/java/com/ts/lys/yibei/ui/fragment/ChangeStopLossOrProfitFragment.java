package com.ts.lys.yibei.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.customeview.AddDeleteView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jcdev1 on 2018/7/17.
 */

public class ChangeStopLossOrProfitFragment extends DialogFragment {
    @Bind(R.id.tv_symbol_en)
    TextView tvSymbolEn;
    @Bind(R.id.iv_status)
    ImageView ivStatus;
    @Bind(R.id.tv_lots)
    TextView tvLots;
    @Bind(R.id.tv_symbol_cn)
    TextView tvSymbolCn;
    @Bind(R.id.tv_open_price)
    TextView tvOpenPrice;
    @Bind(R.id.tv_current_price)
    TextView tvCurrentPrice;
    @Bind(R.id.stop_loss_adv)
    AddDeleteView stopLossAdv;
    @Bind(R.id.stop_profit_adv)
    AddDeleteView stopProfitAdv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_change_stop_loss_profit, container);
        ButterKnife.bind(this, view);

        initView();
        initListener();
        return view;
    }

    private void initView() {

        stopLossAdv.setLimit(0, 0, 5);
        stopLossAdv.setnumber(0.12345);
        stopLossAdv.setIsStopLossOrProfit(true);

    }


    private void initListener() {

        stopLossAdv.setOnAddDelClickLstener(new AddDeleteView.OnAddDelClickLstener() {
            @Override
            public void onAddClick() {

            }

            @Override
            public void onDelClick() {

            }

            @Override
            public void onEditText(double lots) {

            }
        });

        stopProfitAdv.setOnAddDelClickLstener(new AddDeleteView.OnAddDelClickLstener() {
            @Override
            public void onAddClick() {

            }

            @Override
            public void onDelClick() {

            }

            @Override
            public void onEditText(double lots) {

            }
        });

    }

    @OnClick({R.id.tv_cancle, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancle:
                dismiss();
                break;
            case R.id.tv_confirm:
                dismiss();
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
