package com.dh.foundation.exception;

import com.dh.foundation.utils.bluetooth.bluetoothbean.BaseBean;

/**
 * 请求异常类
 * Created By: Seal.Wu
 * Date: 2015/8/6
 * Time: 13:38
 * <p/>
 */
public class DhRequestError extends Exception {

    private Throwable throwable;

    public DhRequestError(String detailMessage) {
        super(detailMessage);
    }


    public DhRequestError(String detailMessage, Throwable throwable) {

        super(detailMessage, throwable);

        this.throwable = throwable;

    }

    public DhRequestError(Throwable throwable) {

        super(throwable);

        this.throwable = throwable;
    }


    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

}