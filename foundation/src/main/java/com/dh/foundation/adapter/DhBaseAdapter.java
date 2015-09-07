package com.dh.foundation.adapter;

import android.widget.BaseAdapter;

import java.util.List;

/**
 * 列表类基础adapter
 * Created By: Seal.Wu
 * Date: 2015/4/17
 * Time: 17:13
 */
public abstract class DhBaseAdapter<E> extends BaseAdapter {
    private List<E> list;

    @Override
    public int getCount() {
        if (list == null) {
            throw new ExceptionInInitializerError(this.getClass().getName() + "has not invoke void setList(),then this filed list is null");
        }
        return list.size();
    }

    @Override
    public E getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    public List<E> getList() {
        return list;
    }
}
