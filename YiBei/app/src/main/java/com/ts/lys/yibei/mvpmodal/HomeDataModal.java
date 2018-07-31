package com.ts.lys.yibei.mvpmodal;

import com.ts.lys.yibei.bean.IndexBean;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.constant.UrlContents;
import com.ts.lys.yibei.utils.CustomHttpUtils;
import com.ts.lys.yibei.utils.JsonAnalysisUtils;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public class HomeDataModal {

    public void getHomeData(Map<String, String> map, String tag, final IRequestServiceListener<IndexBean> listener) {

        CustomHttpUtils.getServiceDatas(map, UrlContents.INDEX_URL, tag, new CustomHttpUtils.ServiceStatus() {
            @Override
            public void faild(Call call, Exception e, int id) {
                listener.faild(BaseContents.NET_ERROR);
            }

            @Override
            public void success(String response, int id) {

                new JsonAnalysisUtils<IndexBean>(IndexBean.class).jsonAnalysis(response, new JsonAnalysisUtils.JsonAnalysisListener<IndexBean>() {
                    @Override
                    public void success(IndexBean indexBean) {
                        listener.success(indexBean);
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
