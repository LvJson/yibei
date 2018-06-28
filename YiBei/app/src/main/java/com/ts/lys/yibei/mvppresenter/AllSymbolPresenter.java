package com.ts.lys.yibei.mvppresenter;

import com.ts.lys.yibei.bean.GetQuotesModel;
import com.ts.lys.yibei.mvpmodal.AllSymbolModal;
import com.ts.lys.yibei.mvpmodal.IRequestServiceListener;
import com.ts.lys.yibei.mvpview.IMarketFragmentView;

import java.util.Map;

/**
 * Created by jcdev1 on 2018/6/25.
 */

public class AllSymbolPresenter extends BasePresenter {

    private IMarketFragmentView iMarkerView;
    private AllSymbolModal allSymbolModal;

    public AllSymbolPresenter(IMarketFragmentView iMarkerView) {
        this.iMarkerView = iMarkerView;
        this.allSymbolModal = new AllSymbolModal();
    }

    /**
     * 获取所有品种的当前行情
     *
     * @param map
     * @param tag
     */
    public void getAllSymbol(Map<String, String> map, String tag) {
        showDialog(iMarkerView);
        allSymbolModal.getAllSymbol(map, tag, new IRequestServiceListener<GetQuotesModel>() {
            @Override
            public void success(GetQuotesModel result) {
                dissDialog(iMarkerView);
                iMarkerView.setAllSymbol(result);
            }

            @Override
            public void faild(String msg) {
                dissDialog(iMarkerView);
                iMarkerView.showToast(msg);
            }
        });

    }

    /**
     * 获取所有品种的当前行情
     *
     * @param map
     * @param tag
     */
    public void getCollectionSymbol(Map<String, String> map, String tag) {
        allSymbolModal.getCollectionSymbol(map, tag, new IRequestServiceListener<GetQuotesModel>() {
            @Override
            public void success(GetQuotesModel result) {
                iMarkerView.setCollectionSymbol(result);
            }

            @Override
            public void faild(String msg) {
                dissDialog(iMarkerView);
                iMarkerView.showToast(msg);
            }
        });

    }
}
