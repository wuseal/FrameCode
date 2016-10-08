package com.dh.foundation.volley.patch;

import com.dh.foundation.volley.toolbox.DiskBasedCache;

import java.io.File;

/**
 * 图片硬盘存储缓存
 * Created By: Seal.Wu
 * Date: 2015/5/20
 * Time: 11:01
 */
public class ImageDiskBasedCache extends DiskBasedCache {

    /**
     * Default maximum disk usage in bytes.
     */
    private static final int DEFAULT_DISK_USAGE_BYTES = 100 * 1024 * 1024;

    public ImageDiskBasedCache(File rootDirectory, int maxCacheSizeInBytes) {
        super(rootDirectory, maxCacheSizeInBytes);
    }

    public ImageDiskBasedCache(File rootDirectory) {
        super(rootDirectory, DEFAULT_DISK_USAGE_BYTES);
    }


    @Override
    public synchronized Entry get(String key) {
        Entry entry = super.get(key);
        if (entry != null) {

            ImageCacheEntry imageCacheEntry = new ImageCacheEntry();

            imageCacheEntry.data = entry.data;
            imageCacheEntry.etag = entry.etag;
            imageCacheEntry.responseHeaders = entry.responseHeaders;
            imageCacheEntry.serverDate = entry.serverDate;
            imageCacheEntry.softTtl = entry.softTtl;
            imageCacheEntry.ttl = entry.ttl;

            return imageCacheEntry;
        }
        return entry;
    }
}
