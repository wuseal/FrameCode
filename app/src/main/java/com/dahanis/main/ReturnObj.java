package com.dahanis.main;

import java.util.List;

/**
 * 服务器返回的对象
 * Created By: Seal.Wu
 * Date: 2015/9/30
 * Time: 10:22
 */
public class ReturnObj {


    private String ReturnCode;
    private String ReturnMsg;
    private List<TruckBean> ReturnData;


    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String returnCode) {
        this.ReturnCode = returnCode;
    }

    public String getReturnMessage() {
        return ReturnMsg;
    }

    public void setReturnMessage(String returnMessage) {
        this.ReturnMsg = returnMessage;
    }

    public List<TruckBean> getTruckBeanList() {
        return ReturnData;
    }

    public void setTruckBeanList(List<TruckBean> truckBeanList) {
        this.ReturnData = truckBeanList;
    }
}
