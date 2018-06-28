package com.ts.lys.yibei.mvpmodal;

/**
 * Created by jcdev1 on 2017/5/22.
 */

public interface IRequestServiceListener<V> {

    void success(V result);

    void faild(String msg);
}
