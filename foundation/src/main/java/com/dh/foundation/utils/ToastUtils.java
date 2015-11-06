package com.dh.foundation.utils;

import android.content.Context;
import android.widget.Toast;

import com.dh.foundation.manager.FoundationManager;

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
     * @param message
     * 消息内容
     */
    public static synchronized void toast(String message) {

        if (message == null) {
            message = "";
        }
        if (instance == null) {
            instance = Toast.makeText(FoundationManager.getContext(), message, Toast.LENGTH_SHORT);
        } else {
            instance.cancel();
            instance = Toast.makeText(FoundationManager.getContext(), message, Toast.LENGTH_SHORT);
        }
        instance.show();
    }
    /**
     * 常规提示
     * @param messageId
     * 消息内容String id
     */
    public static synchronized void toast(int messageId) {

        if (instance == null) {

            instance = Toast.makeText(FoundationManager.getContext(), messageId, Toast.LENGTH_SHORT);
        } else {

            instance.cancel();

            instance = Toast.makeText(FoundationManager.getContext(), messageId, Toast.LENGTH_SHORT);
        }
        instance.show();
    }

    /**
     * 长时间提示
     * @param message
     * 消息内容
     */
    public static synchronized void toastLongTime(String message) {

        if (message == null) {
            message = "";
        }
        if (instance == null) {
            instance = Toast.makeText(FoundationManager.getContext(), message, Toast.LENGTH_LONG);
        } else {
            instance.cancel();
            instance = Toast.makeText(FoundationManager.getContext(), message, Toast.LENGTH_LONG);
        }
        instance.show();
    }

    /**
     * 长时间提示
     * @param messageId
     * 消息内容String id
     */
    public static synchronized void toastLongTime(int messageId) {

        if (instance == null) {
            instance = Toast.makeText(FoundationManager.getContext(), messageId, Toast.LENGTH_LONG);
        } else {
            instance.cancel();
            instance = Toast.makeText(FoundationManager.getContext(), messageId, Toast.LENGTH_LONG);
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
