package com.ts.lys.yibei.mvpview;

import com.ts.lys.yibei.bean.OpenTrader;
import com.ts.lys.yibei.bean.PendingOrder;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public interface ITradeOrPendingView extends BaseMvpView {

    void setTradeBackInfo(OpenTrader openTrader);

    void setPendingBackInfo(PendingOrder pendingOrder);

}
