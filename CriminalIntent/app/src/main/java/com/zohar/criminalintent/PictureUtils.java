package com.zohar.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class PictureUtils {

    public static Bitmap getBitmapScale(String path, Activity activity){

        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getBitmapScale(path, size.x, size.y);
    }

    public static Bitmap getBitmapScale(String path, int destWidth, int destHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // If set to true, the decoder will return null (no bitmap), but the out... fields will still be set, allowing the caller to query the bitmap without having to allocate the memory for its pixels.
        BitmapFactory.decodeFile(path);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        // 图片缩放的大小比例
        int sampleSize = 1;
        if (srcWidth > destWidth || srcHeight > destHeight){
            float widthScale = srcWidth / destWidth;
            float heightScale = srcHeight / destHeight;

            // 重置缩放比例
            sampleSize = Math.round(heightScale > widthScale ? heightScale : widthScale);
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        // 读取并且创建最终的Bitmap
        return BitmapFactory.decodeFile(path,options);

    }
}
