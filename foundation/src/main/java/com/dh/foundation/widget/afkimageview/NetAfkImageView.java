package com.dh.foundation.widget.afkimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.dh.foundation.utils.DLoggerUtils;
import com.dh.foundation.utils.ImageNetLoader;
import com.dh.foundation.utils.StringUtils;

/**
 * 带网络加载功能AfkImageView
 * Created By: Seal.Wu
 * Date: 2015/10/9
 * Time: 10:19
 */
public class NetAfkImageView extends AfkImageView {

    private ImageNetLoader imageNetLoader = ImageNetLoader.getDefault();

    /**
     * 图片请求地址
     */
    private String url;

    public NetAfkImageView(Context context) {
        super(context);
    }

    public NetAfkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImageUrl(final String url) {

        if (!StringUtils.equals(this.url, url) && StringUtils.isNotEmpty(this.url)) {

            imageNetLoader.cancel(this.url);

        }

        this.url = url;

        imageNetLoader.getBitmap(url, new ImageNetLoader.SimpleBitmapReceiver() {
            @Override
            public void onReceiveBitmap(Bitmap bitmap, boolean isImmediate) {

                if (NetAfkImageView.this.url.equals(url)) {

                    setTransitionAnimationEnable(!isImmediate);

                    setImageBitmap(bitmap);
                }

            }
        });
    }


    public void setImageUrl(final String url, final int errorImageResId, final int defaultImageResId) {

        if (!StringUtils.equals(this.url, url) && StringUtils.isNotEmpty(this.url)) {

            imageNetLoader.cancel(this.url);

        }

        this.url = url;

        setTransitionAnimationEnable(false);

        setImageResource(defaultImageResId);

        imageNetLoader.getBitmap(url, new ImageNetLoader.BitmapReceiver() {

            @Override
            public void onReceiveBitmap(Bitmap bitmap, boolean isImmediate) {

                if (NetAfkImageView.this.url.equals(url)) {

                    setTransitionAnimationEnable(!isImmediate);

                    setImageBitmap(bitmap);
                }

            }

            @Override
            public void onError(Throwable error) {

                if (NetAfkImageView.this.url.equals(url)) {

                    setTransitionAnimationEnable(false);

                    setImageResource(errorImageResId);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {

        if (url != null) {

            imageNetLoader.cancel(url);

        }
        super.onDetachedFromWindow();
    }
}
