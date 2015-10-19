package com.dh.foundation.utils;

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
public class RequestParams implements IRequestParams {

    private String paramsEncoding = "utf-8";
    private Map<String, String> params;

    private Map<String,String> headers;

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

    @Override
    public String toString() {
        return encodeParameters(this.params, paramsEncoding);
    }
}
