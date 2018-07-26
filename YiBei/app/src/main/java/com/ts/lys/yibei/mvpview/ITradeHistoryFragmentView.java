package com.ts.lys.yibei.mvpview;

import com.ts.lys.yibei.bean.OrderHistoryModel;

/**
 * Created by jcdev1 on 2018/6/22.
 */

public interface ITradeHistoryFragmentView extends BaseMvpView {

    void setTradeHistoryList(OrderHistoryModel orderHistoryModel);

}
