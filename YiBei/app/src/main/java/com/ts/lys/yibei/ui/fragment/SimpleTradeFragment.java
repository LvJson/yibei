package com.ts.lys.yibei.ui.fragment;

import android.widget.TextView;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.bean.MarginAndProfitBean;
import com.ts.lys.yibei.bean.RealTimeBean;
import com.ts.lys.yibei.bean.RealTimeQuoteDatas;
import com.ts.lys.yibei.bean.SymbolInfo;
import com.ts.lys.yibei.customeview.AddDeleteView;
import com.ts.lys.yibei.customeview.ChooseTimesLayout;
import com.ts.lys.yibei.customeview.KeyboardLayout;
import com.ts.lys.yibei.ui.activity.QuotationsActivity;
import com.ts.lys.yibei.utils.CalMarginAndProfitUtil;

import java.util.List;

import butterknife.Bind;

/**
 * Created by jcdev1 on 2018/6/19.
 */

public class SimpleTradeFragment extends BaseFragment {
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
        addDeleteView.setTvExplain("已用保证金：$ " + margin);
    }

}
