package com.dh.foundation.utils.download;

import com.dh.foundation.app.ApplicationUtil;
import com.dh.foundation.manager.FoundationManager;
import com.dh.foundation.utils.IntentInvokeUtils;

/**
 * 应用升级下载通知类
 * Created By: Seal.Wu
 * Date: 2015/8/27
 * Time: 16:44
 */
public class AppDownLoaderWithNotification {

    /**
     * 待下载的apk下载地址
     */
    private String url;

    /**
     * @param url 待下载的apk下载地址
     */
    public AppDownLoaderWithNotification(String url) {

        this.url = url;

    }


    public void start() {

        DownLoadUtil.getInstance().startADownloadTask(url, ApplicationUtil.getAppName(), "下载完后请点击打开"
                , "application/vnd.android.package-archive", true, new DownloadListener() {
            @Override
            public void onLoadChange(int total, int currentSize, int state) {
            }

            @Override
            public void onComplete(long downloadId, String filePath) {

                IntentInvokeUtils.setupAPK(FoundationManager.getContext(), filePath);
            }
        });
    }
}
