package com.dh.foundation.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Android常用Intent使用代码汇总：
 * Created By: Seal.Wu
 * Date: 2015/6/3
 * Time: 14:37
 */
public class IntentInvokeUtils {


    public static void invokeActivity(Context context,Class<? extends Activity> activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    /**
     * 显示网页:
     */
    public static void showWeb(Context context, String webUrl) {
        if (StringUtils.isNotEmpty(webUrl)) {
            Uri uri = Uri.parse(webUrl);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        }
    }

    /**
     * 显示网页:
     *
     * @param longitude 经度
     * @param latitude  纬度
     */
    public static void showMapPoint(Context context, String longitude, String latitude) {
        if (StringUtils.isNotEmpty(longitude) && StringUtils.isNotEmpty(latitude)) {
            Uri uri = Uri.parse("geo:" + longitude + "," + latitude);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        }
    }

    /**
     * 调用打电话程序，不进行拨号
     *
     * @param telephone 电话号码
     */
    public static void invokeDial(Context context,String telephone) {
        if (StringUtils.isNotEmpty(telephone)) {
            Uri uri = Uri.parse("tel:" + telephone);
            Intent it = new Intent(Intent.ACTION_DIAL, uri);
            context.startActivity(it);
        }
    }

    /**
     * 直接进行拨号
     *
     * @param telephone 电话号码
     */
    public static void callTelephone(Context context, String telephone) {
        if (StringUtils.isNotEmpty(telephone)) {
            Uri uri = Uri.parse("tel:" + telephone);
            Intent it = new Intent(Intent.ACTION_CALL, uri);
            context.startActivity(it);
        }
    }

    /**
     * 安装指定apk
     *
     * @param apkPath apk绝对路径
     */
    public static void setupAPK(Context context, String apkPath) {
        if (StringUtils.isNotEmpty(apkPath)) {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");

            context.startActivity(intent);
        }

    }
}
