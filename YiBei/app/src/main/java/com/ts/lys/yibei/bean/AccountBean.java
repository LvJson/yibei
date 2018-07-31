package com.ts.lys.yibei.bean;

public class AccountBean {

    private String accName;
    private int accIcon;
    private String explain;
    private int accType;
    private int validity;

    public AccountBean(String accName, int accIcon, String explain, int accType, int validity) {
        this.accName = accName;
        this.accIcon = accIcon;
        this.explain = explain;
        this.accType = accType;
        this.validity = validity;
    }

    public int getValidity() {
        return validity;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public int getAccIcon() {
        return accIcon;
    }

    public void setAccIcon(int accIcon) {
        this.accIcon = accIcon;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public int getAccType() {
        return accType;
    }

    public void setAccType(int accType) {
        this.accType = accType;
    }
}
