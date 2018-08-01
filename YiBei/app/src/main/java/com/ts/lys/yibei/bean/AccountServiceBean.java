package com.ts.lys.yibei.bean;

import java.util.List;

public class AccountServiceBean extends ErrorStatus {


    /**
     * data : {"servers":["MMIGNZ-Live"],"icon":null,"name":"XXX","accName":null,"content":null}
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
         * servers : ["MMIGNZ-Live"]
         * icon : null
         * name : XXX
         * accName : null
         * content : null
         */

        private String icon;
        private String name;
        private String accName;
        private String content;
        private List<String> servers;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAccName() {
            return accName;
        }

        public void setAccName(String accName) {
            this.accName = accName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<String> getServers() {
            return servers;
        }

        public void setServers(List<String> servers) {
            this.servers = servers;
        }
    }
}
