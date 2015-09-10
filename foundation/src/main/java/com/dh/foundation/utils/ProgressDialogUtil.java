package com.dh.foundation.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.ContextThemeWrapper;

import java.lang.reflect.Field;

/**
 * 等待进度条展示工具类
 * Created By: Seal.Wu
 * Date: 2014/11/18
 * Time: 22:17
 */
public class ProgressDialogUtil {

private static Dialog progressDialog;

private static int level;//等待框展示层级，只有level=0的时候才能关闭progressDialog

private static DefaultProgressDialogMaker defaultProgressDialogMaker = new DefaultProgressDialogMaker();//对话框制造者

private static ProgressDialogMaker dialogMaker;


    public static ProgressDialogMaker getDialogMaker() {
        return dialogMaker;
    }

    /**
     * 设置对话框构建类
     *
     * @param dialogMaker 对话框构建者
     */
    public static void setDialogMaker(ProgressDialogMaker dialogMaker) {

        ProgressDialogUtil.dialogMaker = dialogMaker;
    }

    /**
     * 设置等待对话框提醒内容
     */
    public static void setTipContent(String content) {

        defaultProgressDialogMaker.setContent(content);
    }

    /**
     * 设置自定义的对话框视图
     */
    public static void setCustomView(int customViewLayout) {

        defaultProgressDialogMaker.setCustomViewLayout(customViewLayout);
    }

    /**
     * 设置对话框样式
     */
    public static void setDialogStyle(int dialogStyle) {

        defaultProgressDialogMaker.setStyle(dialogStyle);
    }

    public static synchronized void showProgressDialog(Context context) {

        showProgressDialog(context, null);
    }


    public static synchronized void showProgressDialog(Context context, String content) {

        if (context instanceof Activity) {


            if (progressDialog == null || !context.equals(getDialogBaseContext(progressDialog)) || !progressDialog.isShowing()) {

                dismissProgressDialog();

                if (StringUtils.isEmpty(content)) {

                    content = "请稍候...";
                }
                if (dialogMaker != null) {

                    progressDialog = dialogMaker.makeDialog(context);

                } else {

                    defaultProgressDialogMaker.setContent(content);

                    progressDialog = defaultProgressDialogMaker.makeDialog(context);
                }


                progressDialog.show();
            }

            level++;

        }
    }

    /**
     * 消除对话框，不用担心多个对话框同时出现，只有当showProgressDialog的次数和dismissProgressDialog调用次数相同时，ProgressDialog才会消除
     */
    public static synchronized void dismissProgressDialog() {

        level--;

        if (level < 0) {

            level = 0;
        }
        if (progressDialog != null && level == 0 && progressDialog.isShowing()) {

            progressDialog.dismiss();
        }
    }

    /**
     * 一般情况下不要调用此方法，此方法为一次性全部消除所有对话框层级．推荐调用dismissProgressDialog按层级进行消除等待框
     */
    public static void dismissAll() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();

            /**
             * 复位
             */
            level = 0;
        }
    }

    private static Context getDialogBaseContext(Dialog dialog) {
        if (dialog.getContext() instanceof ContextThemeWrapper) {
            try {
                Field mBase = null;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    mBase = dialog.getContext().getClass().getDeclaredField("mBase");
                } else {
                    mBase = dialog.getContext().getClass().getSuperclass().getDeclaredField("mBase");
                }
                mBase.setAccessible(true);
                return (Context) mBase.get(dialog.getContext());
            } catch (NoSuchFieldException e) {
                DLoggerUtils.e(e);
            } catch (IllegalAccessException e) {
                DLoggerUtils.e(e);
            }
        }
        return dialog.getContext();
    }
}
