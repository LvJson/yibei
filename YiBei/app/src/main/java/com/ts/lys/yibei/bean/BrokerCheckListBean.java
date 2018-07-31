package com.ts.lys.yibei.bean;

import java.util.List;

/**
 * 交易账户列表
 */
public class BrokerCheckListBean {
    /**
     * data : {"smart":{"accType":0,"name":"经纪商名字","icon":"图标","account":"mt4账号","brokerUrl":"链接","coment":"说明","password":"密码","destory":"2019-01-30","isNow":"false","status":2},"broker":[{"accType":0,"name":"TigerWit","icon":"iVBORw0KGgoAAAANSUhEUgAAADwAAAA8","account":83309027,"brokerUrl":"baidu.com","coment":"","password":"Lqy198816","destory":"","isNow":"true","status":0},{"accType":0,"name":"TigerWit","icon":"iVBORw0KGgoAAAANSUhEUgAAAD","account":48198,"brokerUrl":"baidu.com","coment":"请前往经纪商官网入金","password":"","destory":"","isNow":"false","status":""}]}
     * err_code : 0
     * err_msg : success
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
         * smart : {"accType":0,"name":"经纪商名字","icon":"图标","account":"mt4账号","brokerUrl":"链接","coment":"说明","password":"密码","destory":"2019-01-30","isNow":"false","status":2}
         * broker : [{"accType":0,"name":"TigerWit","icon":"iVBORw0KGgoAAAANSUhEUgAAADwAAAA8","account":83309027,"brokerUrl":"baidu.com","coment":"","password":"Lqy198816","destory":"","isNow":"true","status":0},{"accType":0,"name":"TigerWit","icon":"iVBORw0KGgoAAAANSUhEUgAAAD","account":48198,"brokerUrl":"baidu.com","coment":"请前往经纪商官网入金","password":"","destory":"","isNow":"false","status":""}]
         */

        private SmartBean smart;
        private List<BrokerBean> broker;

        public SmartBean getSmart() {
            return smart;
        }

        public void setSmart(SmartBean smart) {
            this.smart = smart;
        }

        public List<BrokerBean> getBroker() {
            return broker;
        }

        public void setBroker(List<BrokerBean> broker) {
            this.broker = broker;
        }

        public static class SmartBean {
            /**
             * accType : 0
             * name : 经纪商名字
             * icon : 图标
             * account : mt4账号
             * brokerUrl : 链接
             * coment : 说明
             * password : 密码
             * destory : 2019-01-30
             * isNow : false
             * status :  // 0 过期/登录失效 1 未登录 2 可用
             */

            private int accType;
            private String name;
            private String icon;
            private String account;
            private String brokerUrl;
            private String coment;
            private String password;
            private String destory;
            private Boolean isNow;
            private int status;

            public int getAccType() {
                return accType;
            }

            public void setAccType(int accType) {
                this.accType = accType;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public String getBrokerUrl() {
                return brokerUrl;
            }

            public void setBrokerUrl(String brokerUrl) {
                this.brokerUrl = brokerUrl;
            }

            public String getComent() {
                return coment;
            }

            public void setComent(String coment) {
                this.coment = coment;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getDestory() {
                return destory;
            }

            public void setDestory(String destory) {
                this.destory = destory;
            }

            public Boolean getNow() {
                return isNow;
            }

            public void setNow(Boolean now) {
                isNow = now;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }

        public static class BrokerBean {
            /**
             * accType : 0
             * name : TigerWit
             * icon : iVBORw0KGgoAAAANSUhEUgAAADwAAAA8
             * account : 83309027
             * brokerUrl : baidu.com
             * coment :
             * password : Lqy198816
             * destory :
             * isNow : true
             * status : 0
             */

            private int accType;// 经纪商代码 accType //1 老虎 2 昆仑 3 at 4 ph
            private String name;//经纪商名称
            private String icon;//经纪商图标
            private String account;//经纪商账号
            private String brokerUrl;//经纪商连接
            private String coment;//
            private String password;//经纪商密码
            private String destory;//是否过期
            private Boolean isNow;//是否当前选中
            private int status; // 0 过期/登录失效 1 未登录 2 可用

            public int getAccType() {
                return accType;
            }

            public void setAccType(int accType) {
                this.accType = accType;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public String getBrokerUrl() {
                return brokerUrl;
            }

            public void setBrokerUrl(String brokerUrl) {
                this.brokerUrl = brokerUrl;
            }

            public String getComent() {
                return coment;
            }

            public void setComent(String coment) {
                this.coment = coment;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getDestory() {
                return destory;
            }

            public void setDestory(String destory) {
                this.destory = destory;
            }

            public Boolean getNow() {
                return isNow;
            }

            public void setNow(Boolean now) {
                isNow = now;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
