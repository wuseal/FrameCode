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
public interface NLVCommonInterface extends ParamMakerSetter,ServerReturnDataOKListenerSetter {

    /**
     * 设置加载更多的footView
     *
     * @param loadMoreView 加载更多的footView
     */
    void setLoadMoreView(View loadMoreView);

    /**
     * 设置ListView的EmptyView
     *
     * @param emptyViewId ListView的EmptyView
     */
    void setEmptyViewId(int emptyViewId);


    /**
     * 设置网络请求错显示的view的id
     *
     * @param netErrorViewId 网络请求错显示的view的id
     */
    void setNetErrorViewId(int netErrorViewId);



    /**
     * 设置服务器返回错误码显示的view的id
     *
     * @param serverReturnDataErrorViewId 服务返回错误码显示的view的id
     */
    void setNetServerReturnDataErrorViewId(int serverReturnDataErrorViewId);


    /**
     * 获取网络出错提示的View
     *
     * @return 网络出错提示的View
     */
    View getNetErrorView();


    /**
     * 获取网络出错提示的View
     *
     * @return 网络出错提示的View
     */
    View getServerReturnDataErrorView();


    /**
     * 设置是否弹出提示＂已经全部加载＂
     *
     * @param isLoadOkToast 是否弹出提示＂已经全部加载＂
     */
    void setLoadOkToast(boolean isLoadOkToast);


    /**
     * 是否设置网络出错提示
     *
     * @param isNetErrorToast true:代表设置
     */
    void setNetErrorToast(boolean isNetErrorToast);

    /**
     * 设置刷新时是否显示等待框
     *
     * @param isShowProgressDialog 是否显示等待框
     */
    void setIsShowProgressDialog(boolean isShowProgressDialog);

    /**
     * 刷新时是否显示等待框
     *
     * @return true:代表是
     */
    boolean isShowProgressDialog();

    /**
     * 设置加载更多使能功能接口
     *
     * @param loadMoreAbleListener 加载更多使能功能接口
     */
    void setLoadMoreAbleListener(LoadMoreAbleListener loadMoreAbleListener);

    /**
     * 设置加载完成监听接口
     *
     * @param onLoadFinishListener 加载完成监听接口
     */
    void setOnLoadFinishListener(OnLoadFinishListener onLoadFinishListener);


    /**
     * 设置刚开始加载监听器
     *
     * @param onLoadStartListener 刚开始加载监听器
     */
    void setOnLoadStartListener(OnLoadStartListener onLoadStartListener);

    /**
     * 设置ListView滚动监听接口
     *
     * @param onScrollListener 滚动监听接口
     */
    void setOnScrollListener(AbsListView.OnScrollListener onScrollListener);

    /**
     * 获取当前ListView分页的页码数
     *
     * @return 当前ListView分页的页码数
     */
    int getPageNo();

    /**
     * 初始化网络处理功能ListView
     *
     * @param params   参数
     * @param adapter  专用适配器
     * @param pageName 页码名称
     */
    void initNetListView(String baseAddress, RequestParams params, NetListViewBaseAdapter adapter, String pageName);

    /**
     * 初始化网络处理功能ListView
     *
     * @param params    参数
     * @param adapter   专用适配器
     * @param pageName  页码名称
     * @param emptyView 当无数据时指定显示的view
     */
    void initNetListView(String baseAddress, RequestParams params, NetListViewBaseAdapter adapter, String pageName, View emptyView);

    /**
     * 刷新ListView数据
     */
    void refreshData();

    /**
     * 主动调用加载更多
     */
    void loadMore();


    /**
     * 可加载更多扩展全能接口
     *
     * @param <T> 网络获取的结果对象类型
     */
    interface LoadMoreAbleListener<T> {
        /**
         * @param returnObj   当前请求服务器返回的请求结果对象
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

    /**
     * 加载完成监听器
     */
    interface OnLoadFinishListener<ReturnData> {

        /**
         * 当一次加载完成时调用
         *
         * @param isRefreshing  是否是刷新动作
         * @param isLoadSuccess 是否成功通过网络请求到数据
         * @param returnData    返回的数据对象
         * @param error         失败时的错误提示
         */
        void onLoadFinished(boolean isRefreshing, boolean isLoadSuccess, ReturnData returnData, Throwable error);
    }


    /**
     * 开始加载数据监听器
     */
    interface OnLoadStartListener {

        /**
         * 当刚开始加载时调用
         *
         * @param isRefreshing 是否是刷新
         */
        void onLoadStart(boolean isRefreshing);
    }


    /**
     * 参数对象制造者
     */
    interface ParamMaker {

        /**
         * 生产出新的参数对象
         *
         * @param preParam 上一次请求使用的参数
         * @param isInit   是否是初始化NetListView或是刷新时请求这次参数生成
         * @return 新生成的即将被下一次请求使用的参数对象
         */
        RequestParams makeParam(RequestParams preParam, boolean isInit);
    }

}

interface ServerReturnDataOKListenerSetter {

    void setServerReturnDataOKListener(ServerReturnDataOKListener serverReturnDataOKListener);
}

