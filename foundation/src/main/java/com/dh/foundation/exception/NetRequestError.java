package com.dh.foundation.exception;

/**
 * 网络请求出错
 * Created By: Seal.Wu
 * Date: 2015/10/8
 * Time: 14:23
 */
public class NetRequestError extends Exception {


    public NetRequestError() {
        this("网络请求出错");
    }

    public NetRequestError(String detailMessage) {
        super(detailMessage);
    }

    public NetRequestError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NetRequestError(Throwable throwable) {
        super("网络请求出错", throwable);
    }

}
