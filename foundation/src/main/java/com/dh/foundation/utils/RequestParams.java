package com.dh.foundation.utils;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求参数配置集合类
 * Created By: Seal.Wu
 * Date: 2015/4/7
 * Time: 10:57
 */
public class RequestParams<ParamsObj> implements IRequestParams {

    private final static Gson gson = new Gson();

    private transient String paramsEncoding = "utf-8";

    private transient Map<String, String> params;

    private transient Map<String, String> headers;

    private transient boolean isRestStyle = false;

    private ParamsObj data;

    public RequestParams() {
        this.params = new HashMap<String, String>(4);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public void setParamsEncoding(String paramsEncoding) {
        this.paramsEncoding = paramsEncoding;
    }

    @Override
    public String getParamsEncoding() {
        return paramsEncoding;
    }

    @Override
    public void putParams(String key, String value) {
        params.put(key, value);
    }

    @Override
    public void setParams(String key, String value) {
        params.put(key, value);
    }

    @Override
    public void removeParams(String key) {
        params.remove(key);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public String getParam(String key) {
        return params.get(key);
    }

    private String encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString();
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }


    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    /**
     * 是否为rest风格请求
     *
     * @return 是或否
     */
    public boolean isRestStyle() {
        return isRestStyle;
    }

    /**
     * 设置是否为rest风格请求
     *
     * @param isRestStyle 是或否
     */
    public void setIsRestStyle(boolean isRestStyle) {
        this.isRestStyle = isRestStyle;
    }

    /**
     * 当为restStyle样式时要传入的参数对象
     *
     * @return 参数对象
     */
    public ParamsObj getParamsObj() {
        return data;
    }

    /**
     * 设置参数对象
     *
     * @param paramsObj 参数对象
     */
    public void setParamsObj(ParamsObj paramsObj) {
        this.data = paramsObj;
    }

    @Override
    public String toString() {
        return encodeParameters(this.params, paramsEncoding);
    }


    public String getParamObjJsonData() {

        if (getParamsObj() != null) {

             return gson.toJson(getParamsObj());
        }

        return gson.toJson(new Object());
    }
}
