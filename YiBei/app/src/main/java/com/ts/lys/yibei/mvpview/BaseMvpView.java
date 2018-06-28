package com.ts.lys.yibei.mvpview;

/**
 * Created by jcdev1 on 2018/6/22.
 */

public interface BaseMvpView {

    void showCustomProgress(boolean cancelable);

    void disCustomProgress();

    void showToast(String str);
}
