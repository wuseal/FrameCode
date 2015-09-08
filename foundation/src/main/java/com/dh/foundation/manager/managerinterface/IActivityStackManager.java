package com.dh.foundation.manager.managerinterface;

import android.app.Activity;

import java.util.Stack;

/**
 * Activity管理类接口
 * Created By: Seal.Wu
 * Date: 2015/9/7
 * Time: 17:53
 */
public interface IActivityStackManager {

    /**
     * 入栈activity
     */
    void push(Activity activity);


    /**
     * 结束所有activity
     */
    void finishAll();


    /**
     * 出栈activity
     */
    void pop(Activity activity);


    /**
     * 获取栈列表
     */
    Stack<Activity> getStack();
}
