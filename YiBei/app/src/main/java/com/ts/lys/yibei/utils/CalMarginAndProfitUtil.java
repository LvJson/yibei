package com.ts.lys.yibei.utils;

import com.ts.lys.yibei.bean.MarginAndProfitBean;
import com.ts.lys.yibei.bean.SymbolInfo;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public class CalMarginAndProfitUtil {


    /**
     * 计算保证金
     *
     * @param mapb
     * @return
     */
    public static double getMargin(MarginAndProfitBean mapb) {

        double margin = 0;
        double marginCalc = 0;
        double price = 0;

        SymbolInfo.DataBean.SymbolInfoBean sb = mapb.getSymbolInfoBean();
        double cmd = mapb.getCmd();
        double lots = mapb.getLots();
        double[] currentCurrency = mapb.getCurrentCurrency();
        double[] marginCalCurrency = mapb.getMarginCalCurrency();

        if (sb == null) return 0;
        if (sb.getType() == 1) {
            if (cmd == 0) {//买涨
                marginCalc = Arith.round(lots * currentCurrency[0] * sb.getMarginBase(), 2);
            } else {//买跌
                marginCalc = Arith.round(lots * currentCurrency[1] * sb.getMarginBase(), 2);
            }
        } else {
            marginCalc = Arith.round(lots * sb.getMarginBase(), 2);
        }

        if (sb.isMarginChange()) {
            price = Arith.div(Arith.add(marginCalCurrency[0], marginCalCurrency[1]), 2);
            if (sb.isUsdprex())
                margin = Arith.div(marginCalc, price, 2);
            else
                margin = Arith.round(Arith.mul(marginCalc, price), 2);
        } else {
            margin = marginCalc;
        }

        return margin;
    }


    /**
     * 计算盈亏
     *
     * @param mapb
     * @return
     */
    public static double getProfit(MarginAndProfitBean mapb) {

        double profit = 0;
        double profitCal = 0;
        double priceTrans = 0;
        SymbolInfo.DataBean.SymbolInfoBean sb = mapb.getSymbolInfoBean();
        double cmd = mapb.getCmd();
        double lots = mapb.getLots();
        double closePrice = mapb.getClosePrice();
        double openPrice = mapb.getOpenPrice();
        double[] profitCalCurrency = mapb.getProfitCalCurrency();

        profitCal = Arith.mul(Arith.mul(Arith.sub(closePrice, openPrice), sb.getContractSize()), lots);
        if (cmd == 1)//买跌
            profitCal = -profitCal;
        if (sb.isProfitChange()) {
            if (cmd == 0)
                priceTrans = profitCalCurrency[1];
            else
                priceTrans = profitCalCurrency[0];

            if (sb.isProfitUSDPrex())
                profit = Arith.div(profitCal, priceTrans, 2);
            else
                profit = Arith.mul(profitCal, priceTrans);

        } else
            profit = profitCal;

        return profit;
    }

    /**
     * 计算止盈、止损价格区间
     *
     * @param price
     * @param stopLe
     * @param digits
     * @param cmd
     * @param stopLossOrProfit
     * @return
     */
    public static double stopLossOrprofitLimit(double[] price, double stopLe, int digits, int cmd, int stopLossOrProfit) {
        double stoplever = Arith.div(stopLe, Math.pow(10, digits), digits);
        switch (cmd) {
            case 0://买涨
                if (stopLossOrProfit == 0) {//止损
                    double sub = Arith.sub(price[1], stoplever);
                    return sub;
                } else {//止盈
                    double add = Arith.add(price[1], stoplever);
                    return add;
                }

            case 1://买跌
                if (stopLossOrProfit == 0) {//止损

                    double add = Arith.add(price[0], stoplever);
                    return add;

                } else {//止盈
                    double sub = Arith.sub(price[0], stoplever);
                    return sub;
                }

        }
        return 0;

    }


    public static double pendingStopLossOrprofitLimit(double pendingPrice, double stopLe, int digits, int cmd, int stopLossOrProfit) {
        double stoplever = Arith.div(stopLe, Math.pow(10, digits), digits);
        switch (cmd) {
            case 0://买涨
                if (stopLossOrProfit == 0) {//止损
                    double sub = Arith.sub(pendingPrice, stoplever);
                    return sub;
                } else {//止盈
                    double add = Arith.add(pendingPrice, stoplever);
                    return add;
                }

            case 1://买跌
                if (stopLossOrProfit == 0) {//止损

                    double add = Arith.add(pendingPrice, stoplever);
                    return add;

                } else {//止盈
                    double sub = Arith.sub(pendingPrice, stoplever);
                    return sub;
                }

        }
        return 0;

    }


    /**
     * 挂单范围计算
     *
     * @param price
     * @param stopLe
     * @param digits
     * @param cmd
     * @param direction 取值方向  0: 正向 1：反向
     * @return
     */
    public static double getPendingLimit(double[] price, double stopLe, int digits, int cmd, int direction) {
        double stoplever = Arith.div(stopLe, Math.pow(10, digits), digits);
        switch (cmd) {
            case 0://买涨
                if (direction == 1) {//反向
                    double sub = Arith.sub(price[0], stoplever);
                    return sub;
                } else {//正向
                    double add = Arith.add(price[0], stoplever);
                    return add;
                }

            case 1://买跌
                if (direction == 1) {//正向

                    double add = Arith.add(price[1], stoplever);
                    return add;

                } else {//反向
                    double sub = Arith.sub(price[1], stoplever);
                    return sub;
                }

        }
        return 0;

    }


}
