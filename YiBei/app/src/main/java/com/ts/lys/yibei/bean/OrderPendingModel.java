package com.ts.lys.yibei.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jcdev1 on 2018/7/23.
 */

public class OrderPendingModel extends ErrorStatus {

    /**
     * data : {"pendOrder":[{"ticket":"13265265","openTime":"2018-04-05 11:22:04","cmd":3,"volume":0.01,"symbolEn":"NZDCNH","symbolCn":"纽元/离岸人民币","openPrice":5.591,"sl":0,"tp":0,"digits":3,"expiration":0,"price":null,"lever":200,"market":null,"way":"限价"}]}
     * err_code : 0
     * err_msg : successe
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<PendOrderBean> pendOrder;

        public List<PendOrderBean> getPendOrder() {
            return pendOrder;
        }

        public void setPendOrder(List<PendOrderBean> pendOrder) {
            this.pendOrder = pendOrder;
        }

        public static class PendOrderBean implements Serializable {
            /**
             * ticket : 13265265
             * openTime : 2018-04-05 11:22:04
             * cmd : 3
             * volume : 0.01
             * symbolEn : NZDCNH
             * symbolCn : 纽元/离岸人民币
             * openPrice : 5.591
             * sl : 0.0
             * tp : 0.0
             * digits : 3
             * expiration : 0
             * price : null
             * lever : 200
             * market : null
             * way : 限价
             */

            private String ticket;
            private String openTime;
            private int cmd;
            private double volume;
            private String symbolEn;
            private String symbolCn;
            private double openPrice;
            private double sl;
            private double tp;
            private int digits;
            private int expiration;
            private double price;
            private int lever;
            private double market;
            private String way;

            public String getTicket() {
                return ticket;
            }

            public void setTicket(String ticket) {
                this.ticket = ticket;
            }

            public String getOpenTime() {
                return openTime;
            }

            public void setOpenTime(String openTime) {
                this.openTime = openTime;
            }

            public int getCmd() {
                return cmd;
            }

            public void setCmd(int cmd) {
                this.cmd = cmd;
            }

            public double getVolume() {
                return volume;
            }

            public void setVolume(double volume) {
                this.volume = volume;
            }

            public String getSymbolEn() {
                return symbolEn;
            }

            public void setSymbolEn(String symbolEn) {
                this.symbolEn = symbolEn;
            }

            public String getSymbolCn() {
                return symbolCn;
            }

            public void setSymbolCn(String symbolCn) {
                this.symbolCn = symbolCn;
            }

            public double getOpenPrice() {
                return openPrice;
            }

            public void setOpenPrice(double openPrice) {
                this.openPrice = openPrice;
            }

            public double getSl() {
                return sl;
            }

            public void setSl(double sl) {
                this.sl = sl;
            }

            public double getTp() {
                return tp;
            }

            public void setTp(double tp) {
                this.tp = tp;
            }

            public int getDigits() {
                return digits;
            }

            public void setDigits(int digits) {
                this.digits = digits;
            }

            public int getExpiration() {
                return expiration;
            }

            public void setExpiration(int expiration) {
                this.expiration = expiration;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public int getLever() {
                return lever;
            }

            public void setLever(int lever) {
                this.lever = lever;
            }

            public double getMarket() {
                return market;
            }

            public void setMarket(double market) {
                this.market = market;
            }

            public String getWay() {
                return way;
            }

            public void setWay(String way) {
                this.way = way;
            }

        }
    }
}
