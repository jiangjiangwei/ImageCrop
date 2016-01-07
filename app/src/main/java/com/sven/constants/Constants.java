package com.sven.constants;

import android.os.Environment;

/**
 * Created by sven on 2016/1/6.
 * 常量类
 */
public class Constants {
    public static final String EXTERNAL_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory().getPath();
    /**
     * 裁剪图片地址,图片在Intent之间传输有大小限制
     */
    public static final String FILENAME = EXTERNAL_STORAGE_DIRECTORY + "/crop.png";
    /**拍照的图片路径*/
    public static final String PHOTONAME = EXTERNAL_STORAGE_DIRECTORY+ "/aa.png";

    public static final int TAKE_PHOTO = 1;
    public static final int PICK_PHOTO = 2;
    public static final int CROP_BEAUTY = 3;
}
