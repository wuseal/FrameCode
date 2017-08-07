package com.dh.foundation.widget.netlistview;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.dh.foundation.adapter.NetListViewBaseAdapter;
import com.dh.foundation.exception.DhRequestError;
import com.dh.foundation.utils.AutoPrintHttpNetUtils;
import com.dh.foundation.utils.DLoggerUtils;
import com.dh.foundation.utils.HttpNetUtils;
import com.dh.foundation.utils.NetWorkDetector;
import com.dh.foundation.utils.NumUtils;
import com.dh.foundation.utils.ProgressDialogUtil;
import com.dh.foundation.utils.ReflectUtils;
import com.dh.foundation.utils.RequestParams;
import com.dh.foundation.utils.ToastUtils;
import com.google.gson.internal.$Gson$Types;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 网络请求列表委托者
 * Created by Seal.Wu
 * On :2015.09.28
 */
class NetListViewDelegate implements NLVCommonInterface, ParamMakerSetter {

    private final ListView listView;

    private final Context context;

    private List<Object> list;//数据列表

    private NetListViewBaseAdapter adapter;//专用适配器

    private RequestParams params;//参数

    private boolean loadMoreAble;

    private View loadMoreView;//加载更多提示控件

    private String pageName;//分页名称

    private String baseAddress;

    private int pageNo = 0;//请求服务器数据的页码

    private int startPageNo;//分页功能起始页码

    private NLVCommonInterface.LoadMoreAbleListener<Object> loadMoreAbleListener;//扩展接口，是否可加载更多自定义使能

    private NLVCommonInterface.OnLoadFinishListener<Object> onLoadFinishListener;

    private NLVCommonInterface.OnLoadStartListener onLoadStartListener;//刚开始加载监听器

    private boolean isLoadOkToast = true;//加载全部是否进行提示

    private boolean isNetErrorToast = true;//是否出错提示

    private boolean isShowProgressDialog = true;

    private boolean refreshing = true;//是否正在刷新,第一次加载默认当刷新
    /**
     * 　数据为空指示view
     */
    private int emptyViewId;

    private View emptyView;

    private View netErrorView;

    /**
     * 网络请求出错提示viewId
     */
    private int netErrorViewId;

    private AbsListView.OnScrollListener onScrollListener;
    /**
     * 妥协处理，泛型仅用于交互
     */
    private HttpNetUtils.RequestListener<Object> listener;

    private NLVCommonInterface.ParamMaker paramMaker;


    private static class RequestListener extends HttpNetUtils.RequestListener<Object> {

        private boolean isLoadSuccess = true;

        private Throwable error;

        private Object returnData;

        private WeakReference<NetListViewDelegate> weakReference;

        public RequestListener(NetListViewDelegate netListViewDelegate) {

            this.weakReference = new WeakReference<NetListViewDelegate>(netListViewDelegate);
        }

        @Override
        public void onSuccess(Object t) {

            isLoadSuccess = true;

            returnData = t;

            final NetListViewDelegate netListViewDelegate = weakReference.get();

            if (netListViewDelegate == null) {

                return;
            }

            if (netListViewDelegate.loadMoreAbleListener == null) {

                return;
            }

            if (netListViewDelegate.netErrorView != null) {

                netListViewDelegate.netErrorView.setVisibility(View.GONE);
            }

            final List<?> fetchedData = netListViewDelegate.loadMoreAbleListener.getLoadedData(t);

            if (netListViewDelegate.refreshing) {

                netListViewDelegate.list.clear();//如果此次请求是刷新动作，则先清空数据
            }

            if (fetchedData != null) {

                netListViewDelegate.list.addAll(fetchedData);
            }

            netListViewDelegate.setLoadMoreAble(t, fetchedData);

            if (!netListViewDelegate.loadMoreAble && netListViewDelegate.isLoadOkToast) {

                ToastUtils.toast("已全部加载");
            }
        }

        @Override
        public void onFailed(Throwable throwable) {

            isLoadSuccess = false;

            this.error = throwable;

            final NetListViewDelegate netListViewDelegate = weakReference.get();

            if (netListViewDelegate != null) {

                netListViewDelegate.loadMoreFailedReset();

                if (netListViewDelegate.netErrorView != null && netListViewDelegate.list.isEmpty()) {

                    netListViewDelegate.netErrorView.setVisibility(View.VISIBLE);

                    if (netListViewDelegate.listView.getEmptyView() != null) {

                        netListViewDelegate.listView.getEmptyView().setVisibility(View.GONE);
                    }

                }

                if (netListViewDelegate.isNetErrorToast) {

                    final String message = throwable.getCause().getMessage();
                    ToastUtils.toast(message);
                }
            }

        }

        @Override
        public void onFinished() {

            final NetListViewDelegate netListViewDelegate = weakReference.get();

            if (netListViewDelegate != null) {


                if (netListViewDelegate.onLoadFinishListener != null) {

                    netListViewDelegate.onLoadFinishListener.onLoadFinished(netListViewDelegate.refreshing, isLoadSuccess, returnData, error);
                }

                netListViewDelegate.listView.removeFooterView(netListViewDelegate.loadMoreView);

                if (isLoadSuccess) {

                    netListViewDelegate.adapter.notifyDataSetChanged();
                }


                netListViewDelegate.refreshing = false;
            }

            ProgressDialogUtil.dismissProgressDialog();
        }

    }

    public NetListViewDelegate(ListView listView) {

        this.listView = listView;

        this.context = listView.getContext();

        this.listener = new RequestListener(this);
    }


    @Override
    public void setParamMaker(ParamMaker paramMaker) {
        this.paramMaker = paramMaker;
    }

    public void setLoadMoreView(View loadMoreView) {
        this.loadMoreView = loadMoreView;
    }

    @Override
    public void setEmptyViewId(int emptyViewId) {

        this.emptyViewId = emptyViewId;

        if (this.emptyViewId != 0) {

            final View emptyView = listView.getRootView().findViewById(emptyViewId);

            this.emptyView = emptyView;

            if (emptyView != null) {

                emptyView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        emptyView.setVisibility(View.GONE);

                        refreshData();
                    }
                });

                listView.setEmptyView(emptyView);

