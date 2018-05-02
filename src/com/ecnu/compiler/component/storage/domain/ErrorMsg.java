package com.ecnu.compiler.component.storage.domain;

import com.ecnu.compiler.constant.StatusCode;

import java.util.Date;

/**
 * 错误消息
 *
 * @author Michael Chen
 * @date 2018-05-01 21:29
 */
public class ErrorMsg {
    private int code;
    private String msg;
    private Date time;
    private StatusCode statusCode;

    public ErrorMsg(int code, String msg, StatusCode statusCode) {
        this.code = code;
        this.msg = msg;
        this.time = new Date();
        this.statusCode = statusCode;
    }

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
