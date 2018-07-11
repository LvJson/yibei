package com.ts.lys.yibei.mvpmodal;

import com.google.gson.Gson;
import com.ts.lys.yibei.bean.StockChartDatas;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.constant.UrlContents;
import com.ts.lys.yibei.utils.CustomHttpUtils;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by jcdev1 on 2018/6/22.
 */

public class KLineDataModal {

    public void getStockChartData(Map<String, String> map, String tag, final IRequestServiceListener<StockChartDatas> listener) {

        CustomHttpUtils.getServiceDatas(map, UrlContents.STOCK_CHART_ACTIVITY_HISTORYQUOTE, tag, new CustomHttpUtils.ServiceStatus() {
            @Override
            public void faild(Call call, Exception e, int id) {
                listener.faild(BaseContents.NET_ERROR);
            }

            @Override
            public void success(String response, int id) {
                Gson gson = new Gson();
                StockChartDatas scd = gson.fromJson(response, StockChartDatas.class);
                if (scd.getErr_code().equals("0"))
                    listener.success(scd);
                else
                    listener.faild(scd.getErr_msg());

            }
        });
    }
}
