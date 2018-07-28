package com.ts.lys.yibei.bean;
/**
 * 账号信息
 * */
public class UserAccInfoModel {


    /**
     * data : {"nick":"Trader..u","profitCount":104.97,"yieldRate":10.497,"destroyDay":13,"headUrl":"http://msg.trademaster168.com/img/userPortrait/man.jpg","accType":0,"accStatus":2,"mt4Id":null,"equity":1104.97,"email":"249762619@qq.com"}
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
        /**
         * nick : Trader..u
         * profitCount : 104.97
         * yieldRate : 10.497
         * destroyDay : 13
         * headUrl : http://msg.trademaster168.com/img/userPortrait/man.jpg
         * accType : 0
         * accStatus : 2
         * mt4Id : null
         * equity : 1104.97
         * email : 249762619@qq.com
         */

        private String nick;
        private double profitCount;
        private double yieldRate;
        private int destroyDay;
        private String headUrl;
        private int accType;
        private int accStatus;
        private String mt4Id;
        private double equity;
        private String email;
        private String telephone;

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public double getProfitCount() {
            return profitCount;
        }

        public void setProfitCount(double profitCount) {
            this.profitCount = profitCount;
        }

        public double getYieldRate() {
            return yieldRate;
        }

        public void setYieldRate(double yieldRate) {
            this.yieldRate = yieldRate;
        }

        public int getDestroyDay() {
            return destroyDay;
        }

        public void setDestroyDay(int destroyDay) {
            this.destroyDay = destroyDay;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public int getAccType() {
            return accType;
        }

        public void setAccType(int accType) {
            this.accType = accType;
        }

        public int getAccStatus() {
            return accStatus;
        }

        public void setAccStatus(int accStatus) {
            this.accStatus = accStatus;
        }

        public String getMt4Id() {
            return mt4Id;
        }

        public void setMt4Id(String mt4Id) {
            this.mt4Id = mt4Id;
        }

        public double getEquity() {
            return equity;
        }

        public void setEquity(double equity) {
            this.equity = equity;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }
    }
}
