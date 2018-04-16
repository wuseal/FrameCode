package com.dh.foundation.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.http.AndroidHttpClient;
import android.os.Build;
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
import com.dh.foundation.volley.patch.DImageLoader;
import com.dh.foundation.volley.patch.ImageDiskBasedCache;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ImageView图片请求加载工具类
 * Created By: Seal.Wu
 * Date: 2015/4/14
 * Time: 13:58
 */
public class ImageNetLoader {

    private static final String LOG_TAG = "ImageNetLoader";
    private final static String DEFAULT_CACHE_DIR = "volley/image";

    private final static String NULL = "null";

    private final static BitmapCache imageCache = new BitmapCache();

    private final RequestQueue imageRequestQueue = newImageRequestQueue(FoundationManager.getContext(), null);
    private final ImageLoader imageLoader = new DImageLoader(imageRequestQueue, imageCache);

    private Map<String, WeakReference<ImageLoader.ImageContainer>> imageContainerMap = new HashMap<>();

    private Map<String, WeakReference<BitmapReceiverHolder>> holderMap = new HashMap<>();

    /**
     * 用于存储加载过的ImageView
     */
    private HashMap<ImageViewInfoHolder, WeakReference<ImageView>> imageViews = new HashMap<>();

    /**
     * 当前图片加载器是否有效，能使用，默认有用
     */
    private boolean enable = true;

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

    void setEnable(boolean enable) {

        this.enable = enable;
    }

    boolean isEnable() {

        return enable;
    }

    void resumeAllImageViews() {

        setEnable(true);

        List<ImageViewInfoHolder> tobeRemove = new ArrayList<>();

        for (Map.Entry<ImageViewInfoHolder, WeakReference<ImageView>> entry : imageViews.entrySet()) {


            final ImageView imageView = entry.getValue().get();

            final ImageViewInfoHolder key = entry.getKey();
            if (imageView != null) {

                loadImage(imageView, key.url, key.errorImageResId, key.defaultImageResId, key.maxWidth, key.maxHeight);

            } else {

                tobeRemove.add(key);
            }
        }

        for (ImageViewInfoHolder imageViewInfoHolder : tobeRemove) {

            imageViews.remove(imageViewInfoHolder);
        }
    }

    public void getBitmap(String url, final BitmapReceiver bitmapReceiver, int maxWidth, int maxHigh) {

        if (url == null) {

            url = NULL;
        }

        DLoggerUtils.i(LOG_TAG, "ImageNetLoader url =========> " + url);

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

                DLoggerUtils.e(LOG_TAG, error);
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
    public void loadImage(final ImageView imageView, String url, final int errorImageResId, final int defaultImageResId, int maxWidth, int maxHigh) {

        if (url == null) {

            url = NULL;
        }

        imageView.setTag(url);

        if (defaultImageResId != 0) {

            imageView.setImageResource(defaultImageResId);
        }

        final ImageViewInfoHolder imageViewInfoHolder = getImageViewInfoHolder(imageView);

        if (imageViewInfoHolder == null) {
            /**
             * 当前ImageView没有持有info信息的时候
             */
            imageViews.put(new ImageViewInfoHolder(url, defaultImageResId, errorImageResId, maxWidth, maxHigh), new WeakReference<ImageView>(imageView));

        } else {
            /**
             * 替换info信息
             */
            imageViewInfoHolder.url = url;
            imageViewInfoHolder.defaultImageResId = defaultImageResId;
            imageViewInfoHolder.errorImageResId = errorImageResId;
            imageViewInfoHolder.maxWidth = maxWidth;
            imageViewInfoHolder.maxHeight = maxHigh;
        }

        /**
         * 不可用的时候直接pass掉
         */
        if (!enable) {

            return;
        }

        final String finalUrl = url;

        final WeakReference<ImageView> imageWeakReference = new WeakReference<ImageView>(imageView);

        getBitmap(url, new BitmapReceiver() {
            @Override
            public void onReceiveBitmap(Bitmap bitmap, boolean isImmediate) {

                final ImageView imageView = imageWeakReference.get();

                if (imageView != null) {

                    if (imageView.getTag().equals(finalUrl)) {

                        imageView.setImageBitmap(bitmap);
                    }
                }
            }

            @Override
            public void onError(Throwable error) {

                final ImageView imageView = imageWeakReference.get();

                if (imageView != null) {
                    if (errorImageResId != 0 && imageView.getTag().equals(finalUrl)) {

                        imageView.setImageResource(errorImageResId);
                    }
                }
            }
        }, maxWidth, maxHigh);
    }


    private ImageViewInfoHolder getImageViewInfoHolder(ImageView imageView) {

        for (Map.Entry<ImageViewInfoHolder, WeakReference<ImageView>> entry : imageViews.entrySet()) {

            if (imageView.equals(entry.getValue().get())) {

                return entry.getKey();
            }
        }

        return null;
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

        imageViews.clear();

    }

    /**
     * 结束指定Image的请求
     *
     * @param url 请求的image的url
     */
    public void cancel(String url) {

        if (url == null) {

            url = NULL;
        }

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


    /**
     * 销毁当前图片加载器
     */
    public void destroy() {

        imageRequestQueue.stop();
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

    /**
     * 待加载图像的ImageView的加载内容信息
     */
    static class ImageViewInfoHolder {

        /**
         * 图像网络地址
         */
        String url;

        /**
         * 默认显示图片资源id
         */
        int defaultImageResId;

        /**
         * 加载失败显示图片资源id
         */
        int errorImageResId;

        /**
         * 设置显示的最大宽度
         */
        int maxWidth;

        /**
         * 设置显示的最大高度
         */
        int maxHeight;

        public ImageViewInfoHolder(String url, int defaultImageResId, int errorImageResId, int maxWidth, int maxHeight) {
            this.url = url;
            this.defaultImageResId = defaultImageResId;
            this.errorImageResId = errorImageResId;
            this.maxWidth = maxWidth;
            this.maxHeight = maxHeight;
        }
    }
}
