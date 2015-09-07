package com.dh.foundation.exception;

import com.dh.foundation.utils.bluetooth.bluetoothbean.BaseBean;

/**
 * Created By: Seal.Wu
 * Date: 2015/8/6
 * Time: 13:38
 * <p/>
 * 专用网络请求异常类
 */
public class DhRequestError extends Exception {

    private BaseBean baseBean;

    public DhRequestError(String detailMessage) {
        super(detailMessage);
    }


    public DhRequestError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DhRequestError(Throwable throwable) {
        super(throwable);
    }


    public DhRequestError(BaseBean baseBean) {
        super(baseBean.getReturnMsg());
        this.baseBean = baseBean;
    }


    public BaseBean getBaseBean() {
        return baseBean;
    }

    public void setBaseBean(BaseBean baseBean) {
        this.baseBean = baseBean;
    }
}