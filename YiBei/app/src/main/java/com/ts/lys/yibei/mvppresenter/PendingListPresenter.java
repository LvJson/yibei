package com.ts.lys.yibei.mvppresenter;

import com.ts.lys.yibei.bean.OrderPendingModel;
import com.ts.lys.yibei.mvpmodal.IRequestServiceListener;
import com.ts.lys.yibei.mvpmodal.PendingListModal;
import com.ts.lys.yibei.mvpview.IPendingFragmentView;

import java.util.Map;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public class PendingListPresenter extends BasePresenter {
    private IPendingFragmentView iPendingFragmentView;
    private PendingListModal pendingListModal;

    public PendingListPresenter(IPendingFragmentView iPendingFragmentView) {
        this.iPendingFragmentView = iPendingFragmentView;
        this.pendingListModal = new PendingListModal();
    }


    /**
     * 获取挂单列表信息
     *
     * @param map
     * @param tag
     */
    public void getPendingList(Map<String, String> map, String tag) {
        showDialog(iPendingFragmentView);
        pendingListModal.getPendingList(map, tag, new IRequestServiceListener<OrderPendingModel>() {
            @Override
            public void success(OrderPendingModel result) {
                iPendingFragmentView.setPendingList(result.getData().getPendOrder());
                dissDialog(iPendingFragmentView);
            }

            @Override
            public void faild(String msg) {
                showToast(iPendingFragmentView, msg);
                dissDialog(iPendingFragmentView);
            }

        });
    }

    /**
     * 获取取消挂单返回信息
     *
     * @param map
     * @param tag
     */
    public void getCancelPendingBackInfo(Map<String, String> map, String tag) {
        showDialog(iPendingFragmentView);
        pendingListModal.getCancelPendingBackInfo(map, tag, new IRequestServiceListener<Boolean>() {
            @Override
            public void success(Boolean result) {

                iPendingFragmentView.setCancelPendingBackInfo(result);
                dissDialog(iPendingFragmentView);

            }

            @Override
            public void faild(String msg) {
                showToast(iPendingFragmentView, msg);
                dissDialog(iPendingFragmentView);
            }
        });

    }
}
