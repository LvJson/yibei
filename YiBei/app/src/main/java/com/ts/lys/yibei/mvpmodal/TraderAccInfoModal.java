package com.ts.lys.yibei.mvpmodal;

import com.ts.lys.yibei.bean.TraderAccInfoModel;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.constant.UrlContents;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.ts.lys.yibei.utils.JsonAnalysisUtils;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public class TraderAccInfoModal {

    public void getTraderAccInfo(Map<String, String> map, String tag, final IRequestServiceListener<TraderAccInfoModel> listener) {

        CustomHttpUtils.getServiceDatas(map, UrlContents.TRADER_ACCINFO, tag, new CustomHttpUtils.ServiceStatus() {
            @Override
            public void faild(Call call, Exception e, int id) {
                listener.faild(BaseContents.NET_ERROR);
            }

            @Override
            public void success(String response, int id) {


                new JsonAnalysisUtils<TraderAccInfoModel>(TraderAccInfoModel.class).jsonAnalysis(response, new JsonAnalysisUtils.JsonAnalysisListener<TraderAccInfoModel>() {
                    @Override
                    public void success(TraderAccInfoModel traderAccInfoModel) {
                        listener.success(traderAccInfoModel);
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
