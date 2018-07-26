package com.ts.lys.yibei.mvpmodal;

import com.ts.lys.yibei.bean.CloseTraderModel;
import com.ts.lys.yibei.bean.OrderPositionModel;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.constant.UrlContents;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.ts.lys.yibei.utils.JsonAnalysisUtils;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public class PositionListModal {

    public void getPositionList(Map<String, String> map, String tag, final IRequestServiceListener<OrderPositionModel> listener) {

        CustomHttpUtils.getServiceDatas(map, UrlContents.TRADER_POSITIONORDERLIST, tag, new CustomHttpUtils.ServiceStatus() {
            @Override
            public void faild(Call call, Exception e, int id) {
                listener.faild(BaseContents.NET_ERROR);
            }

            @Override
            public void success(String response, int id) {

                new JsonAnalysisUtils<OrderPositionModel>(OrderPositionModel.class).jsonAnalysis(response, new JsonAnalysisUtils.JsonAnalysisListener<OrderPositionModel>() {
                    @Override
                    public void success(OrderPositionModel orderPositionModel) {
                        listener.success(orderPositionModel);
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
//                        OrderPositionModel traderAccInfoModel = gson.fromJson(response, OrderPositionModel.class);
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


    public void getClosePositionBackInfo(Map<String, String> map, String tag, final IRequestServiceListener<CloseTraderModel> listener) {

        CustomHttpUtils.getServiceDatas(map, UrlContents.TRADER_CLOSE, tag, new CustomHttpUtils.ServiceStatus() {
            @Override
            public void faild(Call call, Exception e, int id) {
                listener.faild(BaseContents.NET_ERROR);
            }

            @Override
            public void success(String response, int id) {

                new JsonAnalysisUtils<CloseTraderModel>(CloseTraderModel.class).jsonAnalysis(response, new JsonAnalysisUtils.JsonAnalysisListener<CloseTraderModel>() {
                    @Override
                    public void success(CloseTraderModel closeTraderModel) {
                        listener.success(closeTraderModel);
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
//                        CloseTraderModel traderAccInfoModel = gson.fromJson(response, CloseTraderModel.class);
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
