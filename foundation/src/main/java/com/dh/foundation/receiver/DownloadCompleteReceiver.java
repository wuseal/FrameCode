package com.dh.foundation.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;

import com.dh.foundation.manager.FoundationManager;
import com.dh.foundation.utils.download.DownLoadUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 下载完成广播接收器
 * Created By: Seal.Wu
 * Date: 2015/8/28
 * Time: 18:03
 */
public class DownloadCompleteReceiver extends BroadcastReceiver {
    public static final int DOWNLOAD_COMPLETE = -1;

    private static List<Long> downloadIds = new ArrayList<>();


    @Override
    public void onReceive(Context context, Intent intent) {
        // get complete download id
        long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        // to do here
        Handler handler = FoundationManager.getHandleManager().getHandler(DownLoadUtil.DOWNLOAD_COMPLETE_HANDLER);
        DownloadManager downloadManager = FoundationManager.getDownloadManager();
        Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(completeDownloadId));
        if (cursor != null && cursor.moveToFirst()) {
            if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                if (handler != null && downloadIds.contains(completeDownloadId)) {
                    handler.sendMessage(handler.obtainMessage(DOWNLOAD_COMPLETE, completeDownloadId));
                    downloadIds.remove(completeDownloadId);
                }
            }
        }
        if (cursor != null && !cursor.isClosed()) {

            cursor.close();
        }

    }

    public static void registerDownloadTask(long downloadId) {
        downloadIds.add(downloadId);
    }

    public static long getDownloadId(int position) {
        return downloadIds.get(position);
    }
}
