package com.dh.foundation.volley.patch;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;

import static com.android.volley.toolbox.ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT;
import static com.android.volley.toolbox.ImageRequest.DEFAULT_IMAGE_MAX_RETRIES;
import static com.android.volley.toolbox.ImageRequest.DEFAULT_IMAGE_TIMEOUT_MS;

/**
 * 定制修改ImageLoader
 * Created By: Seal.Wu
 * Date: 2017/9/5
 * Time: 13:40
 */

public class DImageLoader extends ImageLoader {
    /**
     * Constructs a new ImageLoader.
     *
     * @param queue      The RequestQueue to use for making image requests.
     * @param imageCache The cache to use as an L1 cache.
     */
    public DImageLoader(RequestQueue queue, ImageCache imageCache) {
        super(queue, imageCache);
    }


    @Override
    protected Request<Bitmap> makeImageRequest(String requestUrl, int maxWidth, int maxHeight, ImageView.ScaleType scaleType, final String cacheKey) {
        final ImageRequest imageRequest = new ImageRequest(requestUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                onGetImageSuccess(cacheKey, response);
            }
        }, maxWidth, maxHeight, scaleType, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onGetImageError(cacheKey, error);
            }
        });
        imageRequest.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_IMAGE_TIMEOUT_MS * 10, DEFAULT_IMAGE_MAX_RETRIES,
                DEFAULT_IMAGE_BACKOFF_MULT));
        return imageRequest;
    }
}
