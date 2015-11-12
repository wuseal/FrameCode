package com.dh.foundation.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
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
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * ImageView图片请求加载工具类
 * Created By: Seal.Wu
 * Date: 2015/4/14
 * Time: 13:58
 */
public class ImageNetLoader {

    private final static String DEFAULT_CACHE_DIR = "volley/image";

    private final static BitmapCache imageCache = new BitmapCache();

    private final ImageLoader imageLoader = new ImageLoader(newImageRequestQueue(FoundationManager.getContext(), null), imageCache);

    private Map<String,WeakReference<ImageLoader.ImageContainer>> imageContainerMap = new HashMap<>();

    private Map<String, WeakReference<BitmapReceiverHolder>> holderMap = new HashMap<>();


    private final static ImageNetLoader DEFAULT_INSTANCE = new ImageNetLoader();

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public static BitmapCache getImageCache() {
        return imageCache;
    }

    public static ImageNetLoader getDefault() {

        return DEFAULT_INSTANCE;
    }

    public void getBitmap(final String url, final BitmapReceiver bitmapReceiver, int maxWidth, int maxHigh) {

        final BitmapReceiverHolder bitmapReceiverHolder = new BitmapReceiverHolder(bitmapReceiver);

        final ImageLoader.ImageContainer imageContainer = getImageLoader().get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {

                if (bitmapReceiverHolder.getBitmapReceiver() != null && response.getBitmap() != null) {

                    bitmapReceiverHolder.getBitmapReceiver().onReceiveBitmap(response.getBitmap(), isImmediate);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

                if (bitmapReceiverHolder.getBitmapReceiver() != null) {

                    bitmapReceiverHolder.getBitmapReceiver().onError(error);
                }

                DLoggerUtils.e(error);
            }
        }, maxWidth, maxHigh);

        imageContainerMap.put(url, new WeakReference<ImageLoader.ImageContainer>(imageContainer));

        holderMap.put(url, new WeakReference<BitmapReceiverHolder>(bitmapReceiverHolder));

    }

    public void getBitmap(final String url, final BitmapReceiver bitmapReceiver) {

        getBitmap(url, bitmapReceiver, 0, 0);
    }

    public void loadImage(final ImageView imageView, final String url) {

        loadImage(imageView, url, 0, 0);
    }

    public void loadImage(final ImageView imageView, final String url, final int errorImageResId, final int defaultImageResId) {

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
    public void loadImage(final ImageView imageView, final String url, final int errorImageResId, final int defaultImageResId, int maxWidth, int maxHigh) {

        imageView.setTag(url);

        if (defaultImageResId != 0) {

            imageView.setImageResource(defaultImageResId);
        }

        getBitmap(url, new BitmapReceiver() {
            @Override
            public void onReceiveBitmap(Bitmap bitmap, boolean isImmediate) {

                if (imageView.getTag().equals(url)) {

                    imageView.setImageBitmap(bitmap);

                    if (!isImmediate) {

                        AlphaAnimation animation = new AlphaAnimation(0, 1);

                        animation.setDuration(300);

                        imageView.startAnimation(animation);
                    }
                }
            }

            @Override
            public void onError(Throwable error) {

                if (errorImageResId != 0 && imageView.getTag().equals(url)) {

                    imageView.setImageResource(errorImageResId);
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


    /**
     * 结束所有Image的请求
     */
    public void cancelAll() {

        for (Map.Entry<String, WeakReference<ImageLoader.ImageContainer>> entry : imageContainerMap.entrySet()) {

            final ImageLoader.ImageContainer imageContainer = entry.getValue().get();

            if (imageContainer != null) {

                imageContainer.cancelRequest();
            }

            final BitmapReceiverHolder bitmapReceiverHolder = holderMap.get(entry.getKey()).get();

            if (bitmapReceiverHolder != null) {

                bitmapReceiverHolder.setBitmapReceiver(null);
            }

        }

        imageContainerMap.clear();

        holderMap.clear();

    }

    /**
     * 结束指定Image的请求
     *
     * @param url 请求的image的url
     */
    public void cancel(String url) {

        for (Map.Entry<String, WeakReference<ImageLoader.ImageContainer>> entry : imageContainerMap.entrySet()) {

            if (StringUtils.equals(url, entry.getKey())) {

                final ImageLoader.ImageContainer imageContainer = entry.getValue().get();

                if (imageContainer != null) {

                    imageContainer.cancelRequest();
                }

                final BitmapReceiverHolder bitmapReceiverHolder = holderMap.get(entry.getKey()).get();

                if (bitmapReceiverHolder != null) {

                    bitmapReceiverHolder.setBitmapReceiver(null);
                }

                imageContainerMap.remove(entry.getKey());

                holderMap.remove(entry.getKey());

                break;
            }
        }
    }

    static final class BitmapReceiverHolder {

        private BitmapReceiver bitmapReceiver;

        public BitmapReceiverHolder(BitmapReceiver bitmapReceiver) {

            this.bitmapReceiver = bitmapReceiver;
        }

        public void setBitmapReceiver(BitmapReceiver bitmapReceiver) {

            this.bitmapReceiver = bitmapReceiver;
        }

        public BitmapReceiver getBitmapReceiver() {
            return bitmapReceiver;
        }
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
