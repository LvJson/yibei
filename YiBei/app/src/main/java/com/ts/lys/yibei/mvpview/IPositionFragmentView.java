package com.ts.lys.yibei.mvpview;

import com.ts.lys.yibei.bean.CloseTraderModel;
import com.ts.lys.yibei.bean.OrderPositionModel;

import java.util.List;

/**
 * Created by jcdev1 on 2018/6/22.
 */

public interface IPositionFragmentView extends BaseMvpView {

    void setPositionList(List<OrderPositionModel.DataBean.TraderOrderBean> traderOrderBeanList);

    void setClosePositionBackInfo(CloseTraderModel.DataBean.CloseTraderBean closeTraderBean);

}
