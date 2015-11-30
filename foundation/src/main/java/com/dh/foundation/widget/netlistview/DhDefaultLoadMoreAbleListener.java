package com.dh.foundation.widget.netlistview;

import com.dh.foundation.utils.DhHttpNetUtils;
import com.dh.foundation.utils.ToastUtils;
import com.dh.foundation.utils.bluetooth.bluetoothbean.BaseBean;
import com.google.gson.JsonObject;

import java.util.ArrayList;
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

        if (DhHttpNetUtils.isGetDataSuccessfully(listBaseBean) && listBaseBean.getReturnData() != null) {

            if (listBaseBean.getReturnData().size() == getPageSize()) {

                return true;
            }
        }

        return false;
    }

    @Override
    public List<?> getLoadedData(BaseBean<List<JsonObject>> returnObj) {

        if (DhHttpNetUtils.isGetDataSuccessfully(returnObj)) {

            return returnObj.getReturnData();

        } else {

            ToastUtils.toast(returnObj.getReturnMsg());

            return null;
        }

    }

    public int getPageSize() {
        return pageSize;
    }

    public DhDefaultLoadMoreAbleListener setPageSize(int pageSize) {

        this.pageSize = pageSize;

        return this;
    }
}
