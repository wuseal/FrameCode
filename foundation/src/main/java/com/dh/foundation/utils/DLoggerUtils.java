package com.dh.foundation.utils;

import java.util.logging.Level;

/**
 * 日志输出工具类
 * Created By: Seal.Wu
 * Date: 2015/4/7
 * Time: 10:38
 */
public class DLoggerUtils {

    private static DhLogger dhLogger = DhLogger.getInstance();

    public static void i(String info) {
        dhLogger.logInfo(info);
    }

    public static void e(Throwable throwable) {

        dhLogger.logError(throwable.getMessage(), throwable);
    }

    public static void setLevel(Level level) {
        dhLogger.setLogLevel(level);
    }

    public static void i(String tag, String info) {
        dhLogger.logInfo(tag, info);
    }


    public static void w(String warnInfo) {
        dhLogger.logWarn(warnInfo);
    }


    public static void w(String tag, String warnInfo) {
        dhLogger.logWarn(tag, warnInfo);
    }


    public static void offLog() {
        dhLogger.offLog();
    }
}
