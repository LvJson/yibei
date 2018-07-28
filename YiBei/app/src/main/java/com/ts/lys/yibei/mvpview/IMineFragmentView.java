package com.ts.lys.yibei.mvpview;

import com.ts.lys.yibei.bean.UserAccInfoModel;

/**
 * Created by jcdev1 on 2018/6/22.
 */

public interface IMineFragmentView extends BaseMvpView {

    void showAccountInfo(UserAccInfoModel.DataBean accountInfo);

    void showSwitchState(boolean state);

}
