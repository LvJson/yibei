package com.ts.lys.yibei.mvpmodal;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ts.lys.yibei.bean.UserAccInfoModel;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.constant.UrlContents;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.ts.lys.yibei.utils.JsonAnalysisUtils;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public class AccountInfoModal {

    public void getAccountInfo(Map<String, String> map, String tag, final IRequestServiceListener<UserAccInfoModel> listener) {

        CustomHttpUtils.getServiceDatas(map, UrlContents.USER_USERACCINFO, tag, new CustomHttpUtils.ServiceStatus() {
            @Override
            public void faild(Call call, Exception e, int id) {
                listener.faild(BaseContents.NET_ERROR);
            }

            @Override
            public void success(String response, int id) {

                new JsonAnalysisUtils<UserAccInfoModel>(UserAccInfoModel.class).jsonAnalysis(response, new JsonAnalysisUtils.JsonAnalysisListener<UserAccInfoModel>() {
                    @Override
                    public void success(UserAccInfoModel accountInfoModal) {
                        listener.success(accountInfoModal);
                    }

                    @Override
                    public void fail(String str) {
                        listener.faild(str);
                    }
                });

            }
        });
    }


    public void getSwitchAccState(Map<String, String> map, String tag, final IRequestServiceListener<Boolean> listener) {
        CustomHttpUtils.getServiceDatas(map, UrlContents.ACCOUNT_CHECKTYPE, tag, new CustomHttpUtils.ServiceStatus() {
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

                    if (jsonObject.has("err_code") && !jsonObject.get("err_code").isJsonNull())
                        errCode = jsonObject.get("err_code").getAsString();
                    if (jsonObject.has("err_msg") && !jsonObject.get("err_msg").isJsonNull())
                        errMsg = jsonObject.get("err_msg").getAsString();

                    if (errCode.equals("0")) {
                        listener.success(true);
                    } else
                        listener.faild(errMsg);
                }


            }
        });
    }

}
