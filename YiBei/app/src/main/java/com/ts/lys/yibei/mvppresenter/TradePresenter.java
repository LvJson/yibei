package com.ts.lys.yibei.mvppresenter;

import com.ts.lys.yibei.bean.OpenTrader;
import com.ts.lys.yibei.bean.PendingOrder;
import com.ts.lys.yibei.mvpmodal.IRequestServiceListener;
import com.ts.lys.yibei.mvpmodal.TradeModal;
import com.ts.lys.yibei.mvpview.ITradeOrPendingView;

import java.util.Map;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public class TradePresenter extends BasePresenter {
    private ITradeOrPendingView iTradeOrPendingView;
    private TradeModal tradeModal;

    public TradePresenter(ITradeOrPendingView iTradeOrPendingView) {
        this.iTradeOrPendingView = iTradeOrPendingView;
        this.tradeModal = new TradeModal();
    }


    /**
     * 开仓
     *
     * @param map
     * @param tag
     */
    public void openPosition(Map<String, String> map, String tag) {
        showDialog(iTradeOrPendingView);
        tradeModal.openPosition(map, tag, new IRequestServiceListener<OpenTrader>() {
            @Override
            public void success(OpenTrader result) {
                iTradeOrPendingView.setTradeBackInfo(result);
                dissDialog(iTradeOrPendingView);
            }

            @Override
            public void faild(String msg) {
                showToast(iTradeOrPendingView, msg);
                dissDialog(iTradeOrPendingView);
            }

        });
    }

    /**
     * 挂单
     *
     * @param map
     * @param tag
     */
    public void pendingOrder(Map<String, String> map, String tag) {
        showDialog(iTradeOrPendingView);
        tradeModal.pendingOrder(map, tag, new IRequestServiceListener<PendingOrder>() {
            @Override
            public void success(PendingOrder result) {
                iTradeOrPendingView.setPendingBackInfo(result);
                dissDialog(iTradeOrPendingView);
            }

            @Override
            public void faild(String msg) {
                dissDialog(iTradeOrPendingView);
                showToast(iTradeOrPendingView, msg);

            }
        });
    }
}
