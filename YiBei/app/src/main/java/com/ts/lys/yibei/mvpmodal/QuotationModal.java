package com.ts.lys.yibei.mvpmodal;

import com.ts.lys.yibei.bean.SymbolInfo;
import com.ts.lys.yibei.bean.SymbolPrice;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.constant.UrlContents;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.ts.lys.yibei.utils.JsonAnalysisUtils;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public class QuotationModal {

    public void getSymbolCalInfo(Map<String, String> map, String tag, final IRequestServiceListener<SymbolInfo> listener) {

        CustomHttpUtils.getServiceDatas(map, UrlContents.SYMBOL_SYMBOLCALINFO, tag, new CustomHttpUtils.ServiceStatus() {
            @Override
            public void faild(Call call, Exception e, int id) {
                listener.faild(BaseContents.NET_ERROR);
            }

            @Override
            public void success(String response, int id) {

                new JsonAnalysisUtils<SymbolInfo>(SymbolInfo.class).jsonAnalysis(response, new JsonAnalysisUtils.JsonAnalysisListener<SymbolInfo>() {
                    @Override
                    public void success(SymbolInfo symbolInfo) {
                        listener.success(symbolInfo);
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
//                        SymbolInfo getQuotesModel = gson.fromJson(response, SymbolInfo.class);
//                        if (getQuotesModel.getErr_code().equals("0")) {
//                            listener.success(getQuotesModel);
//                        } else
//                            listener.faild(getQuotesModel.getErr_msg());
//                    } else
//                        listener.faild("no data");
//                }

            }
        });
    }


    public void getSymbolPriceData(Map<String, String> map, String tag, final IRequestServiceListener<SymbolPrice> listener) {

        CustomHttpUtils.getServiceDatas(map, UrlContents.QUOTE_SYMBOL_PRICE, tag, new CustomHttpUtils.ServiceStatus() {
            @Override
            public void faild(Call call, Exception e, int id) {
                listener.faild(BaseContents.NET_ERROR);
            }

            @Override
            public void success(String response, int id) {

                new JsonAnalysisUtils<SymbolPrice>(SymbolPrice.class).jsonAnalysis(response, new JsonAnalysisUtils.JsonAnalysisListener<SymbolPrice>() {
                    @Override
                    public void success(SymbolPrice symbolPrice) {
                        listener.success(symbolPrice);
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
//                        SymbolPrice getQuotesModel = gson.fromJson(response, SymbolPrice.class);
//                        if (getQuotesModel.getErr_code().equals("0")) {
//                            listener.success(getQuotesModel);
//                        } else
//                            listener.faild(getQuotesModel.getErr_msg());
//                    } else
//                        listener.faild("no data");
//                }

            }
        });
    }

}
