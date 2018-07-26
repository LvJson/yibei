package com.ts.lys.yibei.bean;

/**
 * Created by jcdev1 on 2018/7/23.
 */

public class TraderAccInfoModel extends ErrorStatus {

    /**
     * data : {"margin":20,"totalProfit":-2.2,"balance":19997.83,"balanceNotIncludeCredit":19997.83,"credit":0,"marginFree":19975.63,"equity":19995.63,"accStatus":2,"marginLevel":99978.15}
     */

    private TraderAccInfoModel.DataBean data;

    public TraderAccInfoModel.DataBean getData() {
        return data;
    }

    public void setData(TraderAccInfoModel.DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * margin : 20.0 //保证金
         * totalProfit : -2.2//总盈利
         * balance : 19997.83//余额
         * balanceNotIncludeCredit : 19997.83//余额
         * credit : 0.0
         * marginFree : 19975.63 //剩余保证金
         * equity : 19995.63 // 净值
         * accStatus : 2 //账户状态 0不可用 2 可用（有MT4ID）
         * marginLevel : 99978.15//保证金比例
         */
        private Double margin;
        private Double totalProfit;
        private Double balance;
        private Double balanceNotIncludeCredit;
        private Double credit;
        private Double marginFree;
        private Double equity;
        private int accStatus;
        private Double marginLevel;

        private Double countProfitRate;
        private int accType;
        private String mt4Id;

        private boolean tost = false;

        public Double getMargin() {
            return margin;
        }

        public void setMargin(Double margin) {
            this.margin = margin;
        }

        public Double getTotalProfit() {
            return totalProfit;
        }

        public void setTotalProfit(Double totalProfit) {
            this.totalProfit = totalProfit;
        }

        public Double getBalance() {
            return balance;
        }

        public void setBalance(Double balance) {
            this.balance = balance;
        }

        public Double getBalanceNotIncludeCredit() {
            return balanceNotIncludeCredit;
        }

        public void setBalanceNotIncludeCredit(Double balanceNotIncludeCredit) {
            this.balanceNotIncludeCredit = balanceNotIncludeCredit;
        }

        public Double getCredit() {
            return credit;
        }

        public void setCredit(Double credit) {
            this.credit = credit;
        }

        public Double getMarginFree() {
            return marginFree;
        }

        public void setMarginFree(Double marginFree) {
            this.marginFree = marginFree;
        }

        public Double getEquity() {
            return equity;
        }

        public void setEquity(Double equity) {
            this.equity = equity;
        }

        public int getAccStatus() {
            return accStatus;
        }

        public void setAccStatus(int accStatus) {
            this.accStatus = accStatus;
        }

        public Double getMarginLevel() {
            return marginLevel;
        }

        public void setMarginLevel(Double marginLevel) {
            this.marginLevel = marginLevel;
        }

        public Double getCountProfitRate() {
            return countProfitRate;
        }

        public void setCountProfitRate(Double countProfitRate) {
            this.countProfitRate = countProfitRate;
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

        public boolean isTost() {
            return tost;
        }

        public void setTost(boolean tost) {
            this.tost = tost;
        }
    }
}
