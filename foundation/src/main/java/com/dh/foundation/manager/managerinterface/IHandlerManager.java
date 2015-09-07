package com.dh.foundation.manager.managerinterface;


import android.os.Handler;

/**
 * HandlerManager管理器接口
 * Created By: Seal.Wu
 * Date: 2015/5/13
 * Time: 14:10
 */
public interface IHandlerManager {
    /**
     * 注册一个handler
     *
     * @param key     映射关键字
     * @param handler 消息接收处理器
     */
    void registerHandler(Object key, Handler handler);

    /**
     * 移除一个handler
     */
    void removeHandler(Object key);

    /**
     * 获取key对应的handler
     */
    Handler getHandler(Object key);
}
