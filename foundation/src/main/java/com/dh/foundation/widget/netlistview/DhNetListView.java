package com.dh.foundation.widget.netlistview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 大含专用网络获取数据ListView
 * Created By: Seal.Wu
 * Date: 2015/1/4
 * Time: 14:57
 */
public class DhNetListView extends NetListViewCompat {

    private DhDefaultLoadMoreAbleListener dhDefaultLoadMoreAbleListener = new DhDefaultLoadMoreAbleListener();


    public DhNetListView(Context context) {
        super(context);
        init();
    }

    public DhNetListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DhNetListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    public void init() {

        if (!isInEditMode()) {

            setLoadMoreAbleListener(dhDefaultLoadMoreAbleListener);
        }

    }


    @Override
    public void setLoadMoreAbleListener(LoadMoreAbleListener loadMoreAbleListener) {

        if (loadMoreAbleListener instanceof DhDefaultLoadMoreAbleListener) {

            this.dhDefaultLoadMoreAbleListener = (DhDefaultLoadMoreAbleListener) loadMoreAbleListener;
        }
        super.setLoadMoreAbleListener(loadMoreAbleListener);
    }

    /**
     * 设置每页的页面大小即每页预期加载的item数量
     *
     * @param pageSize item数量
     */
    public void setPageSize(int pageSize) {
        dhDefaultLoadMoreAbleListener.setPageSize(pageSize);
    }

}
