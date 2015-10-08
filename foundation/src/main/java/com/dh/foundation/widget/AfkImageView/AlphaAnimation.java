package com.dh.foundation.widget.afkimageview;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

/**
 * Created by lee on 2015/9/22.
 */
public class AlphaAnimation extends ToggleAnimation {
    private int lastAlpha;
    private int alpha;

    @Override
    public void setImage(Drawable drawable) {
        super.setImage(drawable);

        lastAlpha = 0;
        alpha = 255;
    }

    @Override
    protected void drawLastDrawable(Canvas canvas) {
        lastAlpha = (int)(255 * progress);
        mLastDrawable.setAlpha(lastAlpha);
        mLastDrawable.draw(canvas);
    }

    @Override
    protected void drawDrawable(Canvas canvas) {
        alpha = (int)(255 * (1 - progress));
        mDrawable.setAlpha(alpha);
        mDrawable.draw(canvas);
    }

    @Override
    protected void finish(Canvas canvas) {
        mDrawable.draw(canvas);
    }
}
