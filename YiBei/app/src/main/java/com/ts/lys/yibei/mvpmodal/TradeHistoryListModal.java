package com.ts.lys.yibei.mvpmodal;

import com.ts.lys.yibei.bean.OrderHistoryModel;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.constant.UrlContents;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.ts.lys.yibei.utils.JsonAnalysisUtils;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public class TradeHistoryListModal {

    public void getTradeHistoryList(Map<String, String> map, String tag, final IRequestServiceListener<OrderHistoryModel> listener) {

        CustomHttpUtils.getServiceDatas(map, UrlContents.TRADER_HISTORYORDER, tag, new CustomHttpUtils.ServiceStatus() {
            @Override
            public void faild(Call call, Exception e, int id) {
                listener.faild(BaseContents.NET_ERROR);
            }

            @Override
            public void success(String response, int id) {
                new JsonAnalysisUtils<OrderHistoryModel>(OrderHistoryModel.class).jsonAnalysis(response, new JsonAnalysisUtils.JsonAnalysisListener<OrderHistoryModel>() {
                    @Override
                    public void success(OrderHistoryModel orderHistoryModel) {
                        listener.success(orderHistoryModel);
                    }

                    @Override
                    public void fail(String str) {
                        listener.faild(str);
                    }
                });

//                if (response != null) {
//                    JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
//                    if (jsonObject.get("data").isJsonObject()) {
//                        Gson gson = new Gson();
//                        OrderHistoryModel traderAccInfoModel = gson.fromJson(response, OrderHistoryModel.class);
//                        if (traderAccInfoModel.getErr_code().equals("0")) {
//                            listener.success(traderAccInfoModel);
//                        } else
//                            listener.faild(traderAccInfoModel.getErr_msg());
//                    } else
//                        listener.faild("no data");
//                }

            }
        });
    }
}
