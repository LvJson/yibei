package com.ts.lys.yibei.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by jcdev1 on 2018/7/23.
 */

public class JsonAnalysisUtils<T> {

    private Class<T> tClass;

    public JsonAnalysisUtils(Class<T> tClass) {
        this.tClass = tClass;
    }

    public void jsonAnalysis(String response, JsonAnalysisListener<T> listener) {
        if (response != null) {
            JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
            String errCode = "";
            String errMsg = "";

            if (jsonObject.has("err_code") && !jsonObject.get("err_code").isJsonNull())
                errCode = jsonObject.get("err_code").getAsString();
            if (jsonObject.has("err_msg") && !jsonObject.get("err_msg").isJsonNull())
                errMsg = jsonObject.get("err_msg").getAsString();

            if (errCode.equals("0")) {

                if (jsonObject.has("data")) {
                    Gson gson = new Gson();
                    T t = gson.fromJson(response, tClass);
                    listener.success(t);
                }else
                    listener.fail("no data");
            } else
                listener.fail(errMsg);


        } else
            listener.fail("no data");

    }

    public interface JsonAnalysisListener<T> {

        void success(T t);

        void fail(String str);
    }


//    private static volatile JsonAnalysisUtils instance;
//
//    public static JsonAnalysisUtils getInstance() {
//        if (instance == null) {
//            synchronized (JsonAnalysisUtils.class) {
//                if (instance == null)
//                    instance = new JsonAnalysisUtils();
//            }
//        }
//        return instance;
//    }
}
