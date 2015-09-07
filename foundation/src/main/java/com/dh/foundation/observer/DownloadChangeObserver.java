package com.dh.foundation.observer;

import android.app.DownloadManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;

import com.dh.foundation.manager.FoundationManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 下载改变观察者
 * Created By: Seal.Wu
 * Date: 2015/8/27
 * Time: 17:58
 */
public class DownloadChangeObserver extends ContentObserver {
    private Handler handler;

    private static List<Long> downloadIds = new ArrayList<>();

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public DownloadChangeObserver(Handler handler) {
        super(handler);
        this.handler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {


        for (long downloadId : downloadIds) {

            int[] bytesAndStatus = getBytesAndStatus(downloadId);
            if (bytesAndStatus[1] != -1) {

                handler.sendMessage(handler.obtainMessage(downloadIds.indexOf(downloadId), bytesAndStatus[0], bytesAndStatus[1],
                        bytesAndStatus[2]));
            }
        }
    }

    private int[] getBytesAndStatus(long downloadId) {
        DownloadManager downloadManager = FoundationManager.getDownloadManager();

        int[] bytesAndStatus = new int[]{-1, -1, 0};
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = null;
        try {
            c = downloadManager.query(query);
            if (c != null && c.moveToFirst()) {
                bytesAndStatus[0] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                bytesAndStatus[1] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                bytesAndStatus[2] = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return bytesAndStatus;
    }

    public static void registerDownloadTask(long downloadId) {
        downloadIds.add(downloadId);
    }

    public static long getDownloadId(int position) {
        return downloadIds.get(position);
    }
}
