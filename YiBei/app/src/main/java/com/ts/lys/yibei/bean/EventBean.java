package com.ts.lys.yibei.bean;

/**
 * Created by jcdev1 on 2018/1/2.
 */

public class EventBean {
    private String tagOne;
    private String tagTwo;

    private int tagThree;
    private int tagFour;

    private Object response;

    public EventBean(String tagOne, Object response) {
        this.tagOne = tagOne;
        this.response = response;
    }

    public String getTagOne() {
        return tagOne;
    }

    public void setTagOne(String tagOne) {
        this.tagOne = tagOne;
    }

    public String getTagTwo() {
        return tagTwo;
    }

    public void setTagTwo(String tagTwo) {
        this.tagTwo = tagTwo;
    }

    public int getTagThree() {
        return tagThree;
    }

    public void setTagThree(int tagThree) {
        this.tagThree = tagThree;
    }

    public int getTagFour() {
        return tagFour;
    }

    public void setTagFour(int tagFour) {
        this.tagFour = tagFour;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
