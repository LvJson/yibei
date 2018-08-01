package com.ts.lys.yibei.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ts.lys.yibei.R;
import com.ts.lys.yibei.adapter.OrderPositionAdapter;
import com.ts.lys.yibei.bean.CloseTraderModel;
import com.ts.lys.yibei.bean.MarginAndProfitBean;
import com.ts.lys.yibei.bean.OrderPositionModel;
import com.ts.lys.yibei.bean.RealTimeBean;
import com.ts.lys.yibei.bean.RealTimeQuoteDatas;
import com.ts.lys.yibei.bean.SymbolInfo;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.customeview.CustomPopWindow;
import com.ts.lys.yibei.mvppresenter.PositionListPresenter;
import com.ts.lys.yibei.mvppresenter.RealTimeDataPresenter;
import com.ts.lys.yibei.mvpview.IPositionFragmentView;
import com.ts.lys.yibei.mvpview.IRealTimeView;
import com.ts.lys.yibei.ui.activity.PositionDetailActivity;
import com.ts.lys.yibei.utils.Arith;
import com.ts.lys.yibei.utils.BaseUtils;
import com.ts.lys.yibei.utils.CalMarginAndProfitUtil;
import com.ts.lys.yibei.utils.CustomHttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jcdev1 on 2018/7/5.
 */

public class PositionFragment extends BaseFragment implements IPositionFragmentView, IRealTimeView {
    @Bind(R.id.x_recycler)
    XRecyclerView xRecycler;
    @Bind(R.id.ll_father)
    LinearLayout llFather;
    @Bind(R.id.ll_net_not_work)
    LinearLayout llNetNotWork;
    @Bind(R.id.ll_not_data)
    LinearLayout llNotData;

    private OrderPositionAdapter adapter;
    private PositionListPresenter presenter = new PositionListPresenter(this);
    private RealTimeDataPresenter realTimeDataPresenter = new RealTimeDataPresenter(this);
    /**
     * 持仓列表
     */
    List<OrderPositionModel.DataBean.TraderOrderBean> traderOrderBeanList;

    /**
     * 记录出现交叉盘时需换算的货币对的实时价格
     */
    Map<String, double[]> pccMap = new HashMap<>();
    /**
     * 默认的交叉盘价格
     */
    double[] profitCalCurrency = new double[]{0, 0};

    private OrderFragment parentFragment;


    @Override

    protected int getLayoutID() {
        return R.layout.fragment_position;
    }

    @Override
    protected void initBaseView() {

        initView();
        initListener();
        initData();
    }

    private void initView() {

        parentFragment = (OrderFragment) getParentFragment();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        xRecycler.setLayoutManager(manager);
        xRecycler.setLoadingMoreEnabled(false);
        xRecycler.setPullRefreshEnabled(false);
        xRecycler.setAdapter(adapter = new OrderPositionAdapter(getActivity()));
    }


