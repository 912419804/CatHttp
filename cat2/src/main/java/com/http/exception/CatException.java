package com.http.exception;

/**
 * 用于全局异常处理
 */

public class CatException extends Exception {

    /**
     * 默认值 -1 代表java异常，因为java异常没有返回码
     * 其他为服务器返回的状态码
     */
    public int statusCode = -1;


    public CatException(int statusCode, String msg) {
        this(msg);
        this.statusCode = statusCode;
    }

    public CatException(String detailMessage) {
        super(detailMessage);
    }

    public CatException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public CatException(Throwable throwable) {
        super(throwable);
    }
}
