package com.dh.foundation.manager;

import android.os.Handler;

import com.dh.foundation.manager.managerinterface.IHandlerManager;

import java.util.HashMap;

/**
 * Created By: Seal.Wu
 * Date: 2015/5/13
 * Time: 14:21
 */
class HandlerManager implements IHandlerManager {

    private final HashMap<Object, Handler> objectHandlerHashMap = new HashMap<Object,Handler>();
    private static final HandlerManager ourInstance = new HandlerManager();

    static HandlerManager getInstance() {
        return ourInstance;
    }

    private HandlerManager() {
    }

    @Override
    public void registerHandler(Object key, Handler handler) {
        objectHandlerHashMap.put(key, handler);
    }

    @Override
    public void removeHandler(Object key) {
        objectHandlerHashMap.remove(key);
    }

    @Override
    public Handler getHandler(Object key) {
        return objectHandlerHashMap.get(key);
    }
}
