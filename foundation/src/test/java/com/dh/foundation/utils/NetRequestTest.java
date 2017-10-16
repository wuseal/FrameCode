package com.dh.foundation.utils;
import com.android.volley.NetworkError;
import com.android.volley.VolleyError;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created By: Seal.Wu
 * Date: 2017/10/16
 * Time: 14:25
 */
public class NetRequestTest {

    @Test
    public void volleyErrorDefaultErrorMessageNull() {
        VolleyError volleyError1 = new VolleyError(new RuntimeException("hahaha"));
        VolleyError volleyError2 = new VolleyError(new IOException());
        VolleyError volleyError3 = new NetworkError();


        assertThat(volleyError1.getMessage(), CoreMatchers.notNullValue());
        assertThat(volleyError2.getMessage(), CoreMatchers.notNullValue());
        assertThat(volleyError3.getMessage(), CoreMatchers.nullValue());

    }

}