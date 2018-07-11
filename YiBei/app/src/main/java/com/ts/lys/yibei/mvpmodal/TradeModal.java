package com.ts.lys.yibei.mvpmodal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ts.lys.yibei.bean.OpenTrader;
import com.ts.lys.yibei.bean.PendingOrder;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.constant.UrlContents;
import com.ts.lys.yibei.utils.CustomHttpUtils;

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
                if (response != null) {
                    JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                    if (jsonObject.get("data").isJsonObject()) {
                        Gson gson = new Gson();
                        PendingOrder getQuotesModel = gson.fromJson(response, PendingOrder.class);
                        if (getQuotesModel.getErr_code().equals("0")) {
                            listener.success(getQuotesModel);
                        } else
                            listener.faild(getQuotesModel.getErr_msg());
                    } else
                        listener.faild("no data");
                }

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
                if (response != null) {
                    JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                    if (jsonObject.get("data").isJsonObject()) {
                        Gson gson = new Gson();
                        OpenTrader getQuotesModel = gson.fromJson(response, OpenTrader.class);
                        if (getQuotesModel.getErr_code().equals("0")) {
                            listener.success(getQuotesModel);
                        } else
                            listener.faild(getQuotesModel.getErr_msg());
                    } else
                        listener.faild("no data");
                }

            }
        });
    }
}
