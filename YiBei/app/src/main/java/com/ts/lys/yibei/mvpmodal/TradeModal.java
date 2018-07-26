package com.ts.lys.yibei.mvpmodal;

import com.ts.lys.yibei.bean.OpenTrader;
import com.ts.lys.yibei.bean.PendingOrder;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.constant.UrlContents;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.ts.lys.yibei.utils.JsonAnalysisUtils;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public class TradeModal {

    public void pendingOrder(Map<String, String> map, String tag, final IRequestServiceListener<PendingOrder> listener) {

        CustomHttpUtils.getServiceDatas(map, UrlContents.PENDING_ORDER, tag, new CustomHttpUtils.ServiceStatus() {
            @Override
            public void faild(Call call, Exception e, int id) {
                listener.faild(BaseContents.NET_ERROR);
            }

            @Override
            public void success(String response, int id) {

                new JsonAnalysisUtils<PendingOrder>(PendingOrder.class).jsonAnalysis(response, new JsonAnalysisUtils.JsonAnalysisListener<PendingOrder>() {
                    @Override
                    public void success(PendingOrder pendingOrder) {
                        listener.success(pendingOrder);
                    }

                    @Override
                    public void fail(String str) {
                        listener.faild(str);
                    }
                });

            }
        });
    }


    public void openPosition(Map<String, String> map, String tag, final IRequestServiceListener<OpenTrader> listener) {

        CustomHttpUtils.getServiceDatas(map, UrlContents.TRADER_OPEN, tag, new CustomHttpUtils.ServiceStatus() {
            @Override
            public void faild(Call call, Exception e, int id) {
                listener.faild(BaseContents.NET_ERROR);
            }

            @Override
            public void success(String response, int id) {


                new JsonAnalysisUtils<OpenTrader>(OpenTrader.class).jsonAnalysis(response, new JsonAnalysisUtils.JsonAnalysisListener<OpenTrader>() {
                    @Override
                    public void success(OpenTrader openTrader) {
                        listener.success(openTrader);
                    }

                    @Override
                    public void fail(String str) {
                        listener.faild(str);
                    }
                });

            }
        });
    }
}
