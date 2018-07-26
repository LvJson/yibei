package com.ts.lys.yibei.mvppresenter;

import com.ts.lys.yibei.bean.TraderAccInfoModel;
import com.ts.lys.yibei.mvpmodal.IRequestServiceListener;
import com.ts.lys.yibei.mvpmodal.TraderAccInfoModal;
import com.ts.lys.yibei.mvpview.IOrderFragmentView;

import java.util.Map;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public class TraderAccInfoPresenter extends BasePresenter {
    private IOrderFragmentView iOrderFragmentView;
    private TraderAccInfoModal tradeModal;

    public TraderAccInfoPresenter(IOrderFragmentView iOrderFragmentView) {
        this.iOrderFragmentView = iOrderFragmentView;
        this.tradeModal = new TraderAccInfoModal();
    }


    /**
     * 获取持仓页基本信息
     *
     * @param map
     * @param tag
     */
    public void getTraderAccInfo(Map<String, String> map, String tag) {
        showDialog(iOrderFragmentView);
        tradeModal.getTraderAccInfo(map, tag, new IRequestServiceListener<TraderAccInfoModel>() {
            @Override
            public void success(TraderAccInfoModel result) {
                iOrderFragmentView.setTradeAccInfo(result.getData());
                dissDialog(iOrderFragmentView);
            }

            @Override
            public void faild(String msg) {
                showToast(iOrderFragmentView, msg);
                dissDialog(iOrderFragmentView);
            }

        });
    }
}
