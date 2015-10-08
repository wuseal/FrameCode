package com.dh.foundation.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.dh.foundation.manager.FoundationManager;
import com.dh.foundation.volley.patch.ImageDiskBasedCache;

import java.io.File;

/**
 * ImageView图片请求加载工具类
 * Created By: Seal.Wu
 * Date: 2015/4/14
 * Time: 13:58
 */
public class ImageNetLoader {

    private static final String DEFAULT_CACHE_DIR = "volley/image";

    private final static BitmapCache imageCache = new BitmapCache();

    private final static ImageLoader imageLoader = new ImageLoader(newImageRequestQueue(FoundationManager.getContext(), null), imageCache);


    public static ImageLoader getImageLoader() {
        return imageLoader;
    }

    public static BitmapCache getImageCache() {
        return imageCache;
    }

    public static void getBitmap(final String url, final BitmapReceiver bitmapReceiver, int maxWidth, int maxHigh) {

        final BitmapHolder bitmapHolder = new BitmapHolder();

        getImageLoader().get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {

                if (bitmapReceiver != null && response.getBitmap() != null) {

                    bitmapReceiver.onReceiveBitmap(response.getBitmap(), isImmediate);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (bitmapReceiver != null) {

                    bitmapReceiver.onError(error);
                }

                DLoggerUtils.e(error);
            }
        }, maxWidth, maxHigh);

    }

    public static void getBitmap(final String url, final BitmapReceiver bitmapReceiver) {

        getBitmap(url, bitmapReceiver, 0, 0);
    }

    public static void loadImage(final ImageView imageView, final String url) {

        loadImage(imageView, url, 0, 0);
    }

    public static void loadImage(final ImageView imageView, final String url, final int errorImageResId, final int defaultImageResId) {

        loadImage(imageView, url, errorImageResId, defaultImageResId, 0, 0);

    }

    /**
     * 装载图片到视图控件里
     *
     * @param imageView         视图控件
     * @param url               图片网络链接地址
     * @param errorImageResId   错误图片id
     * @param defaultImageResId 默认图片id
     * @param maxWidth          设置显示的最大宽度
     * @param maxHigh           设置显示的最大高度
     */
    public static void loadImage(final ImageView imageView, final String url, final int errorImageResId, final int defaultImageResId, int maxWidth, int maxHigh) {

        imageView.setTag(url);

        getImageLoader().get(url, new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (errorImageResId != 0 && imageView.getTag().equals(url)) {

                    imageView.setImageResource(errorImageResId);
                }

                DLoggerUtils.e(error);
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {

                if (response.getBitmap() != null && imageView.getTag().equals(url)) {

                    imageView.setImageBitmap(response.getBitmap());

                    if (!isImmediate) {

                        AlphaAnimation animation = new AlphaAnimation(0, 1);

                        animation.setDuration(300);

                        imageView.startAnimation(animation);
                    }

                } else if (defaultImageResId != 0 && imageView.getTag().equals(url)) {

                    imageView.setImageResource(defaultImageResId);
                }
            }
        }, maxWidth, maxHigh);
    }

    public static RequestQueue newImageRequestQueue(Context context, HttpStack stack) {

        File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);

        String userAgent = "volley/0";
        try {
            String packageName = context.getPackageName();

            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);

            userAgent = packageName + "/" + info.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
        }

        if (stack == null) {

            if (Build.VERSION.SDK_INT >= 9) {

                stack = new HurlStack();

            } else {
                // Prior to Gingerbread, HttpUrlConnection was unreliable.
                // See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
                stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
            }
        }

        Network network = new BasicNetwork(stack);

        RequestQueue queue = new RequestQueue(new ImageDiskBasedCache(cacheDir), network);

        queue.start();

        return queue;
    }

    private static class BitmapHolder {

        Bitmap bitmap;
    }

    /**
     * Bitmap接收者
     */
    public interface BitmapReceiver {

        /**
         * @param bitmap      接收到的位图
         * @param isImmediate 是否是在内存中立即返回
         */
        void onReceiveBitmap(Bitmap bitmap, boolean isImmediate);

        /**
         * 接收位图失败
         *
         * @param error 失败原因
         */
        void onError(Throwable error);
    }

    public static class SimpleBitmapReceiver implements BitmapReceiver {

        @Override
        public void onReceiveBitmap(Bitmap bitmap, boolean isImmediate) {

        }

        @Override
        public void onError(Throwable error) {

        }
    }
}
