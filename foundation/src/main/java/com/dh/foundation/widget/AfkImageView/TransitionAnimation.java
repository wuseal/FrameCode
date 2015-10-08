package com.dh.foundation.widget.afkimageview;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

/**
 * Created by dahan on 2015/9/21.
 */
public interface TransitionAnimation {

    boolean draw(Canvas canvas);

    void setImage(Drawable drawable);

    /**
     * 矫正微调速度
     * @param progress
     */
    void changeProgress(float progress);

    /**
     * 持续时间
     * @return
     */
    int duration();

    void setDuration(int duration);
}
