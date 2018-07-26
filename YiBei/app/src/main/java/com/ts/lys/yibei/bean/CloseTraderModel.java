package com.ts.lys.yibei.bean;

/**
 * Created by jcdev1 on 2018/7/23.
 */

public class CloseTraderModel extends ErrorStatus {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * closeTrader : {"ticket":"446044201302360064","closePrice":0.77781,"closeTime":"2018-05-16 11:20:18","profit":4.42,"symbolCn":"Canadian Dollar Swiss Franc","symbolEn":"CADCHF","openPrice":0.78223,"cmd":1,"volume":0.01}
         */

        private CloseTraderBean closeTrader;

        public CloseTraderBean getCloseTrader() {
            return closeTrader;
        }

        public void setCloseTrader(CloseTraderBean closeTrader) {
            this.closeTrader = closeTrader;
        }

        public static class CloseTraderBean {
            /**
             * ticket : 446044201302360064
             * closePrice : 0.77781
             * closeTime : 2018-05-16 11:20:18
             * profit : 4.42
             * symbolCn : Canadian Dollar Swiss Franc
             * symbolEn : CADCHF
             * openPrice : 0.78223
             * cmd : 1
             * volume : 0.01
             */

            private String ticket;
            private double closePrice;
            private String closeTime;
            private double profit;
            private String symbolCn;
            private String symbolEn;
            private double openPrice;
            private int cmd;
            private double volume;

            public String getTicket() {
                return ticket;
            }

            public void setTicket(String ticket) {
                this.ticket = ticket;
            }

            public double getClosePrice() {
                return closePrice;
            }

            public void setClosePrice(double closePrice) {
                this.closePrice = closePrice;
            }

            public String getCloseTime() {
                return closeTime;
            }

            public void setCloseTime(String closeTime) {
                this.closeTime = closeTime;
            }

            public double getProfit() {
                return profit;
            }

            public void setProfit(double profit) {
                this.profit = profit;
            }

            public String getSymbolCn() {
                return symbolCn;
            }

            public void setSymbolCn(String symbolCn) {
                this.symbolCn = symbolCn;
            }

            public String getSymbolEn() {
                return symbolEn;
            }

            public void setSymbolEn(String symbolEn) {
                this.symbolEn = symbolEn;
            }

            public double getOpenPrice() {
                return openPrice;
            }

            public void setOpenPrice(double openPrice) {
                this.openPrice = openPrice;
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
        }
    }
}
