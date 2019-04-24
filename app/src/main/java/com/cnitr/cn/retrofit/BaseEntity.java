package com.cnitr.cn.retrofit;

import java.io.Serializable;

/**
 * BaseEntity
 * <p>
 * Created by yangcheng on 2018/6/21.
 */

public class BaseEntity<T> implements Serializable {

    // 请求结果码
    public int code;

    // 请求信息
    public String msg;

    public T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
