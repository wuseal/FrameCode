package com.dahanis.main;

import com.dh.foundation.app.ApplicationUtil;
import com.dh.foundation.app.FoundationApplication;
import com.dh.foundation.utils.DLoggerUtils;
import com.dh.foundation.utils.ToastUtils;

/**
 * Created By: Seal.Wu
 * Date: 2015/9/7
 * Time: 19:11
 */
public class MyApplication extends FoundationApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        String a_a = ApplicationUtil.getApplicationInfoMetaData("A_A");
        DLoggerUtils.i("metadata==============" + (a_a ==null?"null":a_a));
        DLoggerUtils.i("metadata==============" );
        ToastUtils.toastLongTime(this, "metadata==================" + ApplicationUtil.getApplicationInfoMetaData("A_A"));

    }
}
