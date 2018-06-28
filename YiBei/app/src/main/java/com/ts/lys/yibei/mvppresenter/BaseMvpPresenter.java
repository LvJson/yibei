package com.ts.lys.yibei.mvppresenter;

import com.ts.lys.yibei.mvpview.BaseMvpView;

/**
 * Created by amitshekhar on 13/01/17.
 */

public interface BaseMvpPresenter<V extends BaseMvpView> {

    void attachView(V mvpView);

    void detachView();

}
