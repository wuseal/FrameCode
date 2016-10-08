package com.dh.foundation.manager.managerinterface;

/**
 * 公共基础包的缓存管理接口
 * Created By: Seal.Wu
 * Date: 2016/6/8
 * Time: 11:15
 */
public interface IFoundationCacheManager {

    /**
     * 获取缓存大小
     */
    long getCacheSize();


    /**
     * 清除缓存
     *
     * @return 成功全部清除返回true;
     * 清楚过程中只要有失败情况统一返回false
     */
    boolean clearCache();
}
