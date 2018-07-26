package com.ts.lys.yibei.mvppresenter;

import com.ts.lys.yibei.bean.CloseTraderModel;
import com.ts.lys.yibei.bean.OrderPositionModel;
import com.ts.lys.yibei.mvpmodal.IRequestServiceListener;
import com.ts.lys.yibei.mvpmodal.PositionListModal;
import com.ts.lys.yibei.mvpview.IPositionFragmentView;

import java.util.Map;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public class PositionListPresenter extends BasePresenter {
    private IPositionFragmentView iPositionFragmentView;
    private PositionListModal tradeModal;

    public PositionListPresenter(IPositionFragmentView iPositionFragmentView) {
        this.iPositionFragmentView = iPositionFragmentView;
        this.tradeModal = new PositionListModal();
    }


    /**
     * 获取持仓列表信息
     *
     * @param map
     * @param tag
     */
    public void getPositionList(Map<String, String> map, String tag) {
        showDialog(iPositionFragmentView);
        tradeModal.getPositionList(map, tag, new IRequestServiceListener<OrderPositionModel>() {
            @Override
            public void success(OrderPositionModel result) {
                iPositionFragmentView.setPositionList(result.getData().getTraderOrder());
                dissDialog(iPositionFragmentView);
            }

            @Override
            public void faild(String msg) {
                showToast(iPositionFragmentView, msg);
                dissDialog(iPositionFragmentView);
            }

        });
    }

    /**
     * 获取平仓返回信息
     *
     * @param map
     * @param tag
     */
    public void getClosePositionBackInfo(Map<String, String> map, String tag) {
        showDialog(iPositionFragmentView);
        tradeModal.getClosePositionBackInfo(map, tag, new IRequestServiceListener<CloseTraderModel>() {
            @Override
            public void success(CloseTraderModel result) {

                iPositionFragmentView.setClosePositionBackInfo(result.getData().getCloseTrader());
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
