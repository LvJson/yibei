package com.ts.lys.yibei.mvpview;

import com.ts.lys.yibei.bean.IndexBean;

/**
 * Created by jcdev1 on 2018/6/22.
 */

public interface IHomeFragmentView extends BaseMvpView {

    void showHomeData(IndexBean.DataBean indexBean);

}
