package com.dh.foundation.utils;

import com.dh.foundation.volley.Request;

import java.lang.reflect.Type;

/**
 * 带自动打印网络请求全地址日志的请求工具
 * Created By: Seal.Wu
 * Date: 2015/4/10
 * Time: 17:23
 */
public class AutoPrintHttpNetUtils extends HttpNetUtils {

    public static synchronized <T> Request getData(String baseAddress, RequestParams requestParams, Class<T> clazz, HttpJsonRequest<T> request) {
        printCompleteUrl(baseAddress, requestParams);
        return HttpNetUtils.getData(baseAddress, requestParams, clazz, request);
    }

    public static synchronized <T> void postData(String baseAddress, RequestParams requestParams, Class<T> clazz, HttpJsonRequest<T> request) {
        printCompleteUrl(baseAddress, requestParams);
        HttpNetUtils.postData(baseAddress, requestParams, clazz, request);
    }

    public static synchronized <T> Request getData(String baseAddress, RequestParams requestParams, Type type, HttpJsonRequest<T> request) {
        printCompleteUrl(baseAddress, requestParams);
        return HttpNetUtils.getData(baseAddress, requestParams, type, request);
    }

    public static synchronized <T> Request postData(String baseAddress, RequestParams requestParams, Type type, HttpJsonRequest<T> request) {
        printCompleteUrl(baseAddress, requestParams);
        return HttpNetUtils.postData(baseAddress, requestParams, type, request);
    }

    public static synchronized <T> Request getData(String baseAddress, RequestParams requestParams, RequestListener<T> requestListener) {
        printCompleteUrl(baseAddress, requestParams);
        Request request = buildGetRequestTask(baseAddress, requestParams, requestListener);
        return addToExecuteQueue(request);
    }

    public static synchronized <T> Request postData(String baseAddress, RequestParams requestParams, RequestListener<T> requestListener) {
        printCompleteUrl(baseAddress, requestParams);
        Request request = buildPostRequestTask(baseAddress, requestParams, requestListener);
        return addToExecuteQueue(request);
    }
}