package com.dh.foundation.widget.netlistview;

import android.view.View;
import android.widget.AbsListView;

import com.dh.foundation.adapter.NetListViewBaseAdapter;
import com.dh.foundation.utils.RequestParams;

import java.util.List;

/**
 * NetListView常规交互接口
 * Created By: Seal.Wu
 * Date: 2015/9/28
 * Time: 17:35
 */
interface NLVCommonInterface {

    void setLoadMoreView(View loadMoreView);

    void setEmptyViewId(int emptyViewId);

    void setLoadOkToast(boolean isLoadOkToast);

    void setIsShowProgressDialog(boolean isShowProgressDialog);

    boolean isShowProgressDialog();

    void setLoadMoreAbleListener(LoadMoreAbleListener loadMoreAbleListener);

    void setOnLoadFinishListener(OnLoadFinishListener onLoadFinishListener);

    void setOnScrollListener(AbsListView.OnScrollListener onScrollListener);

    int getPageNo();

    void initNetListView(String baseAddress, RequestParams params, NetListViewBaseAdapter adapter, String pageName);

    void initNetListView(String baseAddress, RequestParams params, NetListViewBaseAdapter adapter, String pageName, View emptyView);

    void refreshData();

    void loadMore();




    /**
     * 可加载更多扩展全能接口
     *
     * @param <T> 网络获取的结果对象类型
     */
    interface LoadMoreAbleListener<T> {
        /**
         * @param returnObj           当前请求服务器返回的请求结果对象
         * @param allListData listView中全部的数据列表
         * @return 是否可以加载更多
         */
        boolean isLoadMoreAble(T returnObj, List<?> allListData);


        /**
         * 获取每次获取到的列表内容
         *
         * @param returnObj 服务器返回的整体对象
         * @return 获取到的整体对象中和列表有关的列表数据
         */
        List<?> getLoadedData(T returnObj);
    }

    interface OnLoadFinishListener {

        void onLoadFinished();
    }
}
