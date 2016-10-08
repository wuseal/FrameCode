package com.dh.foundation.utils;

/**
 * 级别变化监听器
 * Created By: Seal.Wu
 * Date: 2016/7/19
 * Time: 9:51
 */
public interface LevelChangeListener {

    /**
     * 层级变化监听
     * @param levelPre
     * 级别变化之前的值
     * @param levelAfter
     * 级别变化之后的值
     */
    void onLevelChange(int levelPre, int levelAfter);


}
