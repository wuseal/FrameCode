package com.dh.foundation.utils.download;

/**
 * 下载监听器
 * Created By: Seal.Wu
 * Date: 2015/8/31
 * Time: 17:57
 */
public interface DownloadListener {


    /**
     * 下载变化过程
     *
     * @param total       下载的文件总尺寸大小
     * @param currentSize 当前已经下载的大小
     * @param state       下载状态
     */
    void onLoadChange(int total, int currentSize, int state);


    /**
     * 下载完成
     */
    void onComplete(long downloadId,String filePath);

}
