package com.ts.lys.yibei.mvpview;

import com.ts.lys.yibei.bean.StockChartDatas;
import com.ts.lys.yibei.bean.SymbolInfo;

/**
 * Created by jcdev1 on 2018/6/22.
 */

public interface IViewKLineData extends BaseMvpView {

    void showKLineData(StockChartDatas stockChartDatas);

}
