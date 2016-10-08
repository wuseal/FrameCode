package com.dh.foundation.utils;

/**
 * 层级管控接口
 * Created By: Seal.Wu
 * Date: 2016/6/23
 * Time: 17:55
 */
public interface ILevelControl {

    /**
     * 加一级
     */
    void addOneLevel();

    /**
     * 减一级
     */
    void reduceOneLevel();


    /**
     * 初始化到0级
     */
    void forceInitToLevelZero();

}
