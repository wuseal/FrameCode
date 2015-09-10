package com.dh.foundation.utils;

import java.util.Map;

/**
 * 请求参数构建接口
 * Created By: Seal.Wu
 * Date: 2015/4/7
 * Time: 9:59
 */
public interface IRequestParams {

    public void putParams(String key, String value);

    public void setParams(String key, String value);

    public void removeParams(String ke);

    public Map<String, String> getParams();

    void setParamsEncoding(String paramsEncoding);

    String getParamsEncoding();

}
