package com.dh.foundation.widget.netlistview;

import com.dh.foundation.utils.bluetooth.bluetoothbean.BaseBean;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * 大含专用默认加载更多监听器
 * Created By: Seal.Wu
 * Date: 2015/9/28
 * Time: 16:00
 */
public class DhDefaultLoadMoreAbleListener implements NLVCommonInterface.LoadMoreAbleListener<BaseBean<List<JsonObject>>> {

    /**
     * 默认的每页的条数大小
     */
    private final static int defaultPageSize = 10;

    private int pageSize = defaultPageSize;

    @Override
    public boolean isLoadMoreAble(BaseBean<List<JsonObject>> listBaseBean, List<?> allListData) {

        if (listBaseBean.getReturnData().size() == getPageSize()) {

            return  true;
        }
        return false;
    }

    @Override
    public List<?> getLoadedData(BaseBean<List<JsonObject>> returnObj) {

        return returnObj.getReturnData();
    }

    public int getPageSize() {
        return pageSize;
    }

    public DhDefaultLoadMoreAbleListener setPageSize(int pageSize) {

        this.pageSize = pageSize;

        return this;
    }
}
