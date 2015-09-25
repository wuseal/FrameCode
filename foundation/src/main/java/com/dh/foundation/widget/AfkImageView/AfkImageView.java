package com.dh.foundation.widget.AfkImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Lee on 2015/9/21.
 */
public class AfkImageView extends View {

    public enum AnimType {
        ALPHA_ANIM,
    }

    /**
     * 记录切换动画类型
     */
    private AnimType mAnimType;


    /**
     * FPS
     */
    private final int FPS = 50;

    /**
     * 每帧刷新间隔
     */
    private final int REFRESH_INTERVAL = 1000 / FPS;

    /**
     * 当 mBitmap 被赋值时，记录该时间， 暂时作为动画启动时间
     * 由于当前没有设置延迟等功能，所以mBitmap被赋值时，将直接有过度动画效果
     */
    private long mAnimationStartTime;

    private long mAnimationNowTime;

    private long mAnimationRemainingTime;

    private Paint mPaint;

    private ExcessiveAnimation mExcessiveAnimation;
    private Drawable mLastDrawable;
    private Drawable mDrawable;

    /**
     * 用来记录动画是否结束
     */
    private boolean mAnimationFinish;
    /**
     * 存放图片 最原始的  宽高
     */
    private int mOldWidth, mOldHeight;

    /**
     * 初始图片 宽高
     */
    private int mFirstImageWidth, mFirstImageHeight;

    /**
     * 图片的 最终实际显示的宽高
     */
    private int mImageWidth, mImageHeight;

    /**
     * 控件的 最终宽高
     */
    private int mWidth, mHeight;


    public AfkImageView(Context context) {
        this(context, null);
    }

    public AfkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                invalidate();
            }
            return false;
        }
    });
    //=====================================================================
    //-----------------------     override     ----------------------------
    //_____________________________________________________________________


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawable == null)
            return;

        if (mImageWidth == 0 || mImageHeight == 0) {
            return;
        }

        if (mLastDrawable == null || mExcessiveAnimation == null) {
            drawWithOutExcessiveAnimation(canvas);
        } else {
            drawWithAnimation(canvas);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        int ewidth = MeasureSpec.getSize(widthMeasureSpec);
        int eheight = MeasureSpec.getSize(heightMeasureSpec);

        width = resolveAdjustedSize(mFirstImageWidth,ewidth,widthMeasureSpec);
        height = resolveAdjustedSize(mFirstImageHeight,eheight,heightMeasureSpec);

        mWidth = width;
        mHeight = height;

        mImageWidth = mFirstImageWidth;
        mImageHeight = mFirstImageHeight;

        setDrawableSize(mDrawable);
        setDrawableSize(mLastDrawable);
        setMeasuredDimension(width, height);
    }

    //=====================================================================
    //-----------------------     private     -----------------------------
    //_____________________________________________________________________
    private void init() {
        mPaint = new Paint();
        mAnimType = AnimType.ALPHA_ANIM;

        mFirstImageHeight = -1;
        mFirstImageWidth = -1;

        setExcessiveAnimation(mAnimType);
    }

    private void drawWithOutExcessiveAnimation(Canvas canvas) {
        mDrawable.draw(canvas);
    }

    private void setDrawable(Drawable drawable) {
        if (mExcessiveAnimation == null) {
            throw new NullPointerException("you must setExcessiveAnimation() before setImage(),and this can't be null");
        }

        if (drawable == null)
            return;

        if (mDrawable != null)
            mLastDrawable = mDrawable;

        mDrawable = drawable;

        mOldWidth = mDrawable.getIntrinsicWidth();
        mOldHeight = mDrawable.getIntrinsicHeight();

        if (mFirstImageWidth == -1 || mFirstImageWidth == -1) {
            if (mOldWidth != 0 && mOldHeight != 0) {
                mFirstImageWidth = mOldWidth;
                mFirstImageHeight = mOldHeight;
            }
        }

//        setDrawableSize(mDrawable);
        setDrawableSize(mDrawable);
        setDrawableSize(mLastDrawable);

        mAnimationStartTime = System.currentTimeMillis();

//        if(mLastDrawable != null){
//            setDrawableSize(mLastDrawable);
//        }

        if (mExcessiveAnimation != null) {
            mExcessiveAnimation.setImage(mDrawable);
        }

        invalidate();
    }

    private void setExcessiveAnimation(AnimType type) {
        switch (type) {
            case ALPHA_ANIM:
                setExcessiveAnimation(new AlphaAnimation());
                break;
        }
    }

    private void setDrawableSize(Drawable drawable) {
        if (drawable == null)
            return;
        drawable.setBounds(0, 0, mImageWidth, mImageHeight);
    }

    private int resolveAdjustedSize(int imageSize, int layoutSize, int measureSpec) {
        int result = layoutSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT)
                    result = imageSize;
                else if (getLayoutParams().height == ViewGroup.LayoutParams.MATCH_PARENT)
                    result = layoutSize;
                break;
            case MeasureSpec.AT_MOST:
                result = layoutSize;
                break;
            case MeasureSpec.EXACTLY:
                result = imageSize;
                break;
        }
        return result;
    }

    private void drawWithAnimation(Canvas canvas) {
        countRemainingTime();
        double progress;
//        if(mAnimationRemainingTime <= 0)
//            progress = 1;
//        else{
//            double duration = mExcessiveAnimation.duration();
//            double retime = mAnimationRemainingTime;
//            progress = 1- retime / duration;
//        }
//        if(progress < 0)
//            progress = 0;
        double duration = mExcessiveAnimation.duration();
        double retime = mAnimationRemainingTime;
        progress = retime / duration;
        if (progress > 1)
            progress = 1;
        else if (progress < 0)
            progress = 0;
        mExcessiveAnimation.changeProgress(progress);
        mAnimationFinish = mExcessiveAnimation.draw(canvas);
        if (mAnimationFinish) {
            //animation is finish
        } else {
            delayedRefresh(REFRESH_INTERVAL);
        }
    }

    /**
     * 计算下一帧延迟刷新时间
     */
    private void countRemainingTime() {
        mAnimationNowTime = System.currentTimeMillis();
        mAnimationRemainingTime = mExcessiveAnimation.duration() - (mAnimationNowTime - mAnimationStartTime);
    }

    /**
     * 间隔一段时间，去执行刷新下一帧
     *
     * @param delayedTime
     */
    private void delayedRefresh(int delayedTime) {
        mHandler.sendEmptyMessageDelayed(0, delayedTime);
    }
    //=====================================================================
    //-----------------------     public     -----------------------------
    //_____________________________________________________________________

    /**
     * 与ImageView的功能一样
     */
//    public void setScaleType() {
//
//    }
    public void setImage(int res) {
        Drawable drawable = getResources().getDrawable(res);

        setDrawable(drawable);
    }

    public void setImage(Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);

        setDrawable(drawable);
    }

    public void setImage(Drawable drawable) {
        setDrawable(drawable);
    }

    public void setExcessiveAnimation(ExcessiveAnimation animation) {
        this.mExcessiveAnimation = animation;
    }

    public void setAnimType(AfkImageView.AnimType type) {
        this.mAnimType = type;

        setExcessiveAnimation(mAnimType);
    }
}
