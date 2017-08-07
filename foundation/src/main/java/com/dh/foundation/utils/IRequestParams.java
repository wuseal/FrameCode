package com.dh.foundation.utils;

import java.util.Map;

/**
 * 请求参数构建接口
 * Created By: Seal.Wu
 * Date: 2015/4/7
 * Time: 9:59
 */
public interface IRequestParams {

    void setHeaders(Map<String, String> headers);

    Map<String, String> getHeaders();

    void putParams(String key, String value);

    void setParams(String key, String value);

    void removeParams(String ke);

    Map<String, String> getParams();

    String getParam(String key);


    void setParamsEncoding(String paramsEncoding);

    String getParamsEncoding();

    /**
     * 获取json参数对象的json字符数据
     *
     * @return json参数对象的json字符数据
     */
    String getParamObjJsonData();

}
