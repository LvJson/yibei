package com.ts.lys.yibei.utils;

import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ts.lys.yibei.ui.activity.App;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.MediaType;


/**
 * Created by jcdev1 on 2017/6/17.
 */

public class CustomHttpUtils {

    private static String salt = "bcbfc6bca954050a87225706682cab0eb710b03d0f01759f026a3c32b961cdebb0b936e791a14077f9c4d548bfa8eb89de1b25cf72a5126c61255043719da8a9";

    public static void getServiceDatas(Map<String, String> map, String url, String tag, final ServiceStatus serviceStatus) {
        //公共参数
        String version = App.verName;
        String os = "Android";
        String osVersion = App.getSystemCode();
        String uuid = App.getUniquePsuedoID();
        String rd = UUID.randomUUID().toString();
        Long t = System.currentTimeMillis();
        String sign = Md5.getSign3(uuid, rd, t.toString(), salt);

        Map<String, String> mapPublic = new HashMap<>();
        mapPublic.put("version", version);
        mapPublic.put("os", os);
        mapPublic.put("osVersion", osVersion);
        mapPublic.put("uuid", uuid);
        mapPublic.put("rd", rd);
        mapPublic.put("t", t + "");
        mapPublic.put("sign", sign);
        if (map == null || map.size() == 0)
            map = mapPublic;
        else
            map.putAll(mapPublic);

        String accToken = "";
        if (map.containsKey("access_token")) {
            accToken = map.get("access_token");
            map.remove("access_token");
        }
        Logger.e("accToken", accToken);

        OkHttpUtils
                .post()
                .url(getURL(url, accToken))
                .tag(tag)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        serviceStatus.faild(call, e, id);
                        if (e.getMessage() != null)
                            Logger.e("faild", e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if (response != null) {
                            JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                            String code = "";
                            if (!jsonObject.get("code").isJsonNull())
                                code = jsonObject.get("code").getAsString();
                            if (code.equals("10002")) {
//                                EventBus.getDefault().post(new EventBean(EventContents.TOKEN_OVERDUE, ""));
                                return;
                            }
                        }


                        Logger.e("DATAS", response);
                        serviceStatus.success(response, id);
                    }
                });
    }


    public static void getServiceDatasGET(Map<String, String> map, String url, String tag, final ServiceStatus serviceStatus) {
        //公共参数
        String version = App.verName;
        String os = "Android";
        String osVersion = App.getSystemCode();
        String uuid = App.getUniquePsuedoID();
        String rd = UUID.randomUUID().toString();
        Long t = System.currentTimeMillis();
        String sign = Md5.getSign3(uuid, rd, t.toString(), salt);

        Map<String, String> mapPublic = new HashMap<>();
        mapPublic.put("version", version);
        mapPublic.put("os", os);
        mapPublic.put("osVersion", osVersion);
        mapPublic.put("uuid", uuid);
        mapPublic.put("rd", rd);
        mapPublic.put("t", t + "");
        mapPublic.put("sign", sign);
        if (map == null || map.size() == 0)
            map = mapPublic;
        else
            map.putAll(mapPublic);


        OkHttpUtils
                .get()
                .url(url)
                .tag(tag)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        serviceStatus.faild(call, e, id);
                        if (e.getMessage() != null)
                            Logger.e("faild", e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if (response != null) {
                            JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                            String code = "";
                            if (!jsonObject.get("code").isJsonNull())
                                code = jsonObject.get("code").getAsString();
                            if (code.equals("10002")) {
//                                EventBus.getDefault().post(new EventBean(EventContents.TOKEN_OVERDUE, ""));
                                return;
                            }
                        }


                        Logger.e("DATAS", response);
                        serviceStatus.success(response, id);
                    }
                });
    }

    public static void getServiceDatasJson(String json, String url, String tag, String accToken, final ServiceStatus serviceStatus) {

        OkHttpUtils
                .postString()
                .url(getURL(url, accToken))
                .tag(tag)
                .content(json)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        serviceStatus.faild(call, e, id);
                        if (e.getMessage() != null)
                            Logger.e("faild", e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if (response != null) {
                            JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                            String code = "";
                            if (!jsonObject.get("code").isJsonNull())
                                code = jsonObject.get("code").getAsString();
                            if (code.equals("10002")) {
//                                EventBus.getDefault().post(new EventBean(EventContents.TOKEN_OVERDUE, ""));
                                return;
                            }
                        }


                        Logger.e("DATAS", response);
                        serviceStatus.success(response, id);
                    }
                });
    }

    private static String getURL(String url, String accToken) {
        if (!TextUtils.isEmpty(accToken)) {
            return url + "?access_token=" + accToken;
        } else
            return url;
    }

    public interface ServiceStatus {

        void faild(Call call, Exception e, int id);

        void success(String response, int id);

    }

    /**
     * 取消该次网络请求
     *
     * @param tag
     */
    public static void cancelHttp(String tag) {
        if (!TextUtils.isEmpty(tag))
            OkHttpUtils.getInstance().cancelTag(tag);
    }

}
