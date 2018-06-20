package com.ts.lys.yibei.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ts.lys.yibei.customeview.CustomProgress;
import com.ts.lys.yibei.utils.Logger;
import com.ts.lys.yibei.utils.SpUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.ButterKnife;

/**
 * Created by jcdev1 on 2017/12/22.
 */

public abstract class BaseFragment extends Fragment {

    protected Fragment mFragment;
    protected View mRootView;
    private CustomProgress customProgress;
    public String className = "";
    public String accToken = "";
    public String telePhone = "";
    private Toast toast;
    public String TAG = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragment = this;
        TAG = getClass().getSimpleName();
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
        initBase();
        return mRootView;
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

    public void initBase() {
        className = getClass().getName();
        accToken = SpUtils.getString(getActivity(), "accessToken", "");
        telePhone = SpUtils.getString(getActivity(), "phoneNum", "");

    }


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

    protected void disCustomProgress() {
        if (customProgress != null)
            customProgress.dismiss();
    }

    protected void showToast(String content) {

        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT);
        toast.show();
    }

}
