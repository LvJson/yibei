package com.ts.lys.yibei.mvpview;

import com.ts.lys.yibei.bean.RealTimeQuoteDatas;

/**
 * Created by jcdev1 on 2018/6/22.
 */

public interface IRealTimeView extends BaseMvpView {

    void showRealTimeData(RealTimeQuoteDatas realTimeQuoteDatas);
}
