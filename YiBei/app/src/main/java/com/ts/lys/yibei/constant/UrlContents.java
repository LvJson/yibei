package com.ts.lys.yibei.constant;

/**
 * Created by jcdev1 on 2018/6/22.
 */

public class UrlContents {

    /**
     * 测试环境
     */

//    public static final String URLHEAD = "http://115.236.88.130:4417/api";


    /**
     * 测试环境：实时数据
     */
//    public static final String SOCKETIO_URL = "http://115.236.88.130:7489";


    /**
     * 线上环境
     */
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
    /**
     * 增减自选
     */
    public static final String DEAL_SYMBOL_DIYSYMBOL = URLHEAD + "/deal/symbol/diySymbol";

    /************* 订单模块 ********************/

    /**
     * 持仓信息
     */
    public static final String TRADER_ACCINFO = URLHEAD + "/trader/accInfo";
    /**
     * 持仓列表 - 单独
     */
    public static final String TRADER_POSITIONORDERLIST = URLHEAD + "/trader/positionOrderList";
    /**
     * 平仓
     */
    public static final String TRADER_CLOSE = URLHEAD + "/trader/close";
    /**
     * 挂单列表
     */
    public static final String PENDING_LIST = URLHEAD + "/pending/list";
    /**
     * 撤销挂单
     */
    public static final String PENDING_DELETE = URLHEAD + "/pending/delete";
    /**
     * 持仓历史
     */
    public static final String TRADER_HISTORYORDER = URLHEAD + "/trader/historyOrder";
    /**
     * 修改止盈止损
     */
    public static final String TRADER_UPDATE = URLHEAD + "/trader/update";


    /************* 注册，登录 ********************/

    /**
     * 注册
     */
    public static final String REGISTER = URLHEAD + "/register";
    /**
     * 登录
     */
    public static final String LOGIN = URLHEAD + "/login";
    /**
     * 账号信息
     */
    public static final String USER_USERACCINFO = URLHEAD + "/user/userAccInfo";
    /**
     * 切换账户
     */
    public static final String ACCOUNT_CHECKTYPE = URLHEAD + "/account/checkType";
    /**
     * MT4绑定用户
     */
    public static final String BROKER_BIND = URLHEAD + "/broker/bind";

    /**
     * 找回密码
     */
    public static final String MODIFYPASSWD = URLHEAD + "/modifyPasswd";

}
