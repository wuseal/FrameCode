package com.dh.foundation.exception;

/**
 * Created By: Seal.Wu
 * Date: 2017/9/14
 * Time: 18:26
 */

public class ServerReturnError extends Exception {

    public ServerReturnError(String message) {
        super(message);
    }
    private Object returnData;

    public Object getReturnData() {
        return returnData;
    }

    public void setReturnData(Object returnData) {
        this.returnData = returnData;
    }
}
