package com.dh.foundation.widget.netlistview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.dahanis.foundation.R;
import com.dh.foundation.adapter.NetListViewBaseAdapter;
import com.dh.foundation.utils.HttpNetUtils;
import com.dh.foundation.utils.RequestParams;

/**
 * 网络获取数据ListView
 * Created By: Seal.Wu
 * Date: 2015/9/28
 * Time: 15:34
 */
public class NetListView extends ListView implements NLVCommonInterface,SuperScrollListenerSetter {

    private NLVCommonInterface netListViewDelegate;

    private static final int DEFAULT_LOAD_MORE_LAYOUT = R.layout.dh_net_lv_load_more;//加载更多脚部


    public NetListView(Context context) {
        this(context, null);
    }

    public NetListView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.listViewStyle);
    }

    public NetListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.NetListView, R.attr.NetListViewStyle, 0);

        int loadMoreLayout = a.getResourceId(R.styleable.NetListView_load_more_layout, DEFAULT_LOAD_MORE_LAYOUT);

        if (!isInEditMode()) {

            netListViewDelegate = new NetListViewDelegate(this);

            setLoadMoreView(LayoutInflater.from(getContext()).inflate(loadMoreLayout, this, false));

            setEmptyViewId(a.getResourceId(R.styleable.NetListView_empty_view_id, 0));
        }



        a.recycle();
    }



    public void setLoadMoreView(View loadMoreView) {

        netListViewDelegate.setLoadMoreView(loadMoreView);
    }

    @Override
    public void setEmptyViewId(int emptyViewId) {

        netListViewDelegate.setEmptyViewId(emptyViewId);
    }

    /**
     * 加载全部是否进行提示
     */
    public void setLoadOkToast(boolean isLoadOkToast) {
        netListViewDelegate.setLoadOkToast(isLoadOkToast);
    }

    /**
     * 设置是否展示等待对话框
     */
    public void setIsShowProgressDialog(boolean isShowProgressDialog) {
        netListViewDelegate.setIsShowProgressDialog(isShowProgressDialog);
    }

    public boolean isShowProgressDialog() {
        return netListViewDelegate.isShowProgressDialog();
    }

    /**
     * 设置自定义加载更多使能
     */
    public void setLoadMoreAbleListener(NetListView.LoadMoreAbleListener loadMoreAbleListener) {
        netListViewDelegate.setLoadMoreAbleListener(loadMoreAbleListener);
    }

    /**
     * 设置自加载完成监听器
     */
    public void setOnLoadFinishListener(NetListView.OnLoadFinishListener onLoadFinishListener) {
        netListViewDelegate.setOnLoadFinishListener(onLoadFinishListener);
    }

    @Override
    public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
        netListViewDelegate.setOnScrollListener(onScrollListener);
    }

    public void setSuperOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
        super.setOnScrollListener(onScrollListener);
    }

    /**
     * 获取当前分页页码
     */
    public int getPageNo() {
        return netListViewDelegate.getPageNo();
    }

    /**
     * 初始化网络处理功能ListView
     *
     * @param params    参数
     * @param adapter   专用适配器
     * @param pageName  页码名称
     */
    public void initNetListView(String baseAddress, RequestParams params, final NetListViewBaseAdapter adapter, String pageName) {

        netListViewDelegate.initNetListView(baseAddress, params, adapter, pageName);
    }

       /**
     * 初始化网络处理功能ListView
     *
     * @param params    参数
     * @param adapter   专用适配器
     * @param pageName  页码名称
     * @param emptyView 当无数据时指定显示的view
     */
    public void initNetListView(String baseAddress, RequestParams params, final NetListViewBaseAdapter adapter, String pageName, final View emptyView) {

        netListViewDelegate.initNetListView(baseAddress, params, adapter, pageName, emptyView);
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        netListViewDelegate.refreshData();
    }

    /**
     * 加载更多
     */
    public void loadMore() {
        netListViewDelegate.loadMore();
    }


    @Override
    protected void onDetachedFromWindow() {

        HttpNetUtils.cancelAll(this);

        super.onDetachedFromWindow();
    }
}
