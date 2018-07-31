package com.ts.lys.yibei.bean;

import java.util.List;

/**
 * 账号信息
 */
public class UserAccInfoModel extends ErrorStatus {


    /**
     * data : {"totalVolume":0,"profitCount":0,"lossTrade":0,"destroyDay":-1,"profitMonth":{"month":["3","4","5","6","7"],"profit":[0,0,0,195.24,-34.29]},"headUrl":"http://forex-smart-trade-server.oss-cn-hangzhou.aliyuncs.com/IMG/UserImg/user_head_5493379_big.jpg","telephone":"18507150711","totalCount":0,"accStatus":2,"equity":653.1,"nick":"User_qji9","yieldRate":0,"accType":4,"profitTrade":0,"mt4Id":60029975}
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
         * totalVolume : 0
         * profitCount : 0
         * lossTrade : 0
         * destroyDay : -1
         * profitMonth : {"month":["3","4","5","6","7"],"profit":[0,0,0,195.24,-34.29]}
         * headUrl : http://forex-smart-trade-server.oss-cn-hangzhou.aliyuncs.com/IMG/UserImg/user_head_5493379_big.jpg
         * telephone : 18507150711
         * totalCount : 0
         * accStatus : 2
         * equity : 653.1
         * nick : User_qji9
         * yieldRate : 0
         * accType : 4
         * profitTrade : 0
         * mt4Id : 60029975
         */

        private double totalVolume;
        private double profitCount;
        private double lossTrade;
        private int destroyDay;
        private ProfitMonthBean profitMonth;
        private String headUrl;
        private String telephone;
        private int totalCount;
        private int accStatus;
        private double equity;
        private String nick;
        private double yieldRate;
        private int accType;
        private double profitTrade;
        private String mt4Id;

        public double getTotalVolume() {
            return totalVolume;
        }

        public void setTotalVolume(double totalVolume) {
            this.totalVolume = totalVolume;
        }

        public double getProfitCount() {
            return profitCount;
        }

        public void setProfitCount(double profitCount) {
            this.profitCount = profitCount;
        }

        public double getLossTrade() {
            return lossTrade;
        }

        public void setLossTrade(double lossTrade) {
            this.lossTrade = lossTrade;
        }

        public int getDestroyDay() {
            return destroyDay;
        }

        public void setDestroyDay(int destroyDay) {
            this.destroyDay = destroyDay;
        }

        public ProfitMonthBean getProfitMonth() {
            return profitMonth;
        }

        public void setProfitMonth(ProfitMonthBean profitMonth) {
            this.profitMonth = profitMonth;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getAccStatus() {
            return accStatus;
        }

        public void setAccStatus(int accStatus) {
            this.accStatus = accStatus;
        }

        public double getEquity() {
            return equity;
        }

        public void setEquity(double equity) {
            this.equity = equity;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public double getYieldRate() {
            return yieldRate;
        }

        public void setYieldRate(double yieldRate) {
            this.yieldRate = yieldRate;
        }

        public int getAccType() {
            return accType;
        }

        public void setAccType(int accType) {
            this.accType = accType;
        }

        public double getProfitTrade() {
            return profitTrade;
        }

        public void setProfitTrade(double profitTrade) {
            this.profitTrade = profitTrade;
        }

        public String getMt4Id() {
            return mt4Id;
        }

        public void setMt4Id(String mt4Id) {
            this.mt4Id = mt4Id;
        }

        public static class ProfitMonthBean {
            private List<String> month;
            private List<Double> profit;

            public List<String> getMonth() {
                return month;
            }

            public void setMonth(List<String> month) {
                this.month = month;
            }

            public List<Double> getProfit() {
                return profit;
            }

            public void setProfit(List<Double> profit) {
                this.profit = profit;
            }
        }
    }
}
