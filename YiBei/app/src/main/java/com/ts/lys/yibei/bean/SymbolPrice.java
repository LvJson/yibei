package com.ts.lys.yibei.bean;

/**
 * Created by jcdev1 on 2018/6/25.
 */

public class SymbolPrice extends ErrorStatus {

    /**
     * data : {"price":{"todayOpenPrice":1270.01,"todayHigh":1272.46,"yesterdayClosePrice":1269.24,"todayLow":1265.19}}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * price : {"todayOpenPrice":1270.01,"todayHigh":1272.46,"yesterdayClosePrice":1269.24,"todayLow":1265.19}
         */

        private PriceBean price;

        public PriceBean getPrice() {
            return price;
        }

        public void setPrice(PriceBean price) {
            this.price = price;
        }

        public static class PriceBean {
            /**
             * todayOpenPrice : 1270.01
             * todayHigh : 1272.46
             * yesterdayClosePrice : 1269.24
             * todayLow : 1265.19
             */

            private double todayOpenPrice;
            private double todayHigh;
            private double yesterdayClosePrice;
            private double todayLow;

            public double getTodayOpenPrice() {
                return todayOpenPrice;
            }

            public void setTodayOpenPrice(double todayOpenPrice) {
                this.todayOpenPrice = todayOpenPrice;
            }

            public double getTodayHigh() {
                return todayHigh;
            }

            public void setTodayHigh(double todayHigh) {
                this.todayHigh = todayHigh;
            }

            public double getYesterdayClosePrice() {
                return yesterdayClosePrice;
            }

            public void setYesterdayClosePrice(double yesterdayClosePrice) {
                this.yesterdayClosePrice = yesterdayClosePrice;
            }

            public double getTodayLow() {
                return todayLow;
            }

            public void setTodayLow(double todayLow) {
                this.todayLow = todayLow;
            }
        }
    }
}
