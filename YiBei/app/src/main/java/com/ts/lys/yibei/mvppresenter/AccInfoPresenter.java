package com.ts.lys.yibei.mvppresenter;

import com.ts.lys.yibei.bean.UserAccInfoModel;
import com.ts.lys.yibei.mvpmodal.AccountInfoModal;
import com.ts.lys.yibei.mvpmodal.IRequestServiceListener;
import com.ts.lys.yibei.mvpview.IMineFragmentView;

import java.util.Map;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public class AccInfoPresenter extends BasePresenter {
    private IMineFragmentView iMineFragmentView;
    private AccountInfoModal accountInfoModal;

    public AccInfoPresenter(IMineFragmentView iMineFragmentView) {
        this.iMineFragmentView = iMineFragmentView;
        this.accountInfoModal = new AccountInfoModal();
    }


    /**
     * 获取用户信息
     *
     * @param map
     * @param tag
     */
    public void getAccInfo(Map<String, String> map, String tag) {

        showDialog(iMineFragmentView);
        accountInfoModal.getAccountInfo(map, tag, new IRequestServiceListener<UserAccInfoModel>() {
            @Override
            public void success(UserAccInfoModel result) {
                iMineFragmentView.showAccountInfo(result.getData());
                dissDialog(iMineFragmentView);
            }

            @Override
            public void faild(String msg) {
                showToast(iMineFragmentView, msg);
                dissDialog(iMineFragmentView);
            }

        });
    }


    public void getSwitchAccBackInfo(Map<String, String> map, String tag) {
        showDialog(iMineFragmentView);

        accountInfoModal.getSwitchAccState(map, tag, new IRequestServiceListener<Boolean>() {
            @Override
            public void success(Boolean result) {
                iMineFragmentView.showSwitchState(result);
                dissDialog(iMineFragmentView);

            }

            @Override
            public void faild(String msg) {
                showToast(iMineFragmentView, msg);
                dissDialog(iMineFragmentView);
            }
        });

    }
}
