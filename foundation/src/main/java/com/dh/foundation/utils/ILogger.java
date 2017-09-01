package com.dh.foundation.utils;

import java.util.logging.Level;

/**
 * Created By: Seal.Wu
 * Date: 2015/4/7
 * Time: 10:35
 */
public interface ILogger {
    public void logInfo(String info);

    void logInfo(String tag, String info);

    void logWarn(String tag, String warn);

    void logWarn(String warn);

    public void logError(String info, Throwable throwable);

    public void logError(String tag, String info, Throwable throwable);


    void setLogLevel(Level logLevel);

    /**
     * 关闭日志打印
     */
    void offLog();
}
