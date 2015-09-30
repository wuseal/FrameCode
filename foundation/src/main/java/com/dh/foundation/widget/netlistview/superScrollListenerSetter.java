package com.dh.foundation.widget.netlistview;

import android.widget.AbsListView;

/**
 * 父类滚动监听设置器
 * Created By: Seal.Wu
 * Date: 2015/9/28
 * Time: 17:16
 */
interface SuperScrollListenerSetter {

    /**
     * 设置滚动监听器
     *
     * @param l 监听器
     */
    void setSuperOnScrollListener(AbsListView.OnScrollListener l);
}
