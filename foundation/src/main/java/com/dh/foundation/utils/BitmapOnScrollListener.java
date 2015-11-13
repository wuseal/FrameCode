package com.dh.foundation.utils;

import android.widget.AbsListView;

/**
 * 图片加载获取列表滚动监听器
 * Created By: Seal.Wu
 * Date: 2015/11/12
 * Time: 17:44
 */
public class BitmapOnScrollListener implements AbsListView.OnScrollListener{

    /**
     * 自定义的要使用的onScrollListener
     */
    private AbsListView.OnScrollListener onScrollListener;

    /**
     * bitmap图片加载器
     */
    private ImageNetLoader imageNetLoader;

    /**
     * 当正常触摸滑动的时候是否加载bitmap
     */
    private boolean loadBitmapWhenTouchScroll;


    public BitmapOnScrollListener(ImageNetLoader imageNetLoader, boolean loadBitmapWhenTouchScroll) {

        this(imageNetLoader, loadBitmapWhenTouchScroll, null);
    }

    public BitmapOnScrollListener(ImageNetLoader imageNetLoader, boolean loadBitmapWhenTouchScroll,AbsListView.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
        this.imageNetLoader = imageNetLoader;
        this.loadBitmapWhenTouchScroll = loadBitmapWhenTouchScroll;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == SCROLL_STATE_FLING) {

            imageNetLoader.setEnable(false);

        } else if (scrollState == SCROLL_STATE_IDLE) {

            if (!imageNetLoader.isEnable()) {

                imageNetLoader.resumeAllImageViews();
            }

        }else if (scrollState == SCROLL_STATE_TOUCH_SCROLL && loadBitmapWhenTouchScroll) {

            if (!imageNetLoader.isEnable()) {

                imageNetLoader.resumeAllImageViews();
            }
        } else {

            imageNetLoader.setEnable(false);
        }

        if (this.onScrollListener != null) {

            this.onScrollListener.onScrollStateChanged(view, scrollState);
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (this.onScrollListener != null) {

            this.onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

    }
}
