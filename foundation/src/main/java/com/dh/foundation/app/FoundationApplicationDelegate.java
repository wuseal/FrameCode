package com.dh.foundation.app;

import android.app.Application;
import android.app.DownloadManager;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;

import com.dh.foundation.receiver.DownloadCompleteReceiver;
import com.dh.foundation.manager.FoundationManager;
import com.dh.foundation.observer.DownloadChangeObserver;
import com.dh.foundation.utils.download.DownLoadUtil;

public class FoundationApplicationDelegate implements IFoundationApplicationDelegate {

    private final Application application;

    private ContentObserver dataChangeObserver;

    private DownloadCompleteReceiver downloadCompleteReceiver;


    public FoundationApplicationDelegate(Application application) {

        this.application = application;
    }

    @Override
    public void onCreate() {

        FoundationManager.init(application);

        Thread.setDefaultUncaughtExceptionHandler(new UnCatchExceptionHandler(application));

        DownLoadUtil.getInstance();

        dataChangeObserver = new DownloadChangeObserver(FoundationManager.getHandleManager()
                .getHandler(DownLoadUtil.OBSERVE_CHANGE_HANDLER));

        application.getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads/")
                , true, dataChangeObserver);

        downloadCompleteReceiver = new DownloadCompleteReceiver();

        application.registerReceiver(downloadCompleteReceiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onTerminate() {

        application.getContentResolver().unregisterContentObserver(dataChangeObserver);

        application.unregisterReceiver(downloadCompleteReceiver);
    }
}