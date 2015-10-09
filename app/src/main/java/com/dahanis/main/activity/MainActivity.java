package com.dahanis.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dahanis.main.R;
import com.dh.foundation.utils.AESEncryptUtil;
import com.dh.foundation.utils.DLoggerUtils;
import com.dh.foundation.utils.IntentInvokeUtils;

import butterknife.OnClick;

/**
 * Created By: Seal.Wu
 * Date: 2015/4/1
 * Time: 14:23
 */
public class MainActivity extends Activity {

    private String clearText = "111111";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AESEncryptUtil instance = AESEncryptUtil.getInstance();
        String encrypt = instance.encrypt(clearText);
        DLoggerUtils.i(encrypt);
        DLoggerUtils.i(instance.encrypt("hello,my babyhello,my babyhello,my babyhello,my babyhello,my babyhello,my baby"));
        DLoggerUtils.i(instance.decrypt(encrypt));


    }

    public void netListViewDemo(View view) {

        IntentInvokeUtils.invokeActivity(this, NetListViewActivity.class);

    }

    public void imageNetLoaderDemo(View view) {

        IntentInvokeUtils.invokeActivity(this, LoadImageActivity.class);

    }
    public void dhNetListViewDemo(View view) {

        IntentInvokeUtils.invokeActivity(this, DhNetListViewActivity.class);
    }

    public void toAfkImageView(View view) {

        IntentInvokeUtils.invokeActivity(this, LoadImageAfkImageViewActivity.class);
    }
    public void toNetAfkImageView(View view) {

        IntentInvokeUtils.invokeActivity(this, LoadNetAfkImageViewActivity.class);
    }
}
