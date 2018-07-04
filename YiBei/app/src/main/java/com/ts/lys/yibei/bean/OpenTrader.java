package com.ts.lys.yibei.bean;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public class OpenTrader extends ErrorStatus {

    private OpenTrader.DataBean data;

    public OpenTrader.DataBean getData() {
        return data;
    }

    public void setData(OpenTrader.DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private OpenTraderBean openTrader;

        public OpenTraderBean getOpenTrader() {
            return openTrader;
        }

        public void setOpenTrader(OpenTraderBean openTrader) {
            this.openTrader = openTrader;
        }

        public static class OpenTraderBean {
            private String symbol;
            private String symbolCn;
            private int userId;
            private int cmd;
            private float volume;
            private double tp;
            private double sl;
            private String ticket;
            private double openPrice;
            private double margin;

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public String getSymbolCn() {
                return symbolCn;
            }

            public void setSymbolCn(String symbolCn) {
                this.symbolCn = symbolCn;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getCmd() {
                return cmd;
            }

            public void setCmd(int cmd) {
                this.cmd = cmd;
            }

            public float getVolume() {
                return volume;
            }

            public void setVolume(float volume) {
                this.volume = volume;
            }

            public double getTp() {
                return tp;
            }

            public void setTp(double tp) {
                this.tp = tp;
            }

            public double getSl() {
                return sl;
            }

            public void setSl(double sl) {
                this.sl = sl;
            }

            public String getTicket() {
                return ticket;
            }

            public void setTicket(String ticket) {
                this.ticket = ticket;
            }

            public double getOpenPrice() {
                return openPrice;
            }

            public void setOpenPrice(double openPrice) {
                this.openPrice = openPrice;
            }

            public double getMargin() {
                return margin;
            }

            public void setMargin(double margin) {
                this.margin = margin;
            }

        }
    }
}
