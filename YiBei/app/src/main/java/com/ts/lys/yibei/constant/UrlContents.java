package com.ts.lys.yibei.constant;

/**
 * Created by jcdev1 on 2018/6/22.
 */

public class UrlContents {

    public static final String URLHEAD = "http://hyapi.trademaster168.com/api";
    /**
     * 实时数据
     */
    public static final String SOCKETIO_URL = "http://sc.trademaster168.com";
    /**
     * k线图历史数据
     */
    public static final String STOCK_CHART_ACTIVITY_HISTORYQUOTE = URLHEAD + "/quote/historyQuote";
    /**
     * 实时数据
     */
    public static final String HOME_FRAGMENT_REALTIMEQUOTE = URLHEAD + "/quote/realTimeQuote";
    /**
     * 产品今开，昨收，最高，最低数据
     */
    public static final String QUOTE_SYMBOL_PRICE = URLHEAD + "/quote/symbolPrice";
    /**
     * 所有品种的当前行情
     */
    public static final String DEAL_SYMBOL_PRICE = URLHEAD + "/deal/symbol/price";
    /**
     * 用户关注品种行情
     */
    public static final String DEAL_SYMBOL_FOCUS = URLHEAD + "/deal/symbol/focus";
    /**
     * 报价信息
     */
    public static final String SYMBOL_SYMBOLCALINFO = URLHEAD + "/symbol/symbolcalinfo";
    /**
     * 开仓
     */
    public static final String TRADER_OPEN = URLHEAD + "/trader/open";
    /**
     * 挂单
     */
    public static final String PENDING_ORDER = URLHEAD + "/pending/order";


}