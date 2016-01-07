package com.sven.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sven on 2016/1/6.
 * 文件操作工具类
 */
public class FileUtis {
    /**
     * 将图片对象存储为文件
     *
     * @param bitmap
     * @param filePath
     */
    public static void saveBitmap2File(Bitmap bitmap, String filePath) {
        File file = new File(filePath);
        FileOutputStream fos = null;
        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 缩放图片
     *
     * @param bm
     * @param width
     * @param height
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap bm, int width, int height) {
        int bmWidth = bm.getWidth();
        int bmHeight = bm.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale((float) width / bmWidth, (float) height / bmHeight);
        return Bitmap.createBitmap(bm, 0, 0, bmWidth, bmHeight, matrix, true);
    }
}

