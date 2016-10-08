package com.dh.foundation.manager;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;

import com.dh.foundation.manager.managerinterface.IFoundationCacheManager;
import com.dh.foundation.manager.managerinterface.IHandlerManager;

/**
 * 框架基础包管理者
 * Created By: Seal.Wu
 * Date: 2015/4/14
 * Time: 14:13
 */
public class FoundationManager {
    private final static String sharedPreferencesName = "shared_preferences";
    private static SingletonInjectFactory singletonInjectFactory = SingletonInjectFactory.getInstance();

    public static void init(Context context) {
        singletonInjectFactory.registerDependencySingletonObject(Context.class, context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
        singletonInjectFactory.registerDependencySingletonObject(SharedPreferences.class, sharedPreferences);
        singletonInjectFactory.registerDependencySingletonClass(SharedPreferenceManager.class);
        singletonInjectFactory.registerDependencySingletonImplementObject(IHandlerManager.class, HandlerManager.getInstance());
        singletonInjectFactory.registerDependencySingletonImplementObject(ActivityStackManager.class, new ActivityStackManager());
        singletonInjectFactory.registerDependencySingletonObject(DownloadManager.class, (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE));
        singletonInjectFactory.registerDependencySingletonImplementObject(IFoundationCacheManager.class, new FoundationCacheManager());
    }


    public static Context getContext() {
        return singletonInjectFactory.getObjectByClass(Context.class);
    }

    public static SharedPreferences getSharedPreferences() {
        return singletonInjectFactory.getObjectByClass(SharedPreferences.class);
    }

    public static SharedPreferenceManager getSharedPreferenceManager() {
        return singletonInjectFactory.getObjectByClass(SharedPreferenceManager.class);
    }

    public static ActivityStackManager getActivityManager() {
        return singletonInjectFactory.getObjectByClass(ActivityStackManager.class);
    }

    public static IHandlerManager getHandleManager() {
        return singletonInjectFactory.getObjectByClass(IHandlerManager.class);
    }

    public static DownloadManager getDownloadManager() {
        return singletonInjectFactory.getObjectByClass(DownloadManager.class);
    }

    /**
     * 获取缓存管理者
     */
    public static IFoundationCacheManager getFoundationCacheManager() {
        return singletonInjectFactory.getObjectByClass(IFoundationCacheManager.class);
    }
}
