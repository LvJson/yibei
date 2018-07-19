package com.ts.lys.yibei.mvppresenter;

import com.ts.lys.yibei.bean.StockChartDatas;
import com.ts.lys.yibei.mvpmodal.IRequestServiceListener;
import com.ts.lys.yibei.mvpmodal.KLineDataModal;
import com.ts.lys.yibei.mvpview.IViewKLineData;

import java.util.Map;

/**
 * Created by jcdev1 on 2018/6/22.
 */

public class KLineDataPresenter extends BasePresenter {

    IViewKLineData iViewKLineData;
    KLineDataModal kLineDataModal;


    public KLineDataPresenter(IViewKLineData iViewKLineData) {
        this.iViewKLineData = iViewKLineData;
        kLineDataModal = new KLineDataModal();
    }

    public void getKLineData(Map<String, String> map, String tag, boolean isShowDialog) {

        showDialog(iViewKLineData, isShowDialog);

        kLineDataModal.getStockChartData(map, tag, new IRequestServiceListener<StockChartDatas>() {
            @Override
            public void success(StockChartDatas result) {
                dissDialog(iViewKLineData);
                iViewKLineData.showKLineData(result);
            }

            @Override
            public void faild(String msg) {

                dissDialog(iViewKLineData);
            }
        });
    }


}
