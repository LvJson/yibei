package com.ts.lys.yibei.bean;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public class MarginAndProfitBean {

    private SymbolInfo.DataBean.SymbolInfoBean symbolInfoBean;

    /**
     * 手数
     */
    private double lots;

    /**
     * 涨跌 0：买涨 1：买跌
     */
    private int cmd;

    /**
     * 当前货币的ask，bid值
     */
    private double[] currentCurrency;
    /**
     * 保证金计算需要：提供转换货币的ask，bid值
     */
    private double[] marginCalCurrency;
    /**
     * 盈亏计算需要：提供转换货币的ask，bid值
     */
    private double[] profitCalCurrency;
    /**
     * 收盘价
     */
    private double closePrice;
    /**
     * 开盘价
     */
    private double openPrice;
    /**
     * 止盈还是止损 0：止盈 1：止损 2:未设置止盈止损
     */
    private int stopLossOrProfit;

    public int getStopLossOrProfit() {
        return stopLossOrProfit;
    }

    public void setStopLossOrProfit(int stopLossOrProfit) {
        this.stopLossOrProfit = stopLossOrProfit;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public SymbolInfo.DataBean.SymbolInfoBean getSymbolInfoBean() {
        return symbolInfoBean;
    }

    public void setSymbolInfoBean(SymbolInfo.DataBean.SymbolInfoBean symbolInfoBean) {
        this.symbolInfoBean = symbolInfoBean;
    }

    public double getLots() {
        return lots;
    }

    public void setLots(double lots) {
        this.lots = lots;
    }

    public double[] getCurrentCurrency() {
        return currentCurrency;
    }

    public void setCurrentCurrency(double[] currentCurrency) {
        this.currentCurrency = currentCurrency;
    }

    public double[] getMarginCalCurrency() {
        return marginCalCurrency;
    }

    public void setMarginCalCurrency(double[] marginCalCurrency) {
        this.marginCalCurrency = marginCalCurrency;
    }

    public double[] getProfitCalCurrency() {
        return profitCalCurrency;
    }

    public void setProfitCalCurrency(double[] profitCalCurrency) {
        this.profitCalCurrency = profitCalCurrency;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }
}
