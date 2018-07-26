package com.ts.lys.yibei.ui.fragment;

import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.EventBean;
import com.ts.lys.yibei.bean.MarginAndProfitBean;
import com.ts.lys.yibei.bean.OpenTrader;
import com.ts.lys.yibei.bean.PendingOrder;
import com.ts.lys.yibei.bean.RealTimeBean;
import com.ts.lys.yibei.bean.RealTimeQuoteDatas;
import com.ts.lys.yibei.bean.SymbolInfo;
import com.ts.lys.yibei.constant.EventContents;
import com.ts.lys.yibei.customeview.AddDeleteView;
import com.ts.lys.yibei.customeview.ChooseTimesLayout;
import com.ts.lys.yibei.customeview.CustomPopWindow;
import com.ts.lys.yibei.customeview.KeyboardLayout;
import com.ts.lys.yibei.mvppresenter.TradePresenter;
import com.ts.lys.yibei.mvpview.ITradeOrPendingView;
import com.ts.lys.yibei.ui.activity.QuotationsActivity;
import com.ts.lys.yibei.utils.BaseUtils;
import com.ts.lys.yibei.utils.CalMarginAndProfitUtil;
import com.ts.lys.yibei.utils.CustomHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by jcdev1 on 2018/6/19.
 */

public class SimpleTradeFragment extends BaseFragment implements ITradeOrPendingView {
    @Bind(R.id.tv_free_margin)
    TextView tvFreeMargin;
    @Bind(R.id.add_delete_view)
    AddDeleteView addDeleteView;
    @Bind(R.id.choose_times_layout)
    ChooseTimesLayout chooseTimesLayout;


    QuotationsActivity parentActivity;


    /**
     * 当前货币的ask，bid值
     */
    private double[] currentCurrency = new double[]{0, 0};
    /**
     * 保证金计算需要：提供转换货币的ask，bid值
     */
    private double[] marginCalCurrency = new double[]{0, 0};

    private double transQuanty = 0.01;// TODO 步长，由后台决定

    private double lots = 0;//手数

    private int cmd = 0;//0:买涨  1：买跌
    /**
     * 计算保证金所需对象
     */
    private MarginAndProfitBean mapb = new MarginAndProfitBean();

    private SymbolInfo.DataBean.SymbolInfoBean symbolInfoBean;

