package com.dh.foundation.utils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.dh.foundation.manager.FoundationManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * 网络请求工具类
 * Created By: Seal.Wu
 * Date: 2014/12/21
 * Time: 23:26
 */
public class HttpNetUtils {

    public static final String LOG_TAG = "HttpNetUtils";

    private final static Gson prettyFormatGson = new GsonBuilder().setPrettyPrinting().create();

    private static RequestQueue mRequestQueue = Volley.newRequestQueue(FoundationManager.getContext());

    private final static Gson gson = new Gson();


    public static RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    /**
     * GET请求网络接口数据
     *
     * @param baseAddress     基地址
     * @param requestParams   参数
     * @param type            返回对象类类型
     * @param requestListener 返回接收器
     * @param <T>             返回对象类类型
     * @deprecated
     */
    public static synchronized <T> Request getData(String baseAddress, final RequestParams requestParams, final Type type, final HttpJsonRequest<T> requestListener) {

        baseAddress += baseAddress.contains("?") ? "&" : "?";

        final String url = baseAddress + (requestParams == null ? "" : requestParams.toString());

        Request request = new NetRequest<T>(Request.Method.GET, url, requestParams, type, requestListener);

        return addToExecuteQueue(request);
    }

    /**
     * @param baseAddress     基地址
     * @param requestParams   参数
     * @param type            返回对象类类型
     * @param requestListener 返回接收器
     * @param <T>             返回对象类类型
     * @deprecated 请求网络接口数据
     */
    public static synchronized <T> Request postData(String baseAddress, final RequestParams requestParams, final Type type, final HttpJsonRequest<T> requestListener) {


        Request request = new NetRequest<T>(Request.Method.POST, baseAddress, requestParams, type, requestListener);

        return addToExecuteQueue(request);
    }

    /**
     * Cancels all requests in this queue with the given tag. Tag must be non-null and equality is by identity.
     */
    public static void cancelAll(Object tag) {

        if (mRequestQueue != null) {

            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * 网络请求回调接收器
     *
     * @param <T> 对象类型
     * @deprecated
     */
    public interface HttpJsonRequest<T> {

        void onSuccess(T t);

        void onFailed(Throwable throwable);

        void onFinished();

    }

    /**
     * 打印网络请求链接地址
     *
     * @param baseAddress 头部基本地址
     * @param params      参数
     */
    public static void printCompleteUrl(String baseAddress, RequestParams params) {

        baseAddress += baseAddress.contains("?") ? "&" : "?";

        String url = baseAddress + (params.isRestStyle() ? "" : params.toString());

        String headers = params.getHeadersString();

        String printJsonBody = params.isRestStyle() ? " \n" + DLog.makeSubTitle("RequestJsonParamData") + prettyFormatGson.toJson(params.getParamsObj()) : "";

        final String printHeadersString = headers.isEmpty() ? "" : "\n" + DLog.makeSubTitle("RequestHeaders") + "\n" + headers;

        DLog.i(LOG_TAG, DLog.makeTitle("URLRequest")  + DLog.makeSubTitle("url") + url + printHeadersString + printJsonBody);
    }


    /**
     * @param <T> 对象类型
     * @deprecated
     */
    public static class SimpleHttpJsonRequest<T> implements HttpJsonRequest<T> {

        @Override
        public void onSuccess(T t) {

        }

        @Override
        public void onFailed(Throwable throwable) {

        }

        @Override
        public void onFinished() {

        }
    }


    /**
     * 网络请求回调监听器
     *
     * @param <T> 返回对象类型
     */
    public static abstract class RequestListener<T> extends TypeToken<T> implements HttpJsonRequest<T> {

    }

    public static class SimpleRequestListener<T> extends RequestListener<T> {
        @Override
        public void onSuccess(T t) {

        }

        @Override
        public void onFailed(Throwable throwable) {

        }

        @Override
        public void onFinished() {

        }
    }

    /**
     * 添加请求任务到执行队列中
     *
     * @param request 请求任务
     */
    public static Request<?> addToExecuteQueue(Request<?> request) {

        return mRequestQueue.add(request);
    }

    /**
     * 构建一个GET方式请求任务
     *
     * @param baseAddress     基地址
     * @param requestParams   参数
     * @param requestListener 返回接收器
     * @param <T>             返回对象类类型
     * @return 当前的请求对象
     */
    public static synchronized <T> Request buildGetRequestTask(String baseAddress, final RequestParams requestParams, final RequestListener<T> requestListener) {

        baseAddress += baseAddress.contains("?") ? "&" : "?";

        final String url = baseAddress + (requestParams == null ? "" : requestParams.toString());

        return new NetRequest<>(Request.Method.GET, url, requestParams, requestListener.getType(), requestListener);
    }

    /**
     * 构建一个POST方式请求任务
     *
     * @param baseAddress     基地址
     * @param requestParams   参数
     * @param requestListener 返回接收器
     * @param <T>             返回对象类类型
     * @return 当前的请求对象
     */
    public static synchronized <T> Request buildPostRequestTask(final String baseAddress, final RequestParams requestParams, final RequestListener<T> requestListener) {

        if (mRequestQueue == null) {

            mRequestQueue = Volley.newRequestQueue(FoundationManager.getContext());
        }

        return new NetRequest<>(Request.Method.POST, baseAddress, requestParams, requestListener.getType(), requestListener);

    }


    /**
     * 停止HttpNetUtils工具，关闭整个后台网络请求相关线程
     */
    public static synchronized void stop() {

        if (mRequestQueue != null) {

            mRequestQueue.stop();

            mRequestQueue = null;

        }
    }

}
