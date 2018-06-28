package com.ts.lys.yibei.mvpview;

import com.ts.lys.yibei.bean.SymbolInfo;
import com.ts.lys.yibei.bean.SymbolPrice;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public interface IQuotationView extends BaseMvpView {

    void setSymbolPriceData(SymbolPrice symbolPrice);

    void setSymbolCalInfo(SymbolInfo symbolInfo);

}
