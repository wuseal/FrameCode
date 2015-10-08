package com.dh.foundation.utils.filemanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 图片处理
 * @author ZhangYi 2014-4-1 上午10:44:02
 */
public class ImgUtil {

    private static ImgUtil mImgUtil;

    public static ImgUtil getInstance() {
        if (null == mImgUtil) {
            mImgUtil = new ImgUtil();
        }
        return mImgUtil;
    }

    /**
     * 生成图片
     * @param bitmap
     * @param
     *
     * @return 缩放后新生成的图片
     */
    public static Bitmap zoom(Bitmap bitmap, float zf) {
        Matrix matrix = new Matrix();
        matrix.postScale(zf, zf);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.abs(x1 - x2) * Math.abs(x1 - x2)
                + Math.abs(y1 - y2) * Math.abs(y1 - y2));
    }

    public static double pointTotoDegrees(double x, double y) {
        return Math.toDegrees(Math.atan2(x, y));
    }

    public static boolean checkInRound(float sx, float sy, float r, float x,
                                       float y) {
        return Math.sqrt((sx - x) * (sx - x) + (sy - y) * (sy - y)) < r;
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                : Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }
//    获得带倒影的图片
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
                h / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
                Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }
    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        // drawable转换成bitmap
        Bitmap oldbmp = drawableToBitmap(drawable);
        // 创建操作图片用的Matrix对象
        Matrix matrix = new Matrix();
        // 计算缩放比例
        float sx = ((float) w / width);
        float sy = ((float) h / height);
        // 设置缩放比例
        matrix.postScale(sx, sy);
        // 建立新的bitmap，其内容是对原bitmap的缩放后的图
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(newbmp);
    }
    public static String savaBitmap2File(Context mContext, Bitmap bitmap, String img_name, int SCREEN_WIDHT)
            throws Exception {
        String imgDirPath = "";
        if (FilePathUtil.isExistExtPath()) {
            imgDirPath = FilePathUtil.getExtPath() + FilePathUtil.getImageDirectoryPath(mContext);
        } else {
            imgDirPath = FilePathUtil.getSystemPath(mContext);
        }
        File file = new File(imgDirPath);
        if (!file.exists()) {
            file.mkdir();
        }
        File imageFile = new File(file, img_name);
        imageFile.createNewFile();

        FileOutputStream fos = new FileOutputStream(imageFile);
        /* 尺寸修改 */
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width > SCREEN_WIDHT) {
            /* 图片尺寸大于当前屏幕宽度 */
            int newHeight = SCREEN_WIDHT * height / width;
            bitmap = Bitmap.createScaledBitmap(bitmap,
                    SCREEN_WIDHT, newHeight, false);
        }
        /* 质量修改 保存 */
        bitmap.compress(CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
        return imageFile.getPath();
    }

    /**
     * 从图片的文件地址获取bitmap对象
     * @param pathName
     * @param inSampleSize (缩小倍数 )
     *
     * @return
     */
    public Bitmap getScaleBitmapFromFile(String pathName, int inSampleSize) {
        if (inSampleSize > 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;
            options.inSampleSize = inSampleSize;
            return BitmapFactory.decodeFile(pathName, options);
        }
        return BitmapFactory.decodeFile(pathName);
    }

    /**
     * 尺寸比例 压缩图片
     * @param bm
     * @param newWidth 新的宽度,根据此宽度计算高度
     *
     * @return
     */
    public Bitmap scaleImg(Bitmap bm, int newWidth) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        int newHeight = newWidth * height / width;
        return Bitmap.createScaledBitmap(bm, newWidth, newHeight, false);
    }

    /**
     * 设置圆角的图片
     * @param bitmap 图片
     * @param pixels 角度
     */
    public Bitmap toRoundCornerBitmap(Bitmap bitmap, int pixels) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            int color = 0XFFFFFFFF;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            RectF rectF = new RectF(rect);
            float roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * 将图片转换为圆形的
     * @param bitmap
     *
     * @return
     */
    public Bitmap toRoundBitmap(Bitmap bitmap) {
        try {
            bitmap = cutSquareBitmap(bitmap);
            return toRoundCornerBitmap(bitmap, bitmap.getWidth() / 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 把图片切成正方形的
     * @param bitmap
     *
     * @return
     */
    public Bitmap cutSquareBitmap(Bitmap bitmap) {
        try {
            Bitmap result;
            int w = bitmap.getWidth();// 输入长方形宽
            int h = bitmap.getHeight();// 输入长方形高
            int nw;// 输出正方形宽
            if (w > h) { // 宽大于高
                nw = h;
                result = Bitmap.createBitmap(bitmap, (w - nw) / 2, 0, nw, nw);
            } else {// 高大于宽
                nw = w;
                result = Bitmap.createBitmap(bitmap, 0, (h - nw) / 2, nw, nw);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * 图片反转
     * @param img
     *
     * @return
     */
    public Bitmap toturn(Bitmap img, int Rotate) {
        Matrix matrix = new Matrix();
        matrix.postRotate(Rotate); /* 翻转180度 */
        int width = img.getWidth();
        int height = img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        return img;
    }
}
