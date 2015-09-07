package com.dh.foundation.utils;

/**
 * Created By: Seal.Wu
 * Date: 2015/4/7
 * Time: 10:35
 */
public interface ILogger{
    public void logInfo(String info);

    public void logError(String info, Throwable throwable);
}
