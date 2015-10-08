package com.dh.foundation.widget.afkimageview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by Lee on 2015/9/28.
 */
public class CenterExpandAnimation extends ToggleAnimation{
    private Paint mPaint;

    private Bitmap bitmapDis;
    private Bitmap bitmapSrc;
    private PorterDuffXfermode porterDuffXfermode;

    private Canvas disCanvas;

    private int width,height;
    private int w,h;

    public CenterExpandAnimation() {
        init();
    }

    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void setImage(Drawable drawable) {
        super.setImage(drawable);
        if(drawable != null){
            width = drawable.getIntrinsicWidth();
            height = drawable.getIntrinsicHeight();

            bitmapSrc = ((BitmapDrawable)drawable).getBitmap();

            bitmapDis = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            disCanvas = new Canvas(bitmapDis);

//            disCanvas.drawColor(Color.TRANSPARENT);
//            disCanvas.drawCircle(width/2,height/2,100,mPaint);
        }
    }

    @Override
    protected void drawLastDrawable(Canvas canvas) {

    }

    @Override
    protected void drawDrawable(Canvas canvas) {
        zoom(canvas,mDrawable);
    }

    @Override
    protected void finish(Canvas canvas) {

    }

    private void zoom(Canvas canvas,Drawable drawable){
        w = (int)((1-progress) * width);
        h = (int)((1-progress) * height);

//        drawable.setAlpha(255);
//        disCanvas.drawColor(Color.TRANSPARENT);
        disCanvas.drawCircle(width / 2, height / 2, width * (1 - progress), mPaint);

        int sc = canvas.saveLayer(0,0,width,height,null,Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(bitmapDis,0,0,mPaint);
        mPaint.setXfermode(porterDuffXfermode);
        canvas.drawBitmap(bitmapSrc, 0, 0, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(sc);
    }

}
