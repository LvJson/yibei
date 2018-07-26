package com.ts.lys.yibei.bean;

/**
 * Created by jcdev1 on 2017/5/24.
 */

public class SymbolInfo extends ErrorStatus {


    /**
     * data : {"symbolInfo":{"contractSize":100,"marginBase":500,"minVolume":0.01,"stopsLevel":150,"type":2,"digits":2,"accAmt":0,"openAcc":false,"marginCalCurrency":"USD","profitCalCurrency":"USD","accType":-1,"mt4Id":null,"symbolCn":"黄金","margin":null,"marginChange":false,"usdprex":false,"profitChange":false,"profitUSDPrex":false,"open":true}}
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
         * symbolInfo : {"contractSize":100,"marginBase":500,"minVolume":0.01,"stopsLevel":150,"type":2,"digits":2,"accAmt":0,"openAcc":false,"marginCalCurrency":"USD","profitCalCurrency":"USD","accType":-1,"mt4Id":null,"symbolCn":"黄金","margin":null,"marginChange":false,"usdprex":false,"profitChange":false,"profitUSDPrex":false,"open":true}
         */

        private SymbolInfoBean symbolInfo;

        public SymbolInfoBean getSymbolInfo() {
            return symbolInfo;
        }

        public void setSymbolInfo(SymbolInfoBean symbolInfo) {
            this.symbolInfo = symbolInfo;
        }

        public static class SymbolInfoBean {
            /**
             * contractSize : 100.0
             * marginBase : 500
             * minVolume : 0.01
             * stopsLevel : 150
             * type : 2
             * digits : 2
             * accAmt : 0
             * openAcc : false
             * marginCalCurrency : USD
             * profitCalCurrency : USD
             * accType : -1
             * mt4Id : null
             * symbolCn : 黄金
             * margin : null
             * marginChange : false
             * usdprex : false
             * profitChange : false
             * profitUSDPrex : false
             * open : true
             */

            private double contractSize;
            private double marginBase;
            private double minVolume;
            private int stopsLevel;
            private int type;
            private int digits;
            private double accAmt;
            private boolean openAcc;
            private String marginCalCurrency;
            private String profitCalCurrency;
            private int accType;
            private String mt4Id;
            private String symbolCn;
            private double margin;
            private boolean marginChange;
            private boolean usdprex;
            private boolean profitChange;
            private boolean profitUSDPrex;
            private boolean open;


            public double getContractSize() {
                return contractSize;
            }

            public void setContractSize(double contractSize) {
                this.contractSize = contractSize;
            }

            public double getMarginBase() {
                return marginBase;
            }

            public void setMarginBase(double marginBase) {
                this.marginBase = marginBase;
            }

            public double getMinVolume() {
                return minVolume;
            }

            public void setMinVolume(double minVolume) {
                this.minVolume = minVolume;
            }

            public int getStopsLevel() {
                return stopsLevel;
            }

            public void setStopsLevel(int stopsLevel) {
                this.stopsLevel = stopsLevel;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getDigits() {
                return digits;
            }

            public void setDigits(int digits) {
                this.digits = digits;
            }

            public double getAccAmt() {
                return accAmt;
            }

            public void setAccAmt(double accAmt) {
                this.accAmt = accAmt;
            }

            public boolean isOpenAcc() {
                return openAcc;
            }

            public void setOpenAcc(boolean openAcc) {
                this.openAcc = openAcc;
            }

            public String getMarginCalCurrency() {
                return marginCalCurrency;
            }

            public void setMarginCalCurrency(String marginCalCurrency) {
                this.marginCalCurrency = marginCalCurrency;
            }

            public String getProfitCalCurrency() {
                return profitCalCurrency;
            }

            public void setProfitCalCurrency(String profitCalCurrency) {
                this.profitCalCurrency = profitCalCurrency;
            }

            public int getAccType() {
                return accType;
            }

            public void setAccType(int accType) {
                this.accType = accType;
            }

            public String getMt4Id() {
                return mt4Id;
            }

            public void setMt4Id(String mt4Id) {
                this.mt4Id = mt4Id;
            }

            public String getSymbolCn() {
                return symbolCn;
            }

            public void setSymbolCn(String symbolCn) {
                this.symbolCn = symbolCn;
            }

            public double getMargin() {
                return margin;
            }

            public void setMargin(double margin) {
                this.margin = margin;
            }

            public boolean isMarginChange() {
                return marginChange;
            }

            public void setMarginChange(boolean marginChange) {
                this.marginChange = marginChange;
            }

            public boolean isUsdprex() {
                return usdprex;
            }

            public void setUsdprex(boolean usdprex) {
                this.usdprex = usdprex;
            }

            public boolean isProfitChange() {
                return profitChange;
            }

            public void setProfitChange(boolean profitChange) {
                this.profitChange = profitChange;
            }

            public boolean isProfitUSDPrex() {
                return profitUSDPrex;
            }

            public void setProfitUSDPrex(boolean profitUSDPrex) {
                this.profitUSDPrex = profitUSDPrex;
            }

            public boolean isOpen() {
                return open;
            }

            public void setOpen(boolean open) {
                this.open = open;
            }
        }
    }
}
