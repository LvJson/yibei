package com.ts.lys.yibei.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 获取行情
 */

public class GetQuotesModel {

    /**
     * data : {"symbols":[{"symbolEn":"AUDCAD50","symbolCn":"澳元加元","ask":0.96709,"bid":0.96676,"market":0.96676,"spread":33,"digits":5,"yesterdayClosePrice":0.96804,"gains":-0.13,"group":0,"open":1}]}
     * err_code : 0
     * err_msg : successe
     */

    private DataBean data;
    private String err_code;
    private String err_msg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    public static class DataBean {
        private List<SymbolsBean> symbols;

        public List<SymbolsBean> getSymbols() {
            return symbols;
        }

        public void setSymbols(List<SymbolsBean> symbols) {
            this.symbols = symbols;
        }

        public static class SymbolsBean implements Serializable {
            /**
             * symbolEn : AUDCAD50
             * symbolCn : 澳元加元
             * ask : 0.96709
             * bid : 0.96676
             * market : 0.96676
             * spread : 33
             * digits : 5
             * yesterdayClosePrice : 0.96804
             * gains : -0.13
             * group : 0
             * isOpen : 1
             */

            private String symbolEn;//英文名
            private String symbolCn;//中文名
            private double ask;//买入价格
            private double bid;//卖出价格
            private double market; //市场价
            private int spread;//点差
            private int digits = -1;//小数点位数
            private double yesterdayClosePrice;//昨日收盘价格
            private double gains;//涨跌幅
            private int group = -1;//品种组 0 外汇；1指数；2 贵金属；3 能源
            private String isOpen;//当前是否开盘  1为开盘中 0为停盘
            //自定义属性
            private double difference = 0;//与上次是否有所不同(涨跌价)
            private int position = -1;//该数据所处位置
            private int state = 0;//涨跌显示 1 红 -1 绿

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

            public int getSpread() {
                return spread;
            }

            public void setSpread(int spread) {
                this.spread = spread;
            }

            public int getDigits() {
                return digits;
            }

            public void setDigits(int digits) {
                this.digits = digits;
            }

            public double getYesterdayClosePrice() {
                return yesterdayClosePrice;
            }

            public void setYesterdayClosePrice(double yesterdayClosePrice) {
                this.yesterdayClosePrice = yesterdayClosePrice;
            }

            public double getGains() {
                return gains;
            }

            public void setGains(double gains) {
                this.gains = gains;
            }

            public int getGroup() {
                return group;
            }

            public void setGroup(int group) {
                this.group = group;
            }

            public String getIsOpen() {
                return isOpen;
            }

            public void setIsOpen(String isOpen) {
                this.isOpen = isOpen;
            }

            public double getDifference() {
                return difference;
            }

            public void setDifference(double difference) {
                this.difference = difference;
            }

            public int getPosition() {
                return position;
            }

            public void setPosition(int position) {
                this.position = position;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }
        }
    }
}
