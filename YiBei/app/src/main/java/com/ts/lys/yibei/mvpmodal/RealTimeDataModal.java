package com.ts.lys.yibei.mvpmodal;

import com.ts.lys.yibei.bean.RealTimeQuoteDatas;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.constant.UrlContents;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.ts.lys.yibei.utils.JsonAnalysisUtils;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by jcdev1 on 2018/6/22.
 */

public class RealTimeDataModal {

    public void getRealTimeData(Map<String, String> map, String tag, final IRequestServiceListener<RealTimeQuoteDatas> listener) {

        CustomHttpUtils.getServiceDatas(map, UrlContents.HOME_FRAGMENT_REALTIMEQUOTE, tag, new CustomHttpUtils.ServiceStatus() {
            @Override
            public void faild(Call call, Exception e, int id) {
                listener.faild(BaseContents.NET_ERROR);
            }

            @Override
            public void success(String response, int id) {

                new JsonAnalysisUtils<RealTimeQuoteDatas>(RealTimeQuoteDatas.class).jsonAnalysis(response, new JsonAnalysisUtils.JsonAnalysisListener<RealTimeQuoteDatas>() {
                    @Override
                    public void success(RealTimeQuoteDatas realTimeQuoteDatas) {
                        listener.success(realTimeQuoteDatas);
                    }

                    @Override
                    public void fail(String str) {
                        listener.faild(str);
                    }
                });

//                Gson gson = new Gson();
//                RealTimeQuoteDatas scd = gson.fromJson(response, RealTimeQuoteDatas.class);
//                if (scd.getErr_code().equals("0"))
//                    listener.success(scd);
//                else
//                    listener.faild(scd.getErr_msg());

            }
        });
    }
}
