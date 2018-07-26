package com.ts.lys.yibei.mvpview;

import com.ts.lys.yibei.bean.OrderPendingModel;

import java.util.List;

/**
 * Created by jcdev1 on 2018/6/22.
 */

public interface IPendingFragmentView extends BaseMvpView {

    void setPendingList(List<OrderPendingModel.DataBean.PendOrderBean> pendingList);

    void setCancelPendingBackInfo(boolean result);

}
