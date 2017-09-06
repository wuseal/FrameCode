package com.dh.foundation.utils;

import android.support.annotation.NonNull;

import java.util.logging.Level;

/**
 * 日志输出工具类
 * Created By: Seal.Wu
 * Date: 2015/4/7
 * Time: 10:38
 */
public class DLoggerUtils {

    private static final String TITLE_INDICATOR = "=====================================================";

    public static final String END_LINE = "\n" + TITLE_INDICATOR + TITLE_INDICATOR;

    private static final String SUBTITLE_LEFT_INDICATOR = "<<<<<<<";
    private static final String SUBTITLE_RIGHT_INDICATOR = ">>>>>>>";

    private static DhLogger dhLogger = DhLogger.getInstance();

    public static void i(String info) {
        for (String s : info.split("\n")) {
            s = appendSplit(s);
            dhLogger.logInfo(s);
        }
    }

    @NonNull
    private static String appendSplit(String s) {
        s = "| " + s;
        return s;
    }

    public static void e(Throwable throwable) {

        dhLogger.logError(throwable.getMessage(), throwable);
    }

    public static void e(String tag, Throwable throwable) {
        dhLogger.logError(tag, throwable.getMessage(), throwable);
    }

    public static void e(String tag, String info, Throwable throwable) {
        for (String s : info.split("\n")) {
            s = appendSplit(s);
            dhLogger.logError(s, null);
        }
    }

    public static void setLevel(Level level) {
        dhLogger.setLogLevel(level);
    }

    public static void i(String tag, String info) {
        for (String s : info.split("\n")) {
            s = appendSplit(s);
            dhLogger.logInfo(tag, s);
        }
    }


    public static void w(String warnInfo) {
        for (String s : warnInfo.split("\n")) {
            s = appendSplit(s);
            dhLogger.logWarn(s);
        }
    }


    public static void w(String tag, String warnInfo) {
        for (String s : warnInfo.split("\n")) {
            s = appendSplit(s);
            dhLogger.logWarn(tag, s);
        }
    }


    public static void offLog() {
        dhLogger.offLog();
    }


    public static String makeTitle(String title) {
        return TITLE_INDICATOR + title + TITLE_INDICATOR + "\n";
    }

    public static String makeSubTitle(String subTitle) {
        final int length = subTitle.length();
        if (length < 10) {
            int spaceLength = (10 - length) / 2;
            for (int i = 0; i < spaceLength; i++) {
                subTitle += " ";
                subTitle = " " + subTitle;
            }
        }
        return SUBTITLE_LEFT_INDICATOR + subTitle + SUBTITLE_RIGHT_INDICATOR + "\n";
    }
}
