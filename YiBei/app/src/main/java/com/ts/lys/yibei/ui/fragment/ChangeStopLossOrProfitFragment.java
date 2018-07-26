package com.ts.lys.yibei.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.MarginAndProfitBean;
import com.ts.lys.yibei.bean.OrderPositionModel;
import com.ts.lys.yibei.bean.RealTimeBean;
import com.ts.lys.yibei.bean.SymbolInfo;
import com.ts.lys.yibei.constant.UrlContents;
import com.ts.lys.yibei.customeview.AddDeleteView;
import com.ts.lys.yibei.ui.activity.PositionDetailActivity;
import com.ts.lys.yibei.utils.BaseUtils;
import com.ts.lys.yibei.utils.CalMarginAndProfitUtil;
import com.ts.lys.yibei.utils.CustomHttpUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

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
    private PositionDetailActivity parentActivity;
    private OrderPositionModel.DataBean.TraderOrderBean tb;
    public SymbolInfo.DataBean.SymbolInfoBean symbolInfoBean;
    private MarginAndProfitBean mapb = new MarginAndProfitBean();
    /**
     * 软键盘是否出现
     */
    private boolean isShowKeybord = false;


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

        parentActivity = (PositionDetailActivity) getActivity();
        tb = parentActivity.tb;
        symbolInfoBean = parentActivity.sbinfo;

        tvSymbolCn.setText(tb.getSymbolCn());
        tvSymbolEn.setText(tb.getSymbolEn());
        if (tb.getCmd() == 0)
            ivStatus.setImageResource(R.mipmap.buy_icon);
        else
            ivStatus.setImageResource(R.mipmap.sell_icon);
        tvLots.setText(String.valueOf(tb.getVolume()));

        tvOpenPrice.setText(BaseUtils.getDigitsData(tb.getOpenPrice(), parentActivity.digits));
        tvCurrentPrice.setText(BaseUtils.getDigitsData(tb.getMarket(), parentActivity.digits));


        /**
         *止损价格
         */
        stopLossAdv.setLimit(0, 0, tb.getDigits());
        stopLossAdv.setIsStopLossOrProfit(true);

        if (tb.getSl() != 0)
            stopLossAdv.setnumber(tb.getSl());

        /**
         *止盈价格
         */
        stopProfitAdv.setLimit(0, 0, parentActivity.digits);
        stopProfitAdv.setIsStopLossOrProfit(true);
        if (tb.getTp() != 0)
            stopProfitAdv.setnumber(tb.getTp());


    }


    private void initListener() {

        stopLossAdv.et_number.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setEditTextStatus(true, 1);
                return false;
            }
        });

        stopProfitAdv.et_number.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setEditTextStatus(true, 2);
                return false;
            }
        });

        stopLossAdv.setOnAddDelClickLstener(new AddDeleteView.OnAddDelClickLstener() {
            @Override
            public void onAddClick() {
                setEditTextStatus(false, 0);

                if (stopLossAdv.getnumber() != 0) {
                    double getnumber = stopLossAdv.getnumber();
                    getnumber += parentActivity.stopLossLever;
                    stopLossAdv.setnumber(getnumber);
                    initstopLossLimit();
                } else {
                    double stopLossLimit = CalMarginAndProfitUtil.stopLossOrprofitLimit(parentActivity.currentCurrencyArray, symbolInfoBean.getStopsLevel(), parentActivity.digits, tb.getCmd(), 0);
                    if (tb.getCmd() == 0)
                        stopLossAdv.setLimit(0, stopLossLimit, parentActivity.digits);
                    else
                        stopLossAdv.setLimit(stopLossLimit, 10000000, parentActivity.digits);
                    stopLossAdv.setnumber(stopLossLimit);
                }
            }

            @Override
            public void onDelClick() {
                setEditTextStatus(false, 0);

                if (stopLossAdv.getnumber() != 0) {
                    double getnumber = stopLossAdv.getnumber();
                    getnumber -= parentActivity.stopLossLever;
                    stopLossAdv.setnumber(getnumber);
                    initstopLossLimit();
                } else {
                    double stopLossLimit = CalMarginAndProfitUtil.stopLossOrprofitLimit(parentActivity.currentCurrencyArray, symbolInfoBean.getStopsLevel(), parentActivity.digits, tb.getCmd(), 0);
                    if (tb.getCmd() == 0)
                        stopLossAdv.setLimit(0, stopLossLimit, parentActivity.digits);
                    else
                        stopLossAdv.setLimit(stopLossLimit, 10000000, parentActivity.digits);
                    stopLossAdv.setnumber(stopLossLimit);
                }
            }

            @Override
            public void onEditText(double lots) {
                if (stopLossAdv.getnumber() != 0) {
                    initstopLossLimit();

                }
            }
        });

        stopProfitAdv.setOnAddDelClickLstener(new AddDeleteView.OnAddDelClickLstener() {
            @Override
            public void onAddClick() {
                setEditTextStatus(false, 0);

                if (stopProfitAdv.getnumber() != 0) {
                    double getnumber = stopProfitAdv.getnumber();
                    getnumber += parentActivity.stopProfitLever;
                    stopProfitAdv.setnumber(getnumber);
                    initStopProfitLimit();
                } else {
                    double stopProfitLimit = CalMarginAndProfitUtil.stopLossOrprofitLimit(parentActivity.currentCurrencyArray, symbolInfoBean.getStopsLevel(), parentActivity.digits, tb.getCmd(), 1);
                    if (tb.getCmd() == 0)
                        stopProfitAdv.setLimit(stopProfitLimit, 10000000, parentActivity.digits);
                    else
                        stopProfitAdv.setLimit(0, stopProfitLimit, parentActivity.digits);
                    stopProfitAdv.setnumber(stopProfitLimit);
                }
            }

            @Override
            public void onDelClick() {
                setEditTextStatus(false, 0);

                if (stopProfitAdv.getnumber() != 0) {
                    double getnumber = stopProfitAdv.getnumber();
                    getnumber -= parentActivity.stopProfitLever;
                    stopProfitAdv.setnumber(getnumber);
                    initStopProfitLimit();
                } else {
                    double stopProfitLimit = CalMarginAndProfitUtil.stopLossOrprofitLimit(parentActivity.currentCurrencyArray, symbolInfoBean.getStopsLevel(), parentActivity.digits, tb.getCmd(), 1);
                    if (tb.getCmd() == 0)
                        stopProfitAdv.setLimit(stopProfitLimit, 10000000, parentActivity.digits);
                    else
                        stopProfitAdv.setLimit(0, stopProfitLimit, parentActivity.digits);
                    stopProfitAdv.setnumber(stopProfitLimit);
                }
            }

            @Override
            public void onEditText(double lots) {
                if (stopProfitAdv.getnumber() != 0)
                    initStopProfitLimit();
            }
        });

    }

    /**
     * 设置文本框状态
     *
     * @param state
     */
    private void setEditTextStatus(boolean state, int position) {
        if (!state) {
            isShowKeybord = false;
            stopLossAdv.setEditTextStatus(false);
            stopProfitAdv.setEditTextStatus(false);
        } else {
            if (position == 1)
                stopLossAdv.setEditTextStatus(true);
            if (position == 2)
                stopProfitAdv.setEditTextStatus(true);
            isShowKeybord = true;
        }
    }

    @OnClick({R.id.tv_cancle, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancle:
                dismiss();
                break;
            case R.id.tv_confirm:
                double getnumber = stopLossAdv.getnumber();
                double getnumber1 = stopProfitAdv.getnumber();
                if (tb.getSl() != getnumber || tb.getTp() != getnumber1) {
                    changStopLossOrProfit(getnumber, getnumber1);
                } else
                    parentActivity.showToast(getString(R.string.do_nothing));
                break;
        }
    }


    /**
     * 实时数据
     *
     * @param realTimeBeanAll
     */
    public void setRealPrice(RealTimeBean realTimeBeanAll) {
        if (tvCurrentPrice == null) return;
        if (tb.getCmd() == 0)
            tvCurrentPrice.setText(BaseUtils.getDigitsData(realTimeBeanAll.getBid(), parentActivity.digits));
        else
            tvCurrentPrice.setText(BaseUtils.getDigitsData(realTimeBeanAll.getAsk(), parentActivity.digits));

        initstopLossLimit();
        initStopProfitLimit();
    }


    /**
     * 初始化止损价格列数据展示
     */
    private void initstopLossLimit() {

        double stopLossLimit = CalMarginAndProfitUtil.stopLossOrprofitLimit(parentActivity.currentCurrencyArray, symbolInfoBean.getStopsLevel(), parentActivity.digits, tb.getCmd(), 0);
        if (tb.getCmd() == 0) {
            stopLossAdv.setLimit(0, stopLossLimit, parentActivity.digits);
        } else {
            stopLossAdv.setLimit(stopLossLimit, 10000000, parentActivity.digits);
        }

        /**
         * 如果加减后的价格不在范围内则强制改变输入框的价格（注意：是点击加号和减号的处理逻辑）
         *
         * 如果软键盘出现，即手动输入时不允许强制修改输入框里的内容
         */

        if (!isShowKeybord)
            switch (tb.getCmd()) {
                case 0:
                    if (stopLossAdv.getnumber() > stopLossLimit) {
                        if (stopLossAdv.getnumber() == 0) break;
                        stopLossAdv.setnumber(stopLossLimit);
                    }
                    break;
                case 1:
                    if (stopLossAdv.getnumber() < stopLossLimit) {
                        if (stopLossAdv.getnumber() == 0) break;
                        stopLossAdv.setnumber(stopLossLimit);
                    }
                    break;
            }
        mapb.setSymbolInfoBean(symbolInfoBean);
        mapb.setLots(tb.getVolume());
        mapb.setCmd(tb.getCmd());
        mapb.setProfitCalCurrency(parentActivity.profitCalCurrencyArray);
        mapb.setOpenPrice(tb.getOpenPrice());
        mapb.setClosePrice(stopLossAdv.getnumber());
        double profit = CalMarginAndProfitUtil.getProfit(mapb);

        String explain;
        if (tb.getCmd() == 0)
            explain = getString(R.string.price) + "<" + BaseUtils.getDigitsData(stopLossLimit, parentActivity.digits) + "  " + getString(R.string.expected_loss) + "：" + (stopLossAdv.getnumber() == 0 ? "$0" : BaseUtils.dealSymbol(profit));
        else
            explain = getString(R.string.price) + ">" + BaseUtils.getDigitsData(stopLossLimit, parentActivity.digits) + "  " + getString(R.string.expected_loss) + "：" + (stopLossAdv.getnumber() == 0 ? "$0" : BaseUtils.dealSymbol(profit));
        stopLossAdv.setTvExplain(explain);
    }


    /**
     * 初始化止盈列表数据展示
     */
    private void initStopProfitLimit() {

        double stopProfitLimit = CalMarginAndProfitUtil.stopLossOrprofitLimit(parentActivity.currentCurrencyArray, symbolInfoBean.getStopsLevel(), parentActivity.digits, tb.getCmd(), 1);
        if (tb.getCmd() == 0)
            stopProfitAdv.setLimit(stopProfitLimit, 10000000, parentActivity.digits);
        else
            stopProfitAdv.setLimit(0, stopProfitLimit, parentActivity.digits);

        /**
         * 如果加减后的价格不在范围内则强制改变输入框的价格（注意：是点击加号和减号的处理逻辑）
         *
         * 如果软键盘出现，即手动输入时不允许强制修改输入框里的内容
         */
        if (!isShowKeybord)
            switch (tb.getCmd()) {
                case 0:
                    if (stopProfitAdv.getnumber() < stopProfitLimit) {
                        if (stopProfitAdv.getnumber() == 0) break;
                        stopProfitAdv.setnumber(stopProfitLimit);
                    }
                    break;
                case 1:
                    if (stopProfitAdv.getnumber() > stopProfitLimit) {
                        if (stopProfitAdv.getnumber() == 0) break;
                        stopProfitAdv.setnumber(stopProfitLimit);
                    }
                    break;
            }
        mapb.setSymbolInfoBean(symbolInfoBean);
        mapb.setLots(tb.getVolume());
        mapb.setCmd(tb.getCmd());
        mapb.setProfitCalCurrency(parentActivity.profitCalCurrencyArray);
        mapb.setOpenPrice(tb.getOpenPrice());
        mapb.setClosePrice(stopProfitAdv.getnumber());
        double profit = CalMarginAndProfitUtil.getProfit(mapb);

        String explain;
        if (tb.getCmd() == 0)
            explain = getString(R.string.price) + ">" + BaseUtils.getDigitsData(stopProfitLimit, parentActivity.digits) + "  " + getString(R.string.expected_profit) + "：" + (stopProfitAdv.getnumber() == 0 ? "$0" : BaseUtils.dealSymbol(profit));
        else
            explain = getString(R.string.price) + "<" + BaseUtils.getDigitsData(stopProfitLimit, parentActivity.digits) + "  " + getString(R.string.expected_profit) + "：" + (stopProfitAdv.getnumber() == 0 ? "$0" : BaseUtils.dealSymbol(profit));
        stopProfitAdv.setTvExplain(explain);
    }


    /**
     * 修改止盈止损
     */
    private void changStopLossOrProfit(final double stopLoss, final double stopProfit) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", parentActivity.userId);
        map.put("accessToken", parentActivity.accessToken);
        map.put("ticket", tb.getTicket());
        map.put("tp", String.valueOf(stopProfit));
        map.put("sl", String.valueOf(stopLoss));
        map.put("cmd", String.valueOf(tb.getCmd()));

        parentActivity.showCustomProgress();
        CustomHttpUtils.getServiceDatas(map, UrlContents.TRADER_UPDATE, "csoptFragment", new CustomHttpUtils.ServiceStatus() {
            @Override
            public void faild(Call call, Exception e, int id) {
                parentActivity.disCustomProgress();
                parentActivity.showToast(getString(R.string.net_error));
            }

            @Override
            public void success(String response, int id) {
                parentActivity.disCustomProgress();
                if (response != null) {
                    JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                    String errCode = "";
                    String errMsg = "";
                    if (!jsonObject.get("err_code").isJsonNull()) {
                        errCode = jsonObject.get("err_code").getAsString();
                    }
                    if (!jsonObject.get("err_msg").isJsonNull())
                        errMsg = jsonObject.get("err_msg").getAsString();
                    if (errCode.equals("0")) {
                        parentActivity.changeStopLossOrProfit(stopLoss, stopProfit);
                        ChangeStopLossOrProfitFragment.this.dismiss();
                    } else
                        parentActivity.showToast(errMsg);


                }

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
