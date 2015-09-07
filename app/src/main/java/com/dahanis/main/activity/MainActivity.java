package com.dahanis.main.activity;

import android.app.Activity;
import android.os.Bundle;

import com.dahanis.main.R;
import com.dh.foundation.utils.AESEncryptUtil;
import com.dh.foundation.utils.DLoggerUtils;

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

        AESEncryptUtil instance = AESEncryptUtil.getInstance();
        String encrypt = instance.encrypt(clearText);
        DLoggerUtils.i(encrypt);
        DLoggerUtils.i(instance.encrypt("hello,my babyhello,my babyhello,my babyhello,my babyhello,my babyhello,my baby"));
        DLoggerUtils.i(instance.decrypt(encrypt));
        setContentView(R.layout.main_activity);

    }
}
