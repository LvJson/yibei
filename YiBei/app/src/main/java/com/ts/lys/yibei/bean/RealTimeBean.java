package com.ts.lys.yibei.bean;

/**
 * Created by jcdev1 on 2018/6/25.
 */

public class RealTimeBean {


    /**
     * market : 0.73277
     * symbol : AUDCHF
     * ask : 0.73307
     * time : 1529930566000
     * bid : 0.73277
     */

    private double market;
    private String symbol;
    private double ask;
    private long time;
    private double bid;

    public double getMarket() {
        return market;
    }

    public void setMarket(double market) {
        this.market = market;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }
}
