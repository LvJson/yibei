package com.ts.lys.yibei.utils;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.ts.lys.yibei.bean.EventBean;
import com.ts.lys.yibei.constant.EventContents;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by jcdev1 on 2018/2/10.
 */

public class AndroidInterface extends Object {
    private Activity activity;

    public AndroidInterface(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void npClickHandle() {

    }

    @JavascriptInterface
    public void pClickHandle() {
    }

    @JavascriptInterface
    public void nvClickHandle(String title, String url, String des) {

    }

    @JavascriptInterface
    public void fx_bind(String accType) {
        EventBus.getDefault().post(new EventBean(EventContents.BIND_MT4, accType));
    }
}
