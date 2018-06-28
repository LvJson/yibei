package com.ts.lys.yibei.mvppresenter;

import android.os.Handler;

import com.ts.lys.yibei.mvpview.BaseMvpView;

/**
 * Created by amitshekhar on 13/01/17.
 */

public class BasePresenter<V extends BaseMvpView> implements BaseMvpPresenter<V> {

    private V mMvpView;
    private Handler handler = new Handler();

    @Override
    public void attachView(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public V getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }

    public void showDialog(final BaseMvpView baseMvpView) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                baseMvpView.showCustomProgress(true);
            }
        });
    }

    public void dissDialog(final BaseMvpView baseMvpView) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                baseMvpView.disCustomProgress();
            }
        });
    }

    public void showToast(final BaseMvpView baseMvpView, final String str) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                baseMvpView.showToast(str);
            }
        });
    }


}
