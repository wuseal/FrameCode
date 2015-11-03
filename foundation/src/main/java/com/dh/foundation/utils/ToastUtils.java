package com.dh.foundation.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast显示工具类
 * Created By: Seal.Wu
 * Date: 2014/12/26
 * Time: 17:22
 */
public class ToastUtils {

    private static Toast instance;//Toast实例

    /**
     * 常规提示
     * @param context
     * 上下文
     * @param message
     * 消息内容
     */
    public static synchronized void toast(Context context, String message) {
        if (message == null) {
            message = "";
        }
        if (instance == null) {
            instance = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            instance.cancel();
            instance = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }
        instance.show();
    }
    /**
     * 常规提示
     * @param context
     * 上下文
     * @param messageId
     * 消息内容String id
     */
    public static synchronized void toast(Context context, int messageId) {
        if (instance == null) {
            instance = Toast.makeText(context, messageId, Toast.LENGTH_SHORT);
        } else {
            instance.cancel();
            instance = Toast.makeText(context, messageId, Toast.LENGTH_SHORT);
        }
        instance.show();
    }

    /**
     * 长时间提示
     * @param context
     * 上下文
     * @param message
     * 消息内容
     */
    public static synchronized void toastLongTime(Context context, String message) {
        if (message == null) {
            message = "";
        }
        if (instance == null) {
            instance = Toast.makeText(context, message, Toast.LENGTH_LONG);
        } else {
            instance.cancel();
            instance = Toast.makeText(context, message, Toast.LENGTH_LONG);
        }
        instance.show();
    }

    /**
     * 长时间提示
     * @param context
     * 上下文
     * @param messageId
     * 消息内容String id
     */
    public static synchronized void toastLongTime(Context context, int messageId) {
        if (instance == null) {
            instance = Toast.makeText(context, messageId, Toast.LENGTH_LONG);
        } else {
            instance.cancel();
            instance = Toast.makeText(context, messageId, Toast.LENGTH_LONG);
        }
        instance.show();
    }


    /**
     * 关闭提示
     */
    public static synchronized void dismissToast() {

        if (instance != null) {

            instance.cancel();
        }
    }
}
