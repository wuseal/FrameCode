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

    public static synchronized void toast(Context context, int messageId) {
        if (instance == null) {
            instance = Toast.makeText(context, messageId, Toast.LENGTH_SHORT);
        } else {
            instance.cancel();
            instance = Toast.makeText(context, messageId, Toast.LENGTH_SHORT);
        }
        instance.show();
    }
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

    public static synchronized void toastLongTime(Context context, int messageId) {
        if (instance == null) {
            instance = Toast.makeText(context, messageId, Toast.LENGTH_LONG);
        } else {
            instance.cancel();
            instance = Toast.makeText(context, messageId, Toast.LENGTH_LONG);
        }
        instance.show();
    }
}
