package com.ts.lys.yibei.mvppresenter;

import com.ts.lys.yibei.bean.SymbolInfo;
import com.ts.lys.yibei.bean.SymbolPrice;
import com.ts.lys.yibei.mvpmodal.IRequestServiceListener;
import com.ts.lys.yibei.mvpmodal.QuotationModal;
import com.ts.lys.yibei.mvpview.IQuotationView;

import java.util.Map;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public class QuotationPresenter extends BasePresenter {

    IQuotationView iQuotationView;
    QuotationModal quotationModal;

    public QuotationPresenter(IQuotationView iQuotationView) {
        this.iQuotationView = iQuotationView;
        this.quotationModal = new QuotationModal();
    }

    /**
     * 品种保证金及止盈止损数据接口
     *
     * @param map
     * @param tag
     */
    public void getSymbolCalInfo(Map<String, String> map, String tag) {
        showDialog(iQuotationView);
        quotationModal.getSymbolCalInfo(map, tag, new IRequestServiceListener<SymbolInfo>() {
            @Override
            public void success(SymbolInfo result) {
                iQuotationView.setSymbolCalInfo(result);
                dissDialog(iQuotationView);
            }

            @Override
            public void faild(String msg) {
                showToast(iQuotationView, msg);
                dissDialog(iQuotationView);
            }
        });
    }

    /**
     * 产品今开，昨收，最高，最低数据
     *
     * @param map
     * @param tag
     */
    public void getSymbolPriceData(Map<String, String> map, String tag) {
        showDialog(iQuotationView);
        quotationModal.getSymbolPriceData(map, tag, new IRequestServiceListener<SymbolPrice>() {
            @Override
            public void success(SymbolPrice result) {
                iQuotationView.setSymbolPriceData(result);
                dissDialog(iQuotationView);

            }

            @Override
            public void faild(String msg) {
                dissDialog(iQuotationView);
                showToast(iQuotationView, msg);
            }
        });
    }


}
