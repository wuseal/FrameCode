package com.dh.foundation.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.dh.foundation.volley.toolbox.ImageLoader;

public class BitmapCache implements ImageLoader.ImageCache {

    private LruCache<String, Bitmap> cache;

    BitmapCache() {

        // 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。
        // LruCache通过构造函数传入缓存值，以KB为单位。
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // 使用最大可用内存值的1/8作为缓存的大小。
        int cacheSize = maxMemory / 4;

        DLoggerUtils.i("cacheSize====" + cacheSize);

        cache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {

                // 重写此方法来衡量每张图片的大小，默认返回图片数量。
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return cache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        cache.put(url, bitmap);
    }

}
