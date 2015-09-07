package com.dh.foundation.widget;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.dahanis.foundation.R;
import com.dh.foundation.adapter.DhNetListViewBaseAdapter;
import com.dh.foundation.exception.DhRequestError;
import com.dh.foundation.utils.AutoPrintHttpNetUtils;
import com.dh.foundation.utils.DhHttpNetUtils;
import com.dh.foundation.utils.NetWorkDetector;
import com.dh.foundation.utils.NumUtils;
import com.dh.foundation.utils.ProgressDialogUtil;
import com.dh.foundation.utils.RequestParams;
import com.dh.foundation.utils.ToastUtils;
import com.dh.foundation.utils.bluetooth.bluetoothbean.BaseBean;
import com.google.gson.JsonObject;
import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 网络获取数据ListView
 * Created By: Seal.Wu
 * Date: 2015/1/4
 * Time: 14:57
 */
public class DhNetListView extends ListView {
    private List list;//数据列表
    private DhNetListViewBaseAdapter adapter;//专用适配器
    private RequestParams params;//参数
    private boolean loadMoreAble;
    private View loadMoreView;//加载更多提示控件
    private String pageName;//分页名称
    private String baseAddress;
    private int pageNo = 0;//请求服务器数据的页码
    private int startPageNo;//分页功能起始页码
    private LoadMoreAbleListener loadMoreAbleListener;//扩展接口，是否可加载更多自定义使能
    private OnLoadFinishListener onLoadFinishListener;
    private boolean isLoadOkToast = true;//加载全部是否进行提示
    private boolean isShowProgressDialog = true;

    private boolean refreshing;//是否正在刷新

    private static final int DEFAULT_LOAD_MORE_LAYOUT = R.layout.dh_net_lv_load_more;//加载更多脚部

    /**
     * 　数据为空指示view
     */
    private int emptyViewLayout;


    private OnScrollListener onScrollListener;

    /**
     * 妥协处理，泛型仅用于交互
     */
    private DhHttpNetUtils.RequestListener<BaseBean<List<JsonObject>>> listener = new DhHttpNetUtils.RequestListener<BaseBean<List<JsonObject>>>() {
        @Override
        public void onSuccessfully(BaseBean<List<JsonObject>> t) {

            final List<JsonObject> invoke = t.getReturnData();

            if (refreshing) {
                list.clear();//如果此次请求是刷新动作，则先清空数据
            }
            list.addAll(invoke);
            setLoadMoreAble(t, invoke);
            if (!loadMoreAble && isLoadOkToast) {
                ToastUtils.toast(getContext(), "已全部加载");
            }
        }

        @Override
        public void onFailure(DhRequestError requestError) {
            loadMoreFailedReset();

        }

        @Override
        public void onFinished() {
            if (onLoadFinishListener != null) {
                onLoadFinishListener.onLoadFinished();
            }
            removeFooterView(loadMoreView);
            adapter.notifyDataSetChanged();

            refreshing = false;

            ProgressDialogUtil.dismissProgressDialog();
        }

    };


    public DhNetListView(Context context) {
        this(context,null);
    }

