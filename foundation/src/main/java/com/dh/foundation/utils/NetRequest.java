package com.dh.foundation.utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dh.foundation.exception.DataFormatError;
import com.dh.foundation.exception.NetRequestError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 网络请求调用类
 * Created By: Seal.Wu
 * Date: 2015/10/20
 * Time: 16:11
 */

public class NetRequest<ReturnObj> extends StringRequest {

    private static final Handler handler = new Handler(Looper.getMainLooper());

    private final static Gson prettyFormatGson = new GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting().create();

    private final static Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * 网络请求参数
     */
    private final RequestParams requestParams;


    public NetRequest(int method, String url, RequestParams requestParams, Type returnType, HttpNetUtils.HttpJsonRequest<ReturnObj> requestListener) {

        super(method, url, new Listener<ReturnObj>(url, returnType, requestListener), new ErrorListener(url, requestListener));

        this.requestParams = requestParams;

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(30 * 1000, 0, 1);

        setRetryPolicy(retryPolicy);

        setShouldCache(false);

        Object tag = getTag(method, url, requestParams);

        setTag(tag);
    }

    private static String getTag(int method, String url, RequestParams requestParams) {

        return method == Method.GET ? url : url + (requestParams.isRestStyle() ? requestParams.getParamObjJsonData() : requestParams.getParams());
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        if (requestParams.getHeaders() != null) {

            return requestParams.getHeaders();
        }
        return super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {

        return requestParams.getParams();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {

        if (requestParams.isRestStyle()) {
            try {
                final String json = gson.toJson(requestParams.getParamsObj());
                return json.getBytes(getParamsEncoding());
            } catch (UnsupportedEncodingException e) {
                DLoggerUtils.e(e);
                return null;
            }
        }

        return super.getBody();
    }

    @Override
    public String getBodyContentType() {

        if (requestParams.isRestStyle()) {

            return "application/json;charset=" + getParamsEncoding();
        } else {
            return super.getBodyContentType();
        }
    }

    @Override
    protected String getParamsEncoding() {
        return requestParams.getParamsEncoding();
    }

    private static class Listener<ReturnObj> implements Response.Listener<String> {

        private String url;

        /**
         * 返回对象数据类型
         */
        private final Type returnType;

        /**
         * 返回数据监听接口
         */
        private final HttpNetUtils.HttpJsonRequest requestListener;

        public Listener(String url, Type returnType, HttpNetUtils.HttpJsonRequest<ReturnObj> requestListener) {
            this.url = url;

            this.returnType = returnType;

            this.requestListener = requestListener;
        }

        @Override
        public void onResponse(final String response) {
//            String a = "{\"name\": \"seal\",\"age\": null,\"height\": 175}";
//            NetRequest.printResponse("http://baidu.com", formatJsonString(a));

            AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {
                    ReturnObj o = null;

                    try {
                        String newResponse = gson.toJson(new JsonParser().parse(response));

                        o = NetRequest.gson.fromJson(newResponse, returnType);

                    } catch (final JsonSyntaxException e) {

                        DLoggerUtils.e(e);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                requestListener.onFailed(new DataFormatError(e));

                                requestListener.onFinished();

                            }
                        });
                    }

                    final ReturnObj finalO = o;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            requestListener.onSuccess(finalO);

                            requestListener.onFinished();
                        }


                    });

                    printResponse(url, formatJsonString(response));
                }
            });


        }

    }

    private static String formatJsonString(String response) {
        return prettyFormatGson.toJson(new JsonParser().parse(response));
    }

    private static void printResponse(String url, String response) {

        DLog.i(HttpNetUtils.LOG_TAG, DLog.makeTitle("RequestResponse") + DLog.makeSubTitle("URL") + url + "\n"
                + DLog.makeSubTitle("return data") + response + DLog.END_LINE);
    }

    static class ErrorListener implements Response.ErrorListener {
        String url;

        /**
         * 返回数据监听接口
         */
        private final HttpNetUtils.HttpJsonRequest requestListener;

        public ErrorListener(String url, HttpNetUtils.HttpJsonRequest requestListener) {

            this.url = url;
            this.requestListener = requestListener;
        }

        @Override
        public void onErrorResponse(final VolleyError error) {

            new Runnable() {
                @Override
                public void run() {

                    printErrorInfo();

                    requestListener.onFailed(new NetRequestError(error));

                    requestListener.onFinished();
                }

                private void printErrorInfo() {
                    String errorResponse = new String(error.networkResponse != null ? error.networkResponse.data : (error.getMessage() == null ? error.toString().getBytes() : error.getMessage().getBytes()));
                    DLog.e(HttpNetUtils.LOG_TAG, DLog.makeTitle("RequestError") + DLog.makeSubTitle("URL") + url
                            + "\n" + DLog.makeSubTitle("Error") + errorResponse, error);
                }
            }.run();
        }
    }
}
