package com.dh.foundation.manager;

import android.app.Activity;

import java.util.Stack;

/**
 * activity管理类
 * Created By: Seal.Wu
 * Date: 2015/4/25
 * Time: 22:59
 */
public class ActivityStackManager {
    private final Stack<Activity> stack = new Stack<Activity>();

    public void push(Activity activity) {
        stack.push(activity);
    }


    public void finishAll() {
        Activity activity;
        while (!stack.empty() && (activity = stack.pop()) != null) {
            activity.finish();
        }
    }


    public void pop(Activity activity) {
        if (stack.contains(activity)) {
            stack.remove(activity);
        }
    }

    public Stack<Activity> getStack() {
        return stack;
    }
}
