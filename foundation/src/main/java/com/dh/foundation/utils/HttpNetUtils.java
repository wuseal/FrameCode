package com.dh.foundation.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dh.foundation.exception.DataFormatError;
import com.dh.foundation.exception.NetRequestError;
import com.dh.foundation.manager.FoundationManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 网络请求工具类
 * Created By: Seal.Wu
 * Date: 2014/12/21
 * Time: 23:26
 */
public class HttpNetUtils {

    private static RequestQueue mRequestQueue;

    private static Handler handler = new Handler(Looper.getMainLooper());

    /**
     * GET请求网络接口数据
     *
     * @param context         上下文
     * @param baseAddress     基地址
     * @param requestParams   参数
     * @param type            返回对象类类型
     * @param requestListener 返回接收器
     * @param <T>             返回对象类类型
     * @deprecated
     */
    public static synchronized <T> void getData(Context context, String baseAddress, RequestParams requestParams, final Type type, final HttpJsonRequest<T> requestListener) {

        Request request = getData(baseAddress, requestParams, type, requestListener);

        request.setTag(context);
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
    public static synchronized <T> Request getData(String baseAddress, RequestParams requestParams, final Type type, final HttpJsonRequest<T> requestListener) {

        baseAddress += baseAddress.contains("?") ? "&" : "?";

        final String url = baseAddress + (requestParams == null ? "" : requestParams.toString());

        if (mRequestQueue == null) {

            mRequestQueue = Volley.newRequestQueue(FoundationManager.getContext());
        }
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String s) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Gson gson = new Gson();

                        T o = null;

                        try {

                            o = gson.fromJson(s, type);

                        } catch (JsonSyntaxException e) {

                            DLoggerUtils.e(e);

                            requestListener.onFailed(new DataFormatError(e));

                            requestListener.onFinished();

                            return;
                        }

                        requestListener.onSuccess(o);

                        requestListener.onFinished();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        DLoggerUtils.e(volleyError);

                        requestListener.onFailed(new NetRequestError(volleyError));

                        requestListener.onFinished();
                    }
                });
            }
        });

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(30 * 1000, 0, 1);

        stringRequest.setRetryPolicy(retryPolicy);

        stringRequest.setShouldCache(false);

        stringRequest.setTag(baseAddress + requestParams);

        return addToExecuteQueue(stringRequest);
    }

    /**
     * @param baseAddress     基地址
     * @param requestParams   参数
     * @param type            返回对象类类型
     * @param requestListener 返回接收器
     * @param <T>             返回对象类类型
     * @deprecated 请求网络接口数据
     */
    public static synchronized <T> void postData(Context context, String baseAddress, final RequestParams requestParams, final Type type, final HttpJsonRequest<T> requestListener) {

        Request request = postData(baseAddress, requestParams, type, requestListener);

        request.setTag(context);

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
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(FoundationManager.getContext());
        }
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, baseAddress, new Response.Listener<String>() {
            @Override
            public void onResponse(final String s) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Gson gson = new Gson();

                        T o = null;

                        try {

                            o = gson.fromJson(s, type);

                        } catch (JsonSyntaxException e) {

                            DLoggerUtils.e(e);

                            requestListener.onFailed(new DataFormatError(e));

                            requestListener.onFinished();

                            return;
                        }

                        requestListener.onSuccess(o);

                        requestListener.onFinished();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        DLoggerUtils.e(volleyError);

                        requestListener.onFailed(new NetRequestError(volleyError));

                        requestListener.onFinished();
                    }
                });
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return requestParams.getParams();
            }

            @Override
            protected String getParamsEncoding() {
                return requestParams.getParamsEncoding();
            }
        };
        stringRequest.setTag(baseAddress + requestParams);

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(30 * 1000, 0, 1);

        stringRequest.setRetryPolicy(retryPolicy);

        stringRequest.setShouldCache(false);

        return addToExecuteQueue(stringRequest);
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

        public void onSuccess(T t);

        public void onFailed(Throwable throwable);

        public void onFinished();

    }

    /**
     * 打印网络请求链接地址
     *
     * @param baseAddress 头部基本地址
     * @param params      参数
     */
    public static void printCompleteUrl(String baseAddress, RequestParams params) {

        baseAddress += baseAddress.contains("?") ? "&" : "?";

        String url = baseAddress + params.toString();

        DLoggerUtils.i("HttpNetUtils=======>url= " + url);
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
     * GET请求网络接口数据
     *
     * @param context         上下文
     * @param baseAddress     基地址
     * @param requestParams   参数
     * @param requestListener 返回接收器
     * @param <T>             返回对象类类型
     */
    public static synchronized <T> void getData(final Context context, String baseAddress, RequestParams requestParams, final RequestListener<T> requestListener) {

        final Request stringRequest = buildGetRequestTask(baseAddress, requestParams, requestListener);

        stringRequest.setTag(context);

        addToExecuteQueue(stringRequest);
    }

    /**
     * 请求网络接口数据
     *
     * @param context         上下文
     * @param baseAddress     基地址
     * @param requestParams   参数
     * @param requestListener 返回接收器
     * @param <T>             返回对象类类型
     */
    public static synchronized <T> void postData(final Context context, String baseAddress, final RequestParams requestParams, final RequestListener<T> requestListener) {

        final Request stringRequest = buildPostRequestTask(baseAddress, requestParams, requestListener);

        stringRequest.setTag(context);

        addToExecuteQueue(stringRequest);
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
    public static synchronized <T> Request buildGetRequestTask(String baseAddress, RequestParams requestParams, final RequestListener<T> requestListener) {

        baseAddress += baseAddress.contains("?") ? "&" : "?";

        final String url = baseAddress + (requestParams == null ? "" : requestParams.toString());

        if (mRequestQueue == null) {

            mRequestQueue = Volley.newRequestQueue(FoundationManager.getContext());
        }
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String s) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Gson gson = new Gson();

                        T o = null;

                        try {

                            o = gson.fromJson(s, requestListener.getType());

                        } catch (JsonSyntaxException e) {

                            DLoggerUtils.e(e);

                            requestListener.onFailed(new DataFormatError(e));

                            requestListener.onFinished();

                            return;
                        }

                        requestListener.onSuccess(o);

                        requestListener.onFinished();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        DLoggerUtils.e(volleyError);

                        requestListener.onFailed(new NetRequestError(volleyError));

                        requestListener.onFinished();
                    }
                });
            }
        });

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(30 * 1000, 0, 1);

        stringRequest.setRetryPolicy(retryPolicy);

        stringRequest.setShouldCache(false);

        stringRequest.setTag(baseAddress + requestParams);

        return stringRequest;
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
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, baseAddress, new Response.Listener<String>() {

            @Override
            public void onResponse(final String s) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Gson gson = new Gson();

                        T o = null;

                        try {

                            o = gson.fromJson(s, requestListener.getType());

                        } catch (JsonSyntaxException e) {

                            DLoggerUtils.e(e);

                            requestListener.onFailed(new DataFormatError(e));

                            requestListener.onFinished();

                            return;
                        }

                        requestListener.onSuccess(o);

                        requestListener.onFinished();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError volleyError) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        DLoggerUtils.e(volleyError);

                        requestListener.onFailed(new NetRequestError(volleyError));

                        requestListener.onFinished();
                    }
                });
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return requestParams.getParams();
            }

            @Override
            protected String getParamsEncoding() {
                return requestParams.getParamsEncoding();
            }
        };

        stringRequest.setTag(baseAddress + requestParams);

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(30 * 1000, 0, 1);

        stringRequest.setRetryPolicy(retryPolicy);

        stringRequest.setShouldCache(false);

        return stringRequest;
    }

}
