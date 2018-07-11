package com.ts.lys.yibei.mvpmodal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ts.lys.yibei.bean.GetQuotesModel;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.constant.UrlContents;
import com.ts.lys.yibei.utils.CustomHttpUtils;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by jcdev1 on 2018/6/25.
 */

public class AllSymbolModal {

    /**
     * 获取所有品种的当前行情
     *
     * @param map
     * @param tag
     * @param listener
     */
    public void getAllSymbol(Map<String, String> map, String tag, final IRequestServiceListener<GetQuotesModel> listener) {

        CustomHttpUtils.getServiceDatas(map, UrlContents.DEAL_SYMBOL_PRICE, tag, new CustomHttpUtils.ServiceStatus() {
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
                        GetQuotesModel getQuotesModel = gson.fromJson(response, GetQuotesModel.class);
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


    /**
     * 获取用户收藏品种的当前行情
     *
     * @param map
     * @param tag
     * @param listener
     */
    public void getCollectionSymbol(Map<String, String> map, String tag, final IRequestServiceListener<GetQuotesModel> listener) {

        CustomHttpUtils.getServiceDatas(map, UrlContents.DEAL_SYMBOL_FOCUS, tag, new CustomHttpUtils.ServiceStatus() {
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
                        GetQuotesModel getQuotesModel = gson.fromJson(response, GetQuotesModel.class);
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
