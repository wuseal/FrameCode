package com.dh.foundation;

import android.support.test.runner.AndroidJUnit4;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.dh.foundation.utils.HttpNetUtils;
import com.dh.foundation.utils.NetRequest;
import com.dh.foundation.utils.RequestParams;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created By: Seal.Wu
 * Date: 2017/10/17
 * Time: 14:56
 */
@RunWith(AndroidJUnit4.class)
public class NetRequestTestFAndroid {



    @Test
    public void testErrorMessageCouldBeNull() {

        NetRequest<Object> netRequest = new NetRequest<Object>(Request.Method.GET,"",new RequestParams(),Object.class, new HttpNetUtils.RequestListener<Object>() {
            @Override
            public void onSuccess(Object o) {

            }

            @Override
            public void onFailed(Throwable throwable) {

            }

            @Override
            public void onFinished() {

            }
        });

        final NetworkError error = new NetworkError();
        MatcherAssert.assertThat(error.getMessage(), CoreMatchers.nullValue());
        netRequest.getErrorListener().onErrorResponse(error);
    }

}