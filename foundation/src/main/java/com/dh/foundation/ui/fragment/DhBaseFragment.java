package com.dh.foundation.ui.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.dh.foundation.utils.ProgressDialogUtil;
import com.dh.foundation.utils.ToastUtils;

/**
 * 基础fragment
 * Created By: Seal.Wu
 * Date: 2015/7/31
 * Time: 14:11
 */
public class DhBaseFragment extends Fragment {

    protected View findViewById(int id) {
        ViewGroup group = (ViewGroup) getView();
        if (group != null) {
            return group.findViewById(id);
        }
        return null;
    }

    protected void toast(int resId) {
        ToastUtils.toast(getActivity(), resId);
    }

    protected void toast(String message) {
        ToastUtils.toast(getActivity(), message);
    }

    protected void showProgressDialog() {
        ProgressDialogUtil.showProgressDialog(getActivity());
    }

    protected void dismissProgressDialog() {
        ProgressDialogUtil.dismissProgressDialog();
    }


    protected boolean setViewVisibility(Object view, int visible) {
        if (view instanceof View) {
            ((View) view).setVisibility(visible);
            return true;
        }
        return false;
    }
}
