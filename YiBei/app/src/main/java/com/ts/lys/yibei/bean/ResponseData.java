package com.ts.lys.yibei.bean;

/**
 * Created by jcdev1 on 2018/6/27.
 */

public class ResponseData<T> {

    public String err_code;
    public String err_msg;
    public T data;


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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
