package com.dh.foundation.exception;

import com.dh.foundation.utils.bluetooth.bluetoothbean.BaseBean;

/**
 * 大含专用服务器返回错误信息
 * Created By: Seal.Wu
 * Date: 2015/10/8
 * Time: 15:06
 */
public class DhBaseBeanError extends Error{

    private BaseBean baseBean;


    public DhBaseBeanError(BaseBean baseBean) {

        super(baseBean.getReturnMsg());

        this.baseBean = baseBean;
    }

    public DhBaseBeanError(String detailMessage, BaseBean baseBean) {

        super(detailMessage);

        this.baseBean = baseBean;
    }

    public DhBaseBeanError(String detailMessage, Throwable throwable, BaseBean baseBean) {

        super(detailMessage, throwable);

        this.baseBean = baseBean;
    }

    public DhBaseBeanError(Throwable throwable, BaseBean baseBean) {

        super(baseBean.getReturnMsg(),throwable);

        this.baseBean = baseBean;
    }

    public BaseBean getBaseBean() {
        return baseBean;
    }

    public void setBaseBean(BaseBean baseBean) {
        this.baseBean = baseBean;
    }
}
