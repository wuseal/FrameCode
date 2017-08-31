package com.dh.foundation.app;

import android.app.Application;

/**
 * 基础框架包应用类
 * Created By: Seal.Wu
 * Date: 2015/8/27
 * Time: 17:35
 */
public class FoundationApplication extends Application {

    private final IFoundationApplicationDelegate applicationDelegate = new FoundationApplicationDelegate(this);

    @Override
    public void onCreate() {
        super.onCreate();
        applicationDelegate.onCreate();
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        applicationDelegate.onTerminate();
    }

}
