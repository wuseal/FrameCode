package com.dh.foundation.utils;

import android.app.Dialog;
import android.content.Context;

/**
 * 对话框生产能力接口
 * Created By: Seal.Wu
 * Date: 2015/8/17
 * Time: 11:10
 */
public interface DialogMakeAbility {
    /**
     * 生产出一个对话框
     *
     * @return 生成的对话框
     */
    Dialog makeDialog(Context context);
}
