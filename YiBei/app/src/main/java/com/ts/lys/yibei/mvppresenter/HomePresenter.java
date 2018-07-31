package com.ts.lys.yibei.mvppresenter;

import com.ts.lys.yibei.bean.IndexBean;
import com.ts.lys.yibei.mvpmodal.HomeDataModal;
import com.ts.lys.yibei.mvpmodal.IRequestServiceListener;
import com.ts.lys.yibei.mvpview.IHomeFragmentView;

import java.util.Map;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public class HomePresenter extends BasePresenter {
    private IHomeFragmentView iHomeFragmentView;
    private HomeDataModal homeDataModal;

    public HomePresenter(IHomeFragmentView iHomeFragmentView) {
        this.iHomeFragmentView = iHomeFragmentView;
        this.homeDataModal = new HomeDataModal();
    }


    /**
     * 获取首页数据
     *
     * @param map
     * @param tag
     */
    public void getHomeData(Map<String, String> map, String tag) {

        showDialog(iHomeFragmentView);
        homeDataModal.getHomeData(map, tag, new IRequestServiceListener<IndexBean>() {
            @Override
            public void success(IndexBean result) {
                iHomeFragmentView.showHomeData(result.getData());
                dissDialog(iHomeFragmentView);
            }

            @Override
            public void faild(String msg) {
                showToast(iHomeFragmentView, msg);
                dissDialog(iHomeFragmentView);
            }

        });
    }
}