    public DhNetListView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.listViewStyle);
    }

    public DhNetListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.DhNetListView, R.attr.DhNetListViewStyle, 0);

        int loadMoreLayout = a.getResourceId(R.styleable.DhNetListView_load_more_layout, DEFAULT_LOAD_MORE_LAYOUT);

        loadMoreView = LayoutInflater.from(getContext()).inflate(loadMoreLayout, this, false);

         emptyViewLayout = a.getResourceId(R.styleable.DhNetListView_empty_view, 0);


        a.recycle();
    }


    public void setLoadMoreView(View loadMoreView) {
        this.loadMoreView = loadMoreView;
    }

    /**
     * 加载全部是否进行提示
     */
    public void setLoadOkToast(boolean isLoadOkToast) {
        this.isLoadOkToast = isLoadOkToast;
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
    public void setLoadMoreAbleListener(LoadMoreAbleListener loadMoreAbleListener) {
        this.loadMoreAbleListener = loadMoreAbleListener;
    }

    /**
     * 设置自加载完成监听器
     */
    public void setOnLoadFinishListener(OnLoadFinishListener onLoadFinishListener) {
        this.onLoadFinishListener = onLoadFinishListener;
    }

    @Override
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public void setSuperOnScrollListener(OnScrollListener onScrollListener) {
        super.setOnScrollListener(onScrollListener);
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
     * @param params    参数
     * @param adapter   专用适配器
     * @param pageName  页码名称
     */
    public void initNetListView(String baseAddress, RequestParams params, final DhNetListViewBaseAdapter adapter, String pageName) {

        View emptyView = null;

        if (emptyViewLayout != 0) {

            emptyView = getRootView().findViewById(emptyViewLayout);
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
    public void initNetListView(String baseAddress, RequestParams params, final DhNetListViewBaseAdapter adapter, String pageName, final View emptyView) {
        this.baseAddress = baseAddress;
        this.params = params == null ? new RequestParams() : params;
        this.adapter = adapter;
        this.list = new ArrayList();
        this.pageName = pageName == null ? "" : pageName;
        String page = this.params.getParams().get(pageName);
        if (NumUtils.isInteger(page)) {
            startPageNo = pageNo = Integer.valueOf(page);
        }
        this.adapter.setList(list);
        addFooterView(loadMoreView);
        setAdapter(adapter);
        removeFooterView(loadMoreView);
        super.setOnScrollListener(new OnScrollListener() {
            private int lastVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && lastVisibleItem == adapter.getCount() - 1 && loadMoreAble) {
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

        if (emptyView != null) {
            emptyView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    emptyView.setVisibility(View.GONE);
                    refreshData();
                }
            });
            setEmptyView(emptyView);
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
    private void getData() {
        if (isNetStateUnavailable(getContext(), listener)) {
            return;
        }
        ProgressDialogUtil.showProgressDialog(getContext());
        params.setParams(pageName, pageNo + "");
        AutoPrintHttpNetUtils.getData(getContext(), baseAddress, params, getSuperclassTypeParameter(adapter.getClass()), listener);
        if (pageNo != startPageNo || !isShowProgressDialog()) {//代表在刷新,仅仅刷新的时候显示等待进度条
            ProgressDialogUtil.dismissProgressDialog();
        }
    }

    /**
     * 加载更多
     */
    public void loadMore() {
        loadMoreAble = false;//不能重复加载直到此次加载完毕
        pageNo++;
        addFooterView(loadMoreView);
        setSelection(getBottom());
        getData();
    }

    /**
     * 加载更多失败时恢复加载更多之前参数值配置
     */
    private void loadMoreFailedReset() {
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
    private void setLoadMoreAble(BaseBean<List<JsonObject>> t, List<JsonObject> invoke) {
        /**
         * 优先使用自定义使能判断
         */
        if (loadMoreAbleListener != null) {
            loadMoreAble = loadMoreAbleListener.isLoadMoreAble(t, invoke);
            /**
             * 再根据传入参数进行判断
             */
        } else {
            final String pageSize = params.getParams().get(DhNetListView.KEY_PAGE_SIZE);
            if (pageSize != null && NumUtils.isInteger(pageSize)) {
                loadMoreAble = invoke.size() >= Integer.valueOf(pageSize);
                /**
                 * 使用默认处理方式
                 */
            } else {
                loadMoreAble = invoke.size() >= DEFAULT_PAGE_SIZE;
            }
        }
    }

    /**
     * 可加载更多扩展全能接口
     *
     * @param <T> 网络获取的结果对象类型
     */
    public interface LoadMoreAbleListener<T extends BaseBean> {
        /**
         * @param t    当前请求服务器返回的请求结果对象
         * @param list listView中全部的数据列表
         * @return 是否可以加载更多
         */
        public boolean isLoadMoreAble(T t, Collection list);
    }

    public interface OnLoadFinishListener {
        public void onLoadFinished();
    }

    /**
     * Returns the type from super class's type parameter in {@link $Gson$Types#canonicalize
     * canonical form}.
     */
    static Type getSuperclassTypeParameter(Class<?> subclass) {
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
    private static <T> boolean isNetStateUnavailable(Context context, DhHttpNetUtils.RequestListener<T> requestListener) {
        if (!NetWorkDetector.isNetConnected()) {
            ToastUtils.toast(context, "无可用网络请检查网络设置");
            if (requestListener != null) {
                requestListener.onFailure(new DhRequestError(new NetworkErrorException("无可用网络请检查网络设置")));
                requestListener.onFinished();
            }
            return true;
        }
        return false;
    }

    private final static int DEFAULT_PAGE_SIZE = 10;
    public static String KEY_PAGE_SIZE = "pageCount";

}
