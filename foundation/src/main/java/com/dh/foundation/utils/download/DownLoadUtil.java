package com.dh.foundation.utils.download;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;

import com.dh.foundation.app.ApplicationUtil;
import com.dh.foundation.manager.FoundationManager;
import com.dh.foundation.observer.DownloadChangeObserver;
import com.dh.foundation.receiver.DownloadCompleteReceiver;
import com.dh.foundation.utils.DLoggerUtils;
import com.dh.foundation.utils.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 下载工具类
 * Created By: Seal.Wu
 * Date: 2015/8/28
 * Time: 18:08
 */
public class DownLoadUtil implements Handler.Callback {

    public final static String OBSERVE_CHANGE_HANDLER = "observeChangeHandler";

    public final static String DOWNLOAD_COMPLETE_HANDLER = "downloadCompleteHandler";

    public final static String DOWNLOAD_DIR;

    private final static DownloadManager downloadManager = FoundationManager.getDownloadManager();

    private Handler observeChangeHandler = new Handler(Looper.getMainLooper(), this);

    private Handler downloadCompleteHandler = new Handler(Looper.getMainLooper(), this);

    private Map<Long, DownloadListener> listenerMap = new HashMap<>();


    static {
        DOWNLOAD_DIR = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                ? Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + ApplicationUtil.getAppName()
                : Environment.getDownloadCacheDirectory().getAbsolutePath() + "/" + ApplicationUtil.getAppName();

    }

    {
        FoundationManager.getHandleManager().registerHandler(OBSERVE_CHANGE_HANDLER, observeChangeHandler);

        FoundationManager.getHandleManager().registerHandler(DOWNLOAD_COMPLETE_HANDLER, downloadCompleteHandler);
    }

    private final static DownLoadUtil instance = new DownLoadUtil();


    private DownLoadUtil() {

    }


    public static DownLoadUtil getInstance() {
        return instance;
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case DownloadCompleteReceiver.DOWNLOAD_COMPLETE:
                long completeDownloadId = (long) msg.obj;
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(completeDownloadId);
                Cursor cursor = downloadManager.query(query);
                cursor.moveToFirst();
                if (StringUtils.isNotEmpty(cursor.getColumnName(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE)))) {
                    try {
                        downloadManager.openDownloadedFile(completeDownloadId);
                    } catch (FileNotFoundException e) {
                        DLoggerUtils.e(e);
                    }
                }
                int fileUriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                String fileUriName = cursor.getString(fileUriIndex);
                DownloadListener completeListener = listenerMap.get(completeDownloadId);
                if (completeListener != null) {
                    completeListener.onComplete(completeDownloadId, getRealFilePath(FoundationManager.getContext(), Uri.parse(fileUriName)));
                    listenerMap.remove(completeDownloadId);
                }
                cursor.close();
                break;
            default:
                long downloadId = DownloadChangeObserver.getDownloadId(msg.what);
                DownloadListener listener = listenerMap.get(downloadId);
                if (listener != null) {
                    listener.onLoadChange(msg.arg2, msg.arg1, (Integer) msg.obj);
                }
                break;
        }
        return true;
    }

    /**
     * 启动一个下载任务
     *
     * @param url              待下载文件的地址
     * @param downloadListener 下载监听器
     * @return 下载任务的标识唯一id
     */

    public long startADownloadTask(String url, DownloadListener downloadListener) {

        return startADownloadTask(url, null, null, null, false, downloadListener);
    }

    /**
     * 启动一个下载任务
     *
     * @param url                    待下载文件的地址
     * @param title                  若显示通知栏，则对应通知栏的标题
     * @param description            若显示通知栏，则对应通知栏的描述
     * @param mimeType               文件打开类型
     * @param notificationVisibility 是否显示在通知栏及其它下载应用中
     * @param downloadListener       下载监听器
     * @return 下载任务的标识唯一id
     */
    public long startADownloadTask(String url, String title, String description, String
            mimeType, boolean notificationVisibility, DownloadListener downloadListener) {
        Uri uri = Uri.parse(url);
        // uri 是你的下载地址，可以使用Uri.parse("http://")包装成Uri对象
        DownloadManager.Request req = new DownloadManager.Request(uri);

        // 通过setAllowedNetworkTypes方法可以设置允许在何种网络下下载，
        // 也可以使用setAllowedOverRoaming方法，它更加灵活
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

        if (notificationVisibility) {
            // 此方法表示在下载过程中通知栏会一直显示该下载，在下载完成后仍然会显示，
            // 直到用户点击该通知或者消除该通知。还有其他参数可供选择
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
                req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        } else {
            req.setVisibleInDownloadsUi(false);

        }

        // 设置下载文件存放的路径，同样你可以选择以下方法存放在你想要的位置。
        // setDestinationUri
        // setDestinationInExternalPublicDir
//        req.setDestinationInExternalFilesDir(FoundationManager.getContext(), Environment.DIRECTORY_DOWNLOADS, ApplicationUtil.getAppName());
        req.setDestinationUri(Uri.fromFile(getFile(url)));

        req.setTitle(title);
        req.setDescription(description);
        req.setMimeType(mimeType);
        // Ok go!
        long downloadId = downloadManager.enqueue(req);

        if (downloadListener != null) {

            DownloadChangeObserver.registerDownloadTask(downloadId);


            listenerMap.put(downloadId, downloadListener);

            DownloadCompleteReceiver.registerDownloadTask(downloadId);

        } else if (StringUtils.isNotEmpty(mimeType)) {
            DownloadCompleteReceiver.registerDownloadTask(downloadId);
        }

        return downloadId;
    }

    /**
     * 离开当前activity的时候记得要调用
     *
     * @param downloadId 下载任务的标识唯一id
     */
    public void leaveActivity(long downloadId) {

        listenerMap.remove(downloadId);
    }


    /**
     * 取消结束当前的下载任务
     *
     * @param downloadId 下载任务的标识唯一id
     */
    public void cancelTask(long downloadId) {

        downloadManager.remove(downloadId);
    }


    /**
     * 重新尝试再次开启任务
     *
     * @param downloadId 下载任务的标识唯一id
     */
    public void retryTask(long downloadId) {

        downloadManager.restartDownload(downloadId);
    }

    public static File getFile(String url) {

        File dir = new File(DOWNLOAD_DIR);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("不能创建目录：" + DOWNLOAD_DIR);
            }
        }
        File realFile = new File(dir, url.substring(url.lastIndexOf("/")));
        if (!realFile.exists()) {
            try {
                if (!realFile.createNewFile()) {
                    throw new RuntimeException("不能创建文件：" + realFile.getAbsolutePath());
                }
            } catch (IOException e) {
                DLoggerUtils.e(e);
            }
        }
        return realFile;
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}
