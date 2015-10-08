package com.dh.foundation.exception;

/**
 * 数据格式错误
 * Created By: Seal.Wu
 * Date: 2015/10/8
 * Time: 14:19
 */
public class DataFormatError extends Error {

    public DataFormatError() {

        this("获取到的数据格式错误");

    }

    public DataFormatError(String detailMessage) {
        super(detailMessage);
    }

    public DataFormatError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DataFormatError(Throwable throwable) {
        super("获取到的数据格式错误", throwable);
    }
}
