package com.ts.lys.yibei.mvppresenter;

import com.ts.lys.yibei.bean.OrderHistoryModel;
import com.ts.lys.yibei.mvpmodal.IRequestServiceListener;
import com.ts.lys.yibei.mvpmodal.TradeHistoryListModal;
import com.ts.lys.yibei.mvpview.ITradeHistoryFragmentView;

import java.util.Map;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public class TradeHistoryListPresenter extends BasePresenter {
    private ITradeHistoryFragmentView iPositionFragmentView;
    private TradeHistoryListModal tradeModal;

    public TradeHistoryListPresenter(ITradeHistoryFragmentView iPositionFragmentView) {
        this.iPositionFragmentView = iPositionFragmentView;
        this.tradeModal = new TradeHistoryListModal();
    }


    /**
     * 获取交易历史列表信息
     *
     * @param map
     * @param tag
     */
    public void getTradeHistoryList(Map<String, String> map, String tag) {

        showDialog(iPositionFragmentView);
        tradeModal.getTradeHistoryList(map, tag, new IRequestServiceListener<OrderHistoryModel>() {
            @Override
            public void success(OrderHistoryModel result) {
                iPositionFragmentView.setTradeHistoryList(result);
                dissDialog(iPositionFragmentView);
            }

            @Override
            public void faild(String msg) {
                showToast(iPositionFragmentView, msg);
                dissDialog(iPositionFragmentView);
            }

        });
    }

}
