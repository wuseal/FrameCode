package com.dh.foundation.volley.patch;

import com.android.volley.Cache;

/**
 * 图片缓存入口记录对象:图片永远不过期
 * Created By: Seal.Wu
 * Date: 2015/5/20
 * Time: 10:54
 */
public class ImageCacheEntry extends Cache.Entry {

    @Override
    public boolean isExpired() {
        return false;
    }


    @Override
    public boolean refreshNeeded() {
        return false;
    }
}
