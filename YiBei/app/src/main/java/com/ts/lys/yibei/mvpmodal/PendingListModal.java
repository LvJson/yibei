package com.ts.lys.yibei.mvpmodal;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ts.lys.yibei.bean.OrderPendingModel;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.constant.UrlContents;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.ts.lys.yibei.utils.JsonAnalysisUtils;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public class PendingListModal {

    public void getPendingList(Map<String, String> map, String tag, final IRequestServiceListener<OrderPendingModel> listener) {

        CustomHttpUtils.getServiceDatas(map, UrlContents.PENDING_LIST, tag, new CustomHttpUtils.ServiceStatus() {
            @Override
            public void faild(Call call, Exception e, int id) {
                listener.faild(BaseContents.NET_ERROR);
            }

            @Override
            public void success(String response, int id) {
                new JsonAnalysisUtils<OrderPendingModel>(OrderPendingModel.class).jsonAnalysis(response, new JsonAnalysisUtils.JsonAnalysisListener<OrderPendingModel>() {
                    @Override
                    public void success(OrderPendingModel orderPendingModel) {
                        listener.success(orderPendingModel);
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
//                        OrderPendingModel traderAccInfoModel = gson.fromJson(response, OrderPendingModel.class);
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


    public void getCancelPendingBackInfo(Map<String, String> map, String tag, final IRequestServiceListener<Boolean> listener) {

        CustomHttpUtils.getServiceDatas(map, UrlContents.PENDING_DELETE, tag, new CustomHttpUtils.ServiceStatus() {
            @Override
            public void faild(Call call, Exception e, int id) {
                listener.faild(BaseContents.NET_ERROR);
            }

            @Override
            public void success(String response, int id) {

                if (response != null) {
                    JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                    String errCode = "";
                    String errMsg = "";
                    if (!jsonObject.get("err_code").isJsonNull())
                        errCode = jsonObject.get("err_code").getAsString();
                    if (!jsonObject.get("err_msg").isJsonNull())
                        errMsg = jsonObject.get("err_msg").getAsString();
                    if (errCode.equals("0"))
                        listener.success(true);
                    else
                        listener.faild(errMsg);
                }

            }
        });
    }
}