    private TradePresenter tradePresenter = new TradePresenter(this);

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_simple_trade;
    }

    @Override
    protected void initBaseView() {
        initView();
        initListener();
    }

    private void initView() {
        tradePresenter.attachView(this);
        parentActivity = (QuotationsActivity) getActivity();
        addDeleteView.setScrollView(parentActivity.scrollView, parentActivity.llScrollContent, 0);

    }

    private void initListener() {
        addDeleteView.setOnAddDelClickLstener(new AddDeleteView.OnAddDelClickLstener() {
            @Override
            public void onAddClick() {
                double getnumber = addDeleteView.getnumber();
                getnumber += transQuanty;
                lots = getnumber;
                setMargin();
                addDeleteView.setnumber(getnumber);


            }

            @Override
            public void onDelClick() {
                double getnumber = addDeleteView.getnumber();
                getnumber -= transQuanty;
                lots = getnumber;
                setMargin();
                addDeleteView.setnumber(getnumber);

            }

            @Override
            public void onEditText(double lots) {
                SimpleTradeFragment.this.lots = lots;
                setMargin();
            }
        });


        chooseTimesLayout.setOnItemClickListener(new ChooseTimesLayout.OnItemClickListener() {
            @Override
            public void onItemClick(float times) {

                //此处需要判断选择的步长是否大于后台给定的步长，大于的话有效，否则无效
                if (times == 0) {
                    showToast("最低手数" + transQuanty + "手");
                } else {
                    addDeleteView.setnumber(times);
                    lots = times;
                    setMargin();
                }

            }
        });

    }

    /**
     * 软键盘状态
     *
     * @param state
     */
    public void setKeyboardStatus(int state) {
        if (state == KeyboardLayout.KEYBOARD_STATE_HIDE) {
            addDeleteView.setEditTextStatus(false);
        }
    }

    //************************************接收数据*********************************//

    /**
     * 接收顺序 1
     * <p>
     * 接收实时价格
     *
     * @param quoteBeen
     */
    public void setRealPrice(RealTimeBean quoteBeen) {
        if (getActivity() == null) return;

        if (symbolInfoBean != null) {

            if (quoteBeen.getSymbol().equals(symbolInfoBean.getMarginCalCurrency())) {
                marginCalCurrency[0] = quoteBeen.getAsk();
                marginCalCurrency[1] = quoteBeen.getBid();
            }

            if (quoteBeen.getSymbol().equals(parentActivity.symbol)) {
                currentCurrency[0] = quoteBeen.getAsk();
                currentCurrency[1] = quoteBeen.getBid();
            }
            setMargin();
        }


    }

    /**
     * 接收顺序 2
     * <p>
     * 品种保证金及止盈止损数据接口
     *
     * @param sb
     */
    public void setSymbolCalInfo(SymbolInfo.DataBean.SymbolInfoBean sb) {
        symbolInfoBean = sb;
        lots = sb.getMinVolume();
        transQuanty = sb.getMinVolume();
        tvFreeMargin.setText(getString(R.string.userful_margin) + " ：" + BaseUtils.getDigitsData(sb.getAccAmt(), 2));
        if (sb.getMinVolume() >= 1)
            addDeleteView.setLimit(sb.getMinVolume(), 10, 0);//TODO 第二个参数待定
        else
            addDeleteView.setLimit(sb.getMinVolume(), 10, 2);//TODO 第二个参数待定

        addDeleteView.setnumber(sb.getMinVolume());
        chooseTimesLayout.setBaseTimes(sb.getMinVolume());
    }

    /**
     * 接收顺序 3
     * <p>
     * 多个symbol的最新价格
     *
     * @param quote
     */
    public void setFirstRealTimePrice(List<RealTimeQuoteDatas.DataBean.QuoteBean> quote) {
        for (int i = 0; i < quote.size(); i++) {

            if (quote.get(i).getSymbol().equals(symbolInfoBean.getMarginCalCurrency())) {
                marginCalCurrency[0] = quote.get(i).getAsk();
                marginCalCurrency[1] = quote.get(i).getBid();
            }

            if (quote.get(i).getSymbol().equals(parentActivity.symbol)) {
                currentCurrency[0] = quote.get(i).getAsk();
                currentCurrency[1] = quote.get(i).getBid();
            }

        }
        setMargin();

    }
    //************************************提交数据*********************************//

    /**
     * 买入
     */
    public void buyIn() {
        showTradePop(getParameter(0), 0);
    }


    /**
     * 卖出
     */
    public void sellOut() {
        showTradePop(getParameter(1), 1);
    }

    @NonNull
    private String getParameter(int cmd) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getString(R.string.remind_two));
        buffer.append(cmd == 0 ? getString(R.string.purchase) : getString(R.string.sell_out));
        buffer.append(lots);
        buffer.append(getString(R.string.lots));
        buffer.append(" ");
        buffer.append(parentActivity.symbol);
        buffer.append(" ");
        buffer.append(getString(R.string.ma));
        return buffer.toString();
    }

    private void showTradePop(String title, final int cmd) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_trade_remind_layout, null);
        TextView tvTitle = contentView.findViewById(R.id.tv_title);
        TextView tvCancle = contentView.findViewById(R.id.tv_cancle);
        TextView tvConfirm = contentView.findViewById(R.id.tv_confirm);
        final CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
                .setView(contentView)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(0.7f)
                .create()
                .showAtLocation(parentActivity.keyboardLayout, Gravity.CENTER, 0, 0);
        tvTitle.setText(title);
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPopWindow.dissmiss();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPopWindow.dissmiss();
                //TODO 开仓
                Map<String, String> map = new HashMap<>();
                map.put("accessToken", accessToken);
                map.put("symbol", parentActivity.symbol);
                map.put("userId", userId);
                map.put("cmd", cmd + "");
                map.put("volume", lots + "");
                map.put("tp", "");
                map.put("sl", "");
                tradePresenter.openPosition(map, className + "1");
            }
        });

    }

    /**
     * 交易返回数据
     *
     * @param openTrader
     */
    @Override
    public void setTradeBackInfo(OpenTrader openTrader) {

        OpenTrader.DataBean.OpenTraderBean otb = openTrader.getData().getOpenTrader();
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_trade_back_info_layout, null);
        TextView tvSymbol = contentView.findViewById(R.id.tv_symbol);
        TextView tvDirection = contentView.findViewById(R.id.tv_direction);
        TextView tvOpenPri = contentView.findViewById(R.id.tv_open_price);
        TextView tvTradeTimes = contentView.findViewById(R.id.tv_trade_times);
        TextView tvStopLossPri = contentView.findViewById(R.id.tv_stop_loss_pri);
        TextView tvStopProfitPri = contentView.findViewById(R.id.tv_stop_profit_pri);
        TextView tvUserMargin = contentView.findViewById(R.id.tv_user_margin);
        TextView tvCancle = contentView.findViewById(R.id.tv_cancle);
        TextView tvSeeOrder = contentView.findViewById(R.id.tv_see_order);

        final CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
                .setView(contentView)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(0.7f)
                .create()
                .showAtLocation(parentActivity.keyboardLayout, Gravity.CENTER, 0, 0);

        tvSymbol.setText(otb.getSymbol());
        tvDirection.setText(otb.getCmd() == 0 ? getString(R.string.purchase) : getString(R.string.sell_out));
        tvOpenPri.setText(BaseUtils.getDigitsData(otb.getOpenPrice(), parentActivity.digits));
        tvTradeTimes.setText(BaseUtils.getDigitsData(otb.getVolume(), 2));
        tvStopLossPri.setText(otb.getSl() == 0 ? getString(R.string.not_setting) : BaseUtils.getDigitsData(otb.getSl(), parentActivity.digits));
        tvStopProfitPri.setText(otb.getTp() == 0 ? getString(R.string.not_setting) : BaseUtils.getDigitsData(otb.getTp(), parentActivity.digits));
        tvUserMargin.setText("$" + BaseUtils.getDigitsData(otb.getMargin(), 2));

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPopWindow.dissmiss();
            }
        });
        tvSeeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPopWindow.dissmiss();
                parentActivity.finish();
                EventBus.getDefault().post(new EventBean(EventContents.NEW_TRADING, null));
            }
        });
    }

    @Override
    public void setPendingBackInfo(PendingOrder pendingOrder) {

    }

    //************************************计算数据*********************************//


    /**
     * 计算保证金
     */
    private void setMargin() {
        mapb.setSymbolInfoBean(symbolInfoBean);
        mapb.setCurrentCurrency(currentCurrency);
        mapb.setMarginCalCurrency(marginCalCurrency);
        mapb.setLots(lots);
        mapb.setCmd(cmd);
        double margin = CalMarginAndProfitUtil.getMargin(mapb);
        addDeleteView.setTvExplain(getString(R.string.about_use_margin) + "：$ " + margin);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tradePresenter.detachView();
        CustomHttpUtils.cancelHttp(className + "1");
        CustomHttpUtils.cancelHttp(className + "2");
    }
}
