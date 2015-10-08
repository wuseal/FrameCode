package com.dh.foundation.utils;

import android.util.Log;

import java.util.logging.Level;

/**
 * Created By: Seal.Wu
 * Date: 2015/4/7
 * Time: 10:15
 */
class DhLogger implements ILogger {

    private Level level = Level.ALL;

    private final static String LOG_TAG = "DaHan_LOG";

    private static DhLogger dhLogger = new DhLogger();

    private DhLogger() {
    }

    @Override
    public void logInfo(String info) {

        if (level.intValue() <= Level.INFO.intValue()) {

            Log.i(LOG_TAG, info);
        }
    }

    @Override
    public void logError(String info, Throwable throwable) {

        if (level.intValue() <= Level.WARNING.intValue()) {

            Log.e(LOG_TAG, info, throwable);
        }
    }

    public static final DhLogger getInstance() {
        return dhLogger;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
