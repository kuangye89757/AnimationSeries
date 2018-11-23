package com.jikexueyuan.animationseries;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * UI工具类
 */
public final class UIUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        final float scale = SportsApp.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(float pxValue) {
        final float scale = SportsApp.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     */
    public static int px2sp(float pxValue) {
        float fontScale = SportsApp.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px
     */
    public static int sp2px(float spValue) {
        float fontScale = SportsApp.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取资源
     */
    public static Resources getResources() {
        return SportsApp.getContext().getResources();
    }

    /**
     * 返回String数组
     */
    public static String[] getStringArray(@ArrayRes int id) {
        return getResources().getStringArray(id);
    }

    /**
     * 获取字符串资源
     */
    public static String getString(@StringRes int id) {
        return getResources().getString(id);
    }

    /**
     * 获取颜色资源
     */
    public static int getColor(@ColorRes int id) {
        return getResources().getColor(id);
    }


    /**
     * 获取drawable资源
     */
    public static Drawable getDrawable(@DrawableRes int id) {
        return getResources().getDrawable(id);
    }

    /**
     * 获取Bitmap资源
     */
    public static Bitmap getBitmap(@DrawableRes int id) {
        BitmapDrawable bd = (BitmapDrawable) getDrawable(id);
        return bd.getBitmap();
    }

    /**
     * 获取view的宽度
     */
    public static int getMeasuredWidth(View view) {
        int width = 0;
        if (view != null) {
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(w, h);
            width = view.getMeasuredWidth();
        }
        return width;
    }

    /**
     * 获取view的高度
     */
    public static int getMeasuredHeight(View view) {
        int height = 0;
        if (view != null) {
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(w, h);
            height = view.getMeasuredHeight();
        }
        return height;
    }

    /**
     * 获取布局文件
     */
    public static View inflate(@LayoutRes int id, ViewGroup group, boolean flag) {
        return LayoutInflater.from(SportsApp.getContext()).inflate(id, group, flag);
    }

    /**
     * 获取布局文件
     */
    public static View inflate(@LayoutRes int id) {
        return LayoutInflater.from(SportsApp.getContext()).inflate(id, null, false);
    }

   
    /**
     * Bitmap压缩策略
     */
    public static byte[] compressImageToByte2(Bitmap bmp, float size) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] result;
        try {
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
            int options = 100;
            while (output.toByteArray().length / 1024 >= size) {
                output.reset();
                bmp.compress(Bitmap.CompressFormat.JPEG, options, output);
                if (options == 1) {
                    break;
                }
                //每次减5
                options -= 5;
                if (options <= 0) {
                    options = 1;
                }
            }

            if (output.toByteArray().length / 1024 >= size) {
                //图片依然过大
                return null;
            }

            result = output.toByteArray();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 矩阵方式压缩尺寸
     */
    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        return resizedBitmap;
    }
}


