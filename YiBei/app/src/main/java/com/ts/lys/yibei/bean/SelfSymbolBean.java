package com.ts.lys.yibei.bean;

import java.util.ArrayList;

public class SelfSymbolBean extends ErrorStatus {

    /**
     * data : {"isSub":"false","symbols":["GOLD","AUDCHF","AUDDKK"]}
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
         * isSub : false
         * symbols : ["GOLD","AUDCHF","AUDDKK"]
         */

        private String isSub;
        private ArrayList<String> symbols;

        public String getIsSub() {
            return isSub;
        }

        public void setIsSub(String isSub) {
            this.isSub = isSub;
        }

        public ArrayList<String> getSymbols() {
            return symbols;
        }

        public void setSymbols(ArrayList<String> symbols) {
            this.symbols = symbols;
        }
    }
}