    private void initListener() {

        adapter.setOnItemClickListener(new OrderPositionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(OrderPositionModel.DataBean.TraderOrderBean tb) {

                Intent intent = new Intent(getActivity(), PositionDetailActivity.class);
                intent.putExtra("symbol", tb.getSymbolEn());
                intent.putExtra("symbolCn", tb.getSymbolCn());
                intent.putExtra("digits", tb.getDigits());
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", tb);
                intent.putExtras(bundle);
                startActivity(intent);

            }

            @Override
            public void onClosePositionClick(final OrderPositionModel.DataBean.TraderOrderBean tb) {
                showClosePositionPop(tb);

            }
        });
    }

    /**
     * 展示平仓确定弹框
     *
     * @param tb
     */
    private void showClosePositionPop(final OrderPositionModel.DataBean.TraderOrderBean tb) {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_close_position_remind_layout, null);
        TextView tv_order_num = contentView.findViewById(R.id.tv_order_num);
        TextView tv_expected_profit_loss = contentView.findViewById(R.id.tv_expected_profit_loss);
        TextView tv_open_position_pri = contentView.findViewById(R.id.tv_open_position_pri);
        TextView tv_current_price = contentView.findViewById(R.id.tv_current_price);
        TextView tv_cancle = contentView.findViewById(R.id.tv_cancle);
        TextView tv_confirm = contentView.findViewById(R.id.tv_confirm);
        final CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
                .setView(contentView)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(0.7f)
                .create()
                .showAtLocation(llFather, Gravity.CENTER, 0, 0);

        tv_order_num.setText(tb.getTicket());
        tv_expected_profit_loss.setText(BaseUtils.dealSymbol(tb.getProfit()));
        if (tb.getProfit() < 0)
            tv_expected_profit_loss.setTextColor(getResources().getColor(R.color.fall_color));
        else
            tv_expected_profit_loss.setTextColor(getResources().getColor(R.color.rise_color));

        tv_open_position_pri.setText(BaseUtils.getDigitsData(tb.getOpenPrice(), tb.getDigits()));
        if (tb.getCmd() == 0)
            tv_current_price.setText(BaseUtils.getDigitsData(tb.getBid(), tb.getDigits()));
        else
            tv_current_price.setText(BaseUtils.getDigitsData(tb.getAsk(), tb.getDigits()));
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPopWindow.dissmiss();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPopWindow.dissmiss();
                Map<String, String> map = new HashMap<>();
                map.put("ticket", tb.getTicket());
                map.put("userId", userId);
                map.put("accessToken", accessToken);
                map.put("symbol", tb.getSymbolEn());
                map.put("volume", String.valueOf(tb.getVolume()));
                map.put("cmd", String.valueOf(tb.getCmd()));
                presenter.getClosePositionBackInfo(map, className + "3");

            }
        });
    }

    private void initData() {

        presenter.attachView(this);
        realTimeDataPresenter.attachView(this);
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("accessToken", accessToken);
        presenter.getPositionList(map, className + "1");
    }

    /**
     * 实时数据
     *
     * @param realTimeBean
     */

    //此处三个本来是局部变量的对象放在外面是避免频繁创建和销毁给内存造成压力
    MarginAndProfitBean mapb = new MarginAndProfitBean();
    SymbolInfo.DataBean.SymbolInfoBean sb = new SymbolInfo.DataBean.SymbolInfoBean();
    double[] two = new double[4];

    public void setRealData(RealTimeBean realTimeBean) {

        if (realTimeBean != null && pccMap.size() > 0 && traderOrderBeanList != null && traderOrderBeanList.size() > 0) {

            double profitSum = 0;//浮动盈亏总和
            for (int i = 0; i < traderOrderBeanList.size(); i++) {
                OrderPositionModel.DataBean.TraderOrderBean tb = traderOrderBeanList.get(i);
                //1、计算浮动盈亏等实时数据
                //2、实时更新持仓列表
                if (realTimeBean.getSymbol().equals(tb.getSymbolEn())) {
                    if (realTimeBean.getAsk() != tb.getAsk() || realTimeBean.getBid() != tb.getBid() || realTimeBean.getMarket() != tb.getMarket()) {
                        sb.setContractSize(tb.getContractSize());
                        sb.setProfitChange(tb.isProfitChange());
                        sb.setProfitUSDPrex(tb.isProfitUSDPrex());
                        mapb.setSymbolInfoBean(sb);
                        mapb.setLots(tb.getVolume());
                        mapb.setCmd(tb.getCmd());
                        if (pccMap.containsKey(tb.getProfitCalCurrency()))
                            mapb.setProfitCalCurrency(pccMap.get(tb.getProfitCalCurrency()));
                        else
                            mapb.setProfitCalCurrency(profitCalCurrency);
                        mapb.setOpenPrice(tb.getOpenPrice());
                        if (tb.getCmd() == 0)
                            mapb.setClosePrice(realTimeBean.getBid());
                        else
                            mapb.setClosePrice(realTimeBean.getAsk());

                        double floatPrice = CalMarginAndProfitUtil.getProfit(mapb);
                        double fee = Arith.add(tb.getSwaps(), tb.getCommission());
                        double realProfit = Arith.add(floatPrice, fee);

                        two[0] = realTimeBean.getAsk();
                        two[1] = realTimeBean.getBid();
                        two[2] = realTimeBean.getMarket();
                        two[3] = realProfit;
                        adapter.upData(i, realTimeBean.getSymbol(), two);

                        //修改当前对象的浮动盈亏
                        tb.setProfit(floatPrice);

                    }

                }

                /**********************************计算订单界面顶部所需数据*******************************************/
                double fee = Arith.add(tb.getSwaps(), tb.getCommission());
                double realProfit = Arith.add(tb.getProfit(), fee);
                profitSum = Arith.add(profitSum, realProfit);
            }
            ((OrderFragment) getParentFragment()).updataHeadData(profitSum);

        }

    }

    /**
     * 持仓列表
     *
     * @param traderOrderBeanList
     */
    @Override
    public void setPositionList(List<OrderPositionModel.DataBean.TraderOrderBean> traderOrderBeanList) {
        parentFragment.setRefleshEnable(false);
        if (traderOrderBeanList == null || traderOrderBeanList.size() == 0) {
            setErrorStatus(1);
            this.traderOrderBeanList = null;
            return;
        } else
            setErrorStatus(2);

        this.traderOrderBeanList = traderOrderBeanList;
        adapter.setData(traderOrderBeanList);
        /**
         * 先强行获取一次所有品种的实时价格，只要是交叉盘需要换算的货币对价格，以免计算浮动盈亏出现错误
         */

        //交叉盘品种名称
        String symbolMulti = "";

        for (int i = 0; i < traderOrderBeanList.size(); i++) {
            if (i == 0)
                symbolMulti = traderOrderBeanList.get(i).getProfitCalCurrency();
            else
                symbolMulti += "," + traderOrderBeanList.get(i).getProfitCalCurrency();

        }
        if (TextUtils.isEmpty(symbolMulti)) return;

        Map<String, String> map = new HashMap<>();
        map.put("symbol", symbolMulti);
        realTimeDataPresenter.getRealTimeQuotoDatas(map, className + "2");

    }


    /**
     * 平仓返回结果
     *
     * @param closeTraderBean
     */
    @Override
    public void setClosePositionBackInfo(CloseTraderModel.DataBean.CloseTraderBean closeTraderBean) {
        refreshData();
        parentFragment.setCurrentPosition(2);

    }

    /**
     * 先强行获取一次所需品种的实时价格
     *
     * @param realTimeQuoteDatas
     */
    @Override
    public void showRealTimeData(RealTimeQuoteDatas realTimeQuoteDatas) {

        List<RealTimeQuoteDatas.DataBean.QuoteBean> quote = realTimeQuoteDatas.getData().getQuote();
        if (quote != null && quote.size() > 0) {
            for (int i = 0; i < quote.size(); i++) {
                double[] one = new double[]{quote.get(i).getAsk(), quote.get(i).getBid(), quote.get(i).getMarket()};
                pccMap.put(quote.get(i).getSymbol(), one);
            }
        }
    }

    @Override
    public void showToast(String content) {
        super.showToast(content);
        parentFragment.setRefleshEnable(false);
        if (content.equals(BaseContents.NET_ERROR))
            setErrorStatus(0);
    }

    @OnClick(R.id.tv_reload)
    public void onViewClicked() {
        refreshData();
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        getUserIdAndToken();
        if (TextUtils.isEmpty(userId)) return;
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("accessToken", accessToken);
        presenter.getPositionList(map, className + "1");
        parentFragment.refreshData();

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
            xRecycler.setVisibility(View.GONE);
        } else if (errorStatus == 1) {//数据为空
            llNetNotWork.setVisibility(View.GONE);
            llNotData.setVisibility(View.VISIBLE);
            xRecycler.setVisibility(View.GONE);
        } else {
            llNetNotWork.setVisibility(View.GONE);
            llNotData.setVisibility(View.GONE);
            xRecycler.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        realTimeDataPresenter.detachView();
        CustomHttpUtils.cancelHttp(className + "1");
        CustomHttpUtils.cancelHttp(className + "2");
        CustomHttpUtils.cancelHttp(className + "3");
    }
}
