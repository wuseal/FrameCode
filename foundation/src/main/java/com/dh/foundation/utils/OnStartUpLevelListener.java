package com.dh.foundation.utils;

/**
 * 启动升级监听器
 * Created By: Seal.Wu
 * Date: 2016/6/23
 * Time: 18:10
 */
public interface OnStartUpLevelListener {

    /**
     * 级别升级监听
     *
     * @param currentLevel 当前级别
     */
    void onStartUpLevel(int currentLevel);

}
