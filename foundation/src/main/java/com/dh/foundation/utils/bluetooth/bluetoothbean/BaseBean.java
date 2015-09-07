package com.dh.foundation.utils.bluetooth.bluetoothbean;

import java.io.Serializable;

/**
 * Created By: Seal.Wu
 * Date: 2015/4/10
 * Time: 17:35
 */
public class BaseBean<ReturnData> implements Serializable {
    private String ReturnCode;//	接口返回结果（1：成功，0：失败）
    private String ReturnMsg;//	返回消息的具体信息
    private ReturnData ReturnData;//	返回的数据对象
    private String ReturnTotalRecords;//返回的总条目数


    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String returnCode) {
        ReturnCode = returnCode;
    }

    public String getReturnMsg() {
        return ReturnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        ReturnMsg = returnMsg;
    }

    public ReturnData getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ReturnData returnData) {
        ReturnData = returnData;
    }

    public String getReturnTotalRecords() {
        return ReturnTotalRecords;
    }

    public void setReturnTotalRecords(String returnTotalRecords) {
        ReturnTotalRecords = returnTotalRecords;
    }
}
