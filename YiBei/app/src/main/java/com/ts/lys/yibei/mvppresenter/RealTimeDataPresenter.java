package com.ts.lys.yibei.mvppresenter;

import android.os.Handler;

import com.ts.lys.yibei.bean.RealTimeQuoteDatas;
import com.ts.lys.yibei.mvpmodal.IRequestServiceListener;
import com.ts.lys.yibei.mvpmodal.RealTimeDataModal;
import com.ts.lys.yibei.mvpview.IRealTimeView;
import com.ts.lys.yibei.utils.Logger;

import java.util.Map;

/**
 * Created by jcdev1 on 2018/6/22.
 */

public class RealTimeDataPresenter extends BasePresenter {

    IRealTimeView iViewRealTime;
    RealTimeDataModal realTimeDataModal;

    private Handler handler = new Handler();
    private Runnable runnable;


    public RealTimeDataPresenter(IRealTimeView iViewRealTime) {
        this.iViewRealTime = iViewRealTime;
        realTimeDataModal = new RealTimeDataModal();
    }

    public void getRealTimeQuotoDatas(Map<String, String> map, String tag) {

        realTimeDataModal.getRealTimeData(map, tag, new IRequestServiceListener<RealTimeQuoteDatas>() {
            @Override
            public void success(RealTimeQuoteDatas result) {
                iViewRealTime.showRealTimeData(result);
            }

            @Override
            public void faild(String msg) {

            }
        });

    }


    public void startRealTimeQuote(final Map<String, String> map, final String tag) {
        Logger.e("symbol", map.get("symbol"));
        stopRealTimerQuote();
        runnable = new Runnable() {
            @Override
            public void run() {
                getRealTimeQuotoDatas(map, tag);
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }

    public void stopRealTimerQuote() {
        if (runnable != null && handler != null)
            handler.removeCallbacks(runnable);
    }

}
