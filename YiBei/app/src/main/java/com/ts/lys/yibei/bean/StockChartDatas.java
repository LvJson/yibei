package com.ts.lys.yibei.bean;

import java.util.List;

/**
 * Created by jcdev1 on 2017/5/18.
 */

public class StockChartDatas extends ErrorStatus {

    /**
     * data : {"quotes":[{"symbol":"EURAUD","time":1495087200000,"openPrice":1.49371,"high":1.49383,"low":1.49365,"close":1.49381,"vol":89},{"symbol":"EURAUD","time":1495053300000,"openPrice":1.50163,"high":1.50171,"low":1.50148,"close":1.50162,"vol":89}]}
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
        private List<QuotesBean> quotes;

        public List<QuotesBean> getQuotes() {
            return quotes;
        }

        public void setQuotes(List<QuotesBean> quotes) {
            this.quotes = quotes;
        }

        public static class QuotesBean {
            /**
             * symbol : EURAUD
             * time : 1495087200000
             * openPrice : 1.49371
             * high : 1.49383
             * low : 1.49365
             * close : 1.49381
             * vol : 89
             */

            private String symbol;
            private long time;
            private double openPrice;
            private double high;
            private double low;
            private double close;
            private int vol;

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public double getOpenPrice() {
                return openPrice;
            }

            public void setOpenPrice(double openPrice) {
                this.openPrice = openPrice;
            }

            public double getHigh() {
                return high;
            }

            public void setHigh(double high) {
                this.high = high;
            }

            public double getLow() {
                return low;
            }

            public void setLow(double low) {
                this.low = low;
            }

            public double getClose() {
                return close;
            }

            public void setClose(double close) {
                this.close = close;
            }

            public int getVol() {
                return vol;
            }

            public void setVol(int vol) {
                this.vol = vol;
            }
        }
    }
}
