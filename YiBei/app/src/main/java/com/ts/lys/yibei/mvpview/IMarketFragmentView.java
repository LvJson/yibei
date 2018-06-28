package com.ts.lys.yibei.mvpview;

import com.ts.lys.yibei.bean.GetQuotesModel;

/**
 * Created by jcdev1 on 2018/6/25.
 */

public interface IMarketFragmentView extends BaseMvpView {

    void setAllSymbol(GetQuotesModel allSymbol);

    void setCollectionSymbol(GetQuotesModel collectionSymbol);
}
