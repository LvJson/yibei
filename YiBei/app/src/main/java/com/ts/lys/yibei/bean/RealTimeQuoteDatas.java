package com.ts.lys.yibei.bean;

import java.util.List;

/**
 * Created by jcdev1 on 2017/5/17.
 */

public class RealTimeQuoteDatas extends ErrorStatus {

    /**
     * data : {"quote":[{"symbol":"AUDCAD","time":1495073139000,"ask":1.01427,"bid":1.01447,"market":1.01435}]}
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
        private List<QuoteBean> quote;

        public List<QuoteBean> getQuote() {
            return quote;
        }

        public void setQuote(List<QuoteBean> quote) {
            this.quote = quote;
        }

        public static class QuoteBean {
            /**
             * symbol : AUDCAD
             * time : 1495073139000
             * ask : 1.01427
             * bid : 1.01447
             * market : 1.01435
             */

            private String symbol;
            private long time;
            private double ask;
            private double bid;
            private double market;

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

            public double getAsk() {
                return ask;
            }

            public void setAsk(double ask) {
                this.ask = ask;
            }

            public double getBid() {
                return bid;
            }

            public void setBid(double bid) {
                this.bid = bid;
            }

            public double getMarket() {
                return market;
            }

            public void setMarket(double market) {
                this.market = market;
            }
        }
    }
}
