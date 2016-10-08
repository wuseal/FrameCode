package com.dh.foundation.manager;

import  com.dh.foundation.volley.toolbox.DiskBasedCache;
import com.dh.foundation.manager.managerinterface.IFoundationCacheManager;
import com.dh.foundation.utils.HttpNetUtils;
import com.dh.foundation.utils.ImageNetLoader;
import com.dh.foundation.utils.StringUtils;

import java.io.File;

/**
 * 公共基础包的缓存管理者
 * Created By: Seal.Wu
 * Date: 2016/6/8
 * Time: 11:18
 */
class FoundationCacheManager implements IFoundationCacheManager {

    private DiskBasedCache httpCache;


    private DiskBasedCache imageCache;

    private File httpCacheFile;

    private File imageCacheFile;

    FoundationCacheManager() {
        httpCache = ((DiskBasedCache) HttpNetUtils.getRequestQueue().getCache());
        imageCache = (DiskBasedCache) ImageNetLoader.newImageRequestQueue(FoundationManager.getContext(), null).getCache();
        String wrongHttpCachePath = httpCache.getFileForKey(StringUtils.EMPTY).getAbsolutePath();
        String rightHttpCachePath = wrongHttpCachePath.replace("/00", "");
        httpCacheFile = new File(rightHttpCachePath);
        String wrongImageCachePath = imageCache.getFileForKey(StringUtils.EMPTY).getAbsolutePath();
        String rightImageCachePath = wrongImageCachePath.replace("/00", "");
        imageCacheFile = new File(rightImageCachePath);

    }

    @Override
    public long getCacheSize() {

        Long size = getFileSize(httpCacheFile) + getFileSize(imageCacheFile);

        return size;
    }

    @Override
    public boolean clearCache() {

        httpCache.clear();
        imageCache.clear();

        return deleteFile(httpCacheFile) && deleteFile(imageCacheFile);

    }

    private long getFileSize(File file) {
        long size = 0;
        if (file.isDirectory()) {
            for (File file1 : file.listFiles()) {
                size += getFileSize(file1);
            }
        } else {
            size = file.length();
        }
        return size;
    }


    private boolean deleteFile(File file) {

        boolean OK = true;
        if (file.isDirectory()) {
            for (File file1 : file.listFiles()) {
                if (!deleteFile(file1)) {
                    OK = false;
                }
            }
        } else {
            if (!file.delete()) {
                OK = false;
            }

        }
        return OK;
    }

}
