package com.ts.lys.yibei.bean;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public class PendingOrder extends ErrorStatus {

    private PendingOrder.DataBean data;

    public PendingOrder.DataBean getData() {
        return data;
    }

    public void setData(PendingOrder.DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private PendingOrder.DataBean.PendingOrderBean pendingOrder;

        public PendingOrder.DataBean.PendingOrderBean getPendingOrder() {
            return pendingOrder;
        }

        public void setPendingOrder(PendingOrder.DataBean.PendingOrderBean pendingOrder) {
            this.pendingOrder = pendingOrder;
        }

        public static class PendingOrderBean {
            private String symbolCn;
            private int userId;
            private int cmd;
            private float volume;
            private double tp;
            private double sl;
            private String ticket;
            private double pendingPrice;

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

            public double getPendingPrice() {
                return pendingPrice;
            }

            public void setPendingPrice(double pendingPrice) {
                this.pendingPrice = pendingPrice;
            }

        }
    }
}
