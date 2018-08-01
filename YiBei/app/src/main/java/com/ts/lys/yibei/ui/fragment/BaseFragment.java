package com.ts.lys.yibei.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ts.lys.yibei.R;
import com.ts.lys.yibei.constant.BaseContents;
import com.ts.lys.yibei.customeview.CustomProgress;
import com.ts.lys.yibei.mvpview.BaseMvpView;
import com.ts.lys.yibei.utils.Logger;
import com.ts.lys.yibei.utils.SpUtils;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.ButterKnife;

/**
 * Created by jcdev1 on 2017/12/22.
 */

public abstract class BaseFragment extends Fragment implements BaseMvpView {

    protected Fragment mFragment;
    protected View mRootView;
    private CustomProgress customProgress;
    public String className = "";

    public String userId = "";
    public String accessToken = "";

    private Toast toast;
    public String TAG = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragment = this;
        TAG = getClass().getSimpleName();
        className = getClass().getName();
//        accessToken = "d31303049b7224712b2073354d4cc92c8a196f1894b1876509d78b4cd9268149b9ab165da9a31a6251d2448261fd99d1a7d22133bb3ef018493849f4a7896993";
//        userId = "1403";
        getUserIdAndToken();
        Logger.e(TAG, "onCreat()");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutID(), null);
            ButterKnife.bind(this, mRootView);
            initBaseView();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    public void getUserIdAndToken() {
        accessToken = SpUtils.getString(getActivity(), BaseContents.ACCESS_TOKEN, "");
        userId = SpUtils.getString(getActivity(), BaseContents.USERID, "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        OkHttpUtils.getInstance().cancelTag(className);
        OkHttpUtils.getInstance().cancelTag(className);
    }

    protected abstract int getLayoutID();

    protected abstract void initBaseView();


    /**
     * 展示自定义dialog
     */
    protected void showCustomProgress() {
        if (getActivity() != null)
            if (customProgress == null)
                customProgress = CustomProgress.show(getActivity());
            else
                customProgress.show();
    }

    /**
     * 展示自定义dialog
     */
    @Override
    public void showCustomProgress(boolean cancelable) {
        if (customProgress == null)
            customProgress = CustomProgress.show(getActivity(), cancelable);
        else
            customProgress.show();
    }

    @Override
    public void disCustomProgress() {
        if (customProgress != null)
            customProgress.dismiss();
    }

    @Override
    public void showToast(String content) {
        if (content.equals(BaseContents.NET_ERROR))
            content = getString(R.string.net_error);

        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        String className = getClass().getSimpleName();
        MobclickAgent.onPageStart(className);
    }

    @Override
    public void onResume() {
        super.onResume();
        String className = getClass().getSimpleName();
        MobclickAgent.onPageEnd(className);
    }
}
