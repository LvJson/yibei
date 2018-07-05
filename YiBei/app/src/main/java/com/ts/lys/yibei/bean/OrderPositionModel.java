package com.ts.lys.yibei.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 持仓列表
 */
public class OrderPositionModel extends ErrorStatus {

    /**
     * data : {"traderOrder":[{"symbolCn":"欧元/美元","symbolEn":"EURUSD","cmd":0,"volume":0.01,"openPrice":1.22825,"closePrice":1.22785,"market":null,"lever":100,"profit":-0.4,"ticket":"13265264","contractSize":100000,"profitChange":false,"profitUSDPrex":false,"profitCalCurrency":"USD","bid":null,"ask":null,"occupyAsset":null,"marginRate":1.23,"margin":0,"tp":0,"sl":0,"openTime":"2018-04-05 11:21:32","swaps":0,"commission":0,"stopsLevel":30,"multiply":100000,"digits":5,"profitCurrency":null,"profitRate":null,"isCopy":0,"username":null,"userCode":null}]}
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
        private List<TraderOrderBean> traderOrder;

        public List<TraderOrderBean> getTraderOrder() {
            return traderOrder;
        }

        public void setTraderOrder(List<TraderOrderBean> traderOrder) {
            this.traderOrder = traderOrder;
        }

        public static class TraderOrderBean implements Serializable {
            /**
             * symbolCn : 欧元/美元
             * symbolEn : EURUSD
             * cmd : 0
             * volume : 0.01
             * openPrice : 1.22825
             * closePrice : 1.22785
             * market : null
             * lever : 100
             * profit : -0.4
             * ticket : 13265264
             * contractSize : 100000.0
             * profitChange : false
             * profitUSDPrex : false
             * profitCalCurrency : USD
             * bid : null
             * ask : null
             * occupyAsset : null
             * marginRate : 1.23
             * margin : 0
             * tp : 0.0
             * sl : 0.0
             * openTime : 2018-04-05 11:21:32
             * swaps : 0.0 //库存费
             * commission : 0.0 // 手续费
             * stopsLevel : 30
             * multiply : 100000
             * digits : 5
             * profitCurrency : null
             * profitRate : null
             * isCopy : 0
             * username : null
             * userCode : null
             */

            private String symbolCn;
            private String symbolEn;
            private int cmd;
            private double volume;
            private double openPrice;
            private double closePrice;
            private double market;
            private int lever;
            private double profit;
            private String ticket;
            private int contractSize;
            private boolean profitChange;
            private boolean profitUSDPrex;
            private String profitCalCurrency;
            private double bid;
            private double ask;
            private double occupyAsset;
            private double marginRate;
            private double margin;
            private double tp;
            private double sl;
            private String openTime;
            private double swaps;
            private double commission;
            private int stopsLevel;
            private int multiply;
            private String profitCurrency;
            private double profitRate;
            private int digits;
            private int isCopy;
            private String username;
            private String userCode;

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

            public double getOpenPrice() {
                return openPrice;
            }

            public void setOpenPrice(double openPrice) {
                this.openPrice = openPrice;
            }

            public double getClosePrice() {
                return closePrice;
            }

            public void setClosePrice(double closePrice) {
                this.closePrice = closePrice;
            }

            public double getMarket() {
                return market;
            }

            public void setMarket(double market) {
                this.market = market;
            }

            public int getLever() {
                return lever;
            }

            public void setLever(int lever) {
                this.lever = lever;
            }

            public double getProfit() {
                return profit;
            }

            public void setProfit(double profit) {
                this.profit = profit;
            }

            public String getTicket() {
                return ticket;
            }

            public void setTicket(String ticket) {
                this.ticket = ticket;
            }

            public int getContractSize() {
                return contractSize;
            }

            public void setContractSize(int contractSize) {
                this.contractSize = contractSize;
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

            public String getProfitCalCurrency() {
                return profitCalCurrency;
            }

            public void setProfitCalCurrency(String profitCalCurrency) {
                this.profitCalCurrency = profitCalCurrency;
            }

            public double getBid() {
                return bid;
            }

            public void setBid(double bid) {
                this.bid = bid;
            }

            public double getAsk() {
                return ask;
            }

            public void setAsk(double ask) {
                this.ask = ask;
            }

            public double getOccupyAsset() {
                return occupyAsset;
            }

            public void setOccupyAsset(double occupyAsset) {
                this.occupyAsset = occupyAsset;
            }

            public double getMarginRate() {
                return marginRate;
            }

            public void setMarginRate(double marginRate) {
                this.marginRate = marginRate;
            }

            public double getMargin() {
                return margin;
            }

            public void setMargin(double margin) {
                this.margin = margin;
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

            public String getOpenTime() {
                return openTime;
            }

            public void setOpenTime(String openTime) {
                this.openTime = openTime;
            }

            public double getSwaps() {
                return swaps;
            }

            public void setSwaps(double swaps) {
                this.swaps = swaps;
            }

            public double getCommission() {
                return commission;
            }

            public void setCommission(double commission) {
                this.commission = commission;
            }

            public int getStopsLevel() {
                return stopsLevel;
            }

            public void setStopsLevel(int stopsLevel) {
                this.stopsLevel = stopsLevel;
            }

            public int getMultiply() {
                return multiply;
            }

            public void setMultiply(int multiply) {
                this.multiply = multiply;
            }

            public String getProfitCurrency() {
                return profitCurrency;
            }

            public void setProfitCurrency(String profitCurrency) {
                this.profitCurrency = profitCurrency;
            }

            public double getProfitRate() {
                return profitRate;
            }

            public void setProfitRate(double profitRate) {
                this.profitRate = profitRate;
            }

            public int getDigits() {
                return digits;
            }

            public void setDigits(int digits) {
                this.digits = digits;
            }

            public int getIsCopy() {
                return isCopy;
            }

            public void setIsCopy(int isCopy) {
                this.isCopy = isCopy;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getUserCode() {
                return userCode;
            }

            public void setUserCode(String userCode) {
                this.userCode = userCode;
            }
        }
    }
}
