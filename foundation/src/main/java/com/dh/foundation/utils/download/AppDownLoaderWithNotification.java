package com.dh.foundation.utils.download;

import android.app.DownloadManager;

import com.dh.foundation.app.ApplicationUtil;
import com.dh.foundation.manager.FoundationManager;
import com.dh.foundation.utils.InvokeIntentUtils;

/**
 * 应用升级下载通知类
 * Created By: Seal.Wu
 * Date: 2015/8/27
 * Time: 16:44
 */
public class AppDownLoaderWithNotification {
    private DownloadManager.Request req;

    private String url;

    public AppDownLoaderWithNotification(String url) {
        this.url=url;

//        Uri uri = Uri.parse(url);
//        // uri 是你的下载地址，可以使用Uri.parse("http://")包装成Uri对象
//        req = new DownloadManager.Request(uri);
//
//        // 通过setAllowedNetworkTypes方法可以设置允许在何种网络下下载，
//        // 也可以使用setAllowedOverRoaming方法，它更加灵活
//        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
//
//        // 此方法表示在下载过程中通知栏会一直显示该下载，在下载完成后仍然会显示，
//        // 直到用户点击该通知或者消除该通知。还有其他参数可供选择
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
//            req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//
//        // 设置下载文件存放的路径，同样你可以选择以下方法存放在你想要的位置。
//        // setDestinationUri
//        // setDestinationInExternalPublicDir
//        req.setDestinationInExternalFilesDir(FoundationManager.getContext(), Environment.DIRECTORY_DOWNLOADS, ApplicationUtil.getAppName());
//
//        // 设置一些基本显示信息
//        req.setContent(ApplicationUtil.getAppName());
//        req.setDescription("下载完后请点击打开");
//        req.setMimeType("application/vnd.android.package-archive");

    }


    public void start() {
//        // Ok go!
//        DownloadManager dm = FoundationManager.getDownloadManager();
//        long downloadId = dm.enqueue(req);

        DownLoadUtil.getInstance().startADownloadTask(url, ApplicationUtil.getAppName(), "下载完后请点击打开"
                , "application/vnd.android.package-archive", true, new DownloadListener() {
            @Override
            public void onLoadChange(int total, int currentSize, int state) {
            }

            @Override
            public void onComplete(long downloadId, String filePath) {

                InvokeIntentUtils.setupAPK(FoundationManager.getContext(), filePath);
            }
        });
    }
}
