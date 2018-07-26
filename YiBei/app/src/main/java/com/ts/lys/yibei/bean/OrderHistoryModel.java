package com.ts.lys.yibei.bean;

import java.util.List;

/**
 * Created by jcdev1 on 2018/7/23.
 */

public class OrderHistoryModel extends ErrorStatus {

    private PageBean page;
    private DataBean data;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class PageBean {
        /**
         * pageSize : 10
         * currentPage : 1
         * totalNum : 0
         * pageNum : null
         */

        private int pageSize;
        private int currentPage;
        private int totalNum;
        private int pageNum;

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }
    }

    public static class DataBean {
        private List<HistoryOrderBean> historyOrder;

        public List<HistoryOrderBean> getHistoryOrder() {
            return historyOrder;
        }

        public void setHistoryOrder(List<HistoryOrderBean> historyOrder) {
            this.historyOrder = historyOrder;
        }

        public static class HistoryOrderBean {
            /**
             * ticket : 423884605846716416
             * openPrice : 83.423
             * lever : 200
             * market : 82.268
             * volume : 0.1
             * symbolCn : 澳元日元
             * closeTypeName : 手动平仓
             * symbolEn : AUDJPY200
             * closeTime : 2018-03-16 18:46:13
             * cmd : 1
             * closePrice : 82.268
             * openTime : 2018-03-15 16:46:05
             * profit : 109.28
             */

            private String ticket;
            private double openPrice;
            private int lever;
            private double market;
            private double volume;
            private String symbolCn;
            private String closeTypeName;
            private int closeType;
            private String symbolEn;
            private String closeTime;
            private int cmd;
            private double closePrice;
            private String openTime;
            private double profit;
            private boolean isShow = false;

            private double sl;
            private double tp;
            private double commission;
            private double swaps;
            private double margin;

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

            public double getVolume() {
                return volume;
            }

            public void setVolume(double volume) {
                this.volume = volume;
            }

            public String getSymbolCn() {
                return symbolCn;
            }

            public void setSymbolCn(String symbolCn) {
                this.symbolCn = symbolCn;
            }

            public String getCloseTypeName() {
                return closeTypeName;
            }

            public void setCloseTypeName(String closeTypeName) {
                this.closeTypeName = closeTypeName;
            }

            public int getCloseType() {
                return closeType;
            }

            public void setCloseType(int closeType) {
                this.closeType = closeType;
            }

            public String getSymbolEn() {
                return symbolEn;
            }

            public void setSymbolEn(String symbolEn) {
                this.symbolEn = symbolEn;
            }

            public String getCloseTime() {
                return closeTime;
            }

            public void setCloseTime(String closeTime) {
                this.closeTime = closeTime;
            }

            public int getCmd() {
                return cmd;
            }

            public void setCmd(int cmd) {
                this.cmd = cmd;
            }

            public double getClosePrice() {
                return closePrice;
            }

            public void setClosePrice(double closePrice) {
                this.closePrice = closePrice;
            }

            public String getOpenTime() {
                return openTime;
            }

            public void setOpenTime(String openTime) {
                this.openTime = openTime;
            }

            public double getProfit() {
                return profit;
            }

            public void setProfit(double profit) {
                this.profit = profit;
            }

            public boolean isShow() {
                return isShow;
            }

            public void setShow(boolean show) {
                isShow = show;
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

            public double getCommission() {
                return commission;
            }

            public void setCommission(double commission) {
                this.commission = commission;
            }

            public double getSwaps() {
                return swaps;
            }

            public void setSwaps(double swaps) {
                this.swaps = swaps;
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