                emptyView.setVisibility(View.GONE);

            }
        }
    }

    @Override
    public void setNetErrorViewId(int netErrorViewId) {

        this.netErrorViewId = netErrorViewId;

        if (this.netErrorViewId != 0) {

            final View netErrorView = listView.getRootView().findViewById(netErrorViewId);

            this.netErrorView = listView.getRootView().findViewById(netErrorViewId);

            if (netErrorView != null) {

                netErrorView.setVisibility(View.GONE);

                netErrorView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        netErrorView.setVisibility(View.GONE);

                        refreshData();
                    }
                });
            }

        }
    }

    @Override
    public View getNetErrorView() {
        return netErrorView;
    }

    /**
     * 加载全部是否进行提示
     */
    public void setLoadOkToast(boolean isLoadOkToast) {

        this.isLoadOkToast = isLoadOkToast;
    }

    @Override
    public void setNetErrorToast(boolean isNetErrorToast) {
        this.isNetErrorToast = isNetErrorToast;
    }

    /**
     * 设置是否展示等待对话框
     */
    public void setIsShowProgressDialog(boolean isShowProgressDialog) {

        this.isShowProgressDialog = isShowProgressDialog;
    }

    public boolean isShowProgressDialog() {

        return isShowProgressDialog;
    }

    /**
     * 设置自定义加载更多使能
     */
    public void setLoadMoreAbleListener(NLVCommonInterface.LoadMoreAbleListener loadMoreAbleListener) {

        this.loadMoreAbleListener = loadMoreAbleListener;
    }

    /**
     * 设置自加载完成监听器
     */
    public void setOnLoadFinishListener(NLVCommonInterface.OnLoadFinishListener onLoadFinishListener) {

        this.onLoadFinishListener = onLoadFinishListener;
    }

    @Override
    public void setOnLoadStartListener(OnLoadStartListener onLoadStartListener) {

        this.onLoadStartListener = onLoadStartListener;
    }

    public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {

        this.onScrollListener = onScrollListener;
    }

    /**
     * 获取当前分页页码
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * 初始化网络处理功能ListView
     *
     * @param params   参数
     * @param adapter  专用适配器
     * @param pageName 页码名称
     */
    public void initNetListView(String baseAddress, RequestParams params, final NetListViewBaseAdapter adapter, String pageName) {

        View emptyView = null;

        if (emptyViewId != 0) {

            emptyView = listView.getRootView().findViewById(emptyViewId);
        }

        initNetListView(baseAddress, params, adapter, pageName, emptyView);
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

        this.baseAddress = baseAddress;

        this.params = params == null ? new RequestParams() : params;

        this.adapter = adapter;

        this.list = new ArrayList<>();

        this.pageName = pageName == null ? "" : pageName;

        if (params != null) {

            if (paramMaker != null) {
                this.params = paramMaker.makeParam(params, refreshing);
            } else {

                if (params.isRestStyle()) {
                    Object paramObj = params.getParamsObj();
                    Field field = ReflectUtils.getDeclaredField(paramObj.getClass(), pageName);
                    if (field != null) {
                        field.setAccessible(true);
                        try {
                            startPageNo = pageNo = field.getInt(paramObj);
                        } catch (IllegalAccessException e) {
                            DLoggerUtils.e(e);
                        }
                    }

                } else {

                    String page = (String) this.params.getParams().get(pageName);

                    if (NumUtils.isInteger(page)) {

                        startPageNo = pageNo = Integer.valueOf(page);
                    }
                }
            }
        }

        this.adapter.setList(list);

        listView.addFooterView(loadMoreView);

        listView.setAdapter(adapter);

        listView.removeFooterView(loadMoreView);

        if (listView instanceof SuperScrollListenerSetter) {

            ((SuperScrollListenerSetter) listView).setSuperOnScrollListener(new AbsListView.OnScrollListener() {

                private int lastVisibleItem;

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                    if (scrollState == SCROLL_STATE_IDLE && lastVisibleItem == adapter.getCount() + listView.getHeaderViewsCount() + listView.getFooterViewsCount() - 1 && loadMoreAble) {

                        loadMore();
                    }
                    if (onScrollListener != null) {

                        onScrollListener.onScrollStateChanged(view, scrollState);
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    lastVisibleItem = firstVisibleItem + visibleItemCount - 1;

                    if (onScrollListener != null) {

                        onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                    }
                }
            });
        }

        if (emptyView != null) {

            emptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    emptyView.setVisibility(View.GONE);

                    refreshData();
                }
            });

            listView.setEmptyView(emptyView);

            emptyView.setVisibility(View.GONE);

        }
        getData();
    }

    /**
     * 刷新数据
     */
    public void refreshData() {

        refreshing = true;

        pageNo = startPageNo;

        getData();
    }

    /**
     * 请求网络数据
     */
    public void getData() {

        if (onLoadStartListener != null) {

            onLoadStartListener.onLoadStart(refreshing);
        }
        if (isNetStateUnavailable(listener)) {

            return;
        }
        ProgressDialogUtil.showProgressDialog(context);


        if (paramMaker != null) {

            this.params = paramMaker.makeParam(params, refreshing);

        } else {

            if (params.isRestStyle()) {
                Object paramObj = params.getParamsObj();
                Field field = ReflectUtils.getDeclaredField(paramObj.getClass(), pageName);
                if (field != null) {
                    field.setAccessible(true);
                    try {
                        field.setInt(paramObj, pageNo);
                    } catch (IllegalAccessException e) {
                        DLoggerUtils.e(e);
                    }
                }
            } else {
                params.setParams(pageName, pageNo + "");
            }
        }
        if (params.isRestStyle()) {

            AutoPrintHttpNetUtils.postData(baseAddress, params, getSuperclassTypeParameter(adapter.getClass()), listener).setTag(listView.hashCode());
        } else {
            AutoPrintHttpNetUtils.getData(baseAddress, params, getSuperclassTypeParameter(adapter.getClass()), listener).setTag(listView.hashCode());
        }

        if (!refreshing || !isShowProgressDialog()) {//代表在刷新,仅仅刷新的时候显示等待进度条

            ProgressDialogUtil.dismissProgressDialog();
        }
    }

    /**
     * 加载更多
     */
    public void loadMore() {

        loadMoreAble = false;//不能重复加载直到此次加载完毕

        pageNo++;

        listView.addFooterView(loadMoreView);

        listView.setSelection(listView.getBottom());

        getData();

    }

    /**
     * 加载更多失败时恢复加载更多之前参数值配置
     */
    public void loadMoreFailedReset() {

        if (pageNo != startPageNo) {

            pageNo--;

            loadMoreAble = true;
        }
    }

    /**
     * 设置是否可以加载更多
     *
     * @param t      网络请求返回对象
     * @param invoke 数据列表
     */
    private void setLoadMoreAble(Object t, List<?> invoke) {

        loadMoreAble = loadMoreAbleListener.isLoadMoreAble(t, invoke);
    }

    /**
     * Returns the type from super class's type parameter in {@link $Gson$Types#canonicalize
     * canonical form}.
     */
    private Type getSuperclassTypeParameter(Class<?> subclass) {

        Type superclass = subclass.getGenericSuperclass();

        if (superclass instanceof Class) {

            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;

        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    /**
     * 网络状态是否不可用
     */
    private <T> boolean isNetStateUnavailable(final HttpNetUtils.RequestListener<T> requestListener) {

        if (!NetWorkDetector.isNetConnected()) {

            listView.post(new Runnable() {
                @Override
                public void run() {

                    if (requestListener != null) {

                        requestListener.onFailed(new DhRequestError(new NetworkErrorException("无可用网络请检查网络设置")));

                        requestListener.onFinished();
                    }
                }
            });

            return true;
        }
        return false;
    }

}
