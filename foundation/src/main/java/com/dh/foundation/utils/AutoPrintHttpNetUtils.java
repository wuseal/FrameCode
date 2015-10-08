package com.dh.foundation.utils;

import android.content.Context;

import com.android.volley.Request;

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
        Request request = HttpNetUtils.buildGetRequestTask(baseAddress, requestParams, requestListener);
        return HttpNetUtils.addToExecuteQueue(request);
    }

    public static synchronized <T> Request postData(String baseAddress, RequestParams requestParams, RequestListener<T> requestListener) {
        printCompleteUrl(baseAddress, requestParams);
        Request request = HttpNetUtils.buildPostRequestTask(baseAddress, requestParams, requestListener);
        return HttpNetUtils.addToExecuteQueue(request);
    }




    public static synchronized <T> void getData(Context context, String baseAddress, RequestParams requestParams, Class<T> clazz, HttpJsonRequest<T> request) {
        printCompleteUrl(baseAddress, requestParams);
        HttpNetUtils.getData(context, baseAddress, requestParams, clazz, request);
    }

    public static synchronized <T> void postData(Context context, String baseAddress, RequestParams requestParams, Class<T> clazz, HttpJsonRequest<T> request) {
        printCompleteUrl(baseAddress, requestParams);
        HttpNetUtils.postData(context, baseAddress, requestParams, clazz, request);
    }

    public static synchronized <T> void getData(Context context, String baseAddress, RequestParams requestParams, Type type, HttpJsonRequest<T> request) {
        printCompleteUrl(baseAddress, requestParams);
        HttpNetUtils.getData(context, baseAddress, requestParams, type, request);
    }

    public static synchronized <T> void postData(Context context, String baseAddress, RequestParams requestParams, Type type, HttpJsonRequest<T> request) {
        printCompleteUrl(baseAddress, requestParams);
        HttpNetUtils.postData(context, baseAddress, requestParams, type, request);
    }

    public static synchronized <T> void getData(Context context, String baseAddress, RequestParams requestParams, RequestListener<T> requestListener) {
        printCompleteUrl(baseAddress, requestParams);
        HttpNetUtils.getData(context, baseAddress, requestParams, requestListener);
    }

    public static synchronized <T> void postData(Context context, String baseAddress, RequestParams requestParams, RequestListener<T> requestListener) {
        printCompleteUrl(baseAddress, requestParams);
        HttpNetUtils.postData(context, baseAddress, requestParams, requestListener);
    }
}