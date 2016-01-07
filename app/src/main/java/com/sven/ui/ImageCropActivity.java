package com.sven.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.sven.R;
import com.sven.constants.Constants;
import com.sven.utils.FileUtis;
import com.sven.views.CropImageView;

/**
 * Created by Administrator on 2016/1/6.
 */
public class ImageCropActivity extends Activity {
    private Bitmap mBitmap;
    private String mPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            mPhotoPath = b.getString("PHOTO_PATH");
        }

        final CropImageView cropImageView = (CropImageView) findViewById(R.id.cropimageview);
        if (!TextUtils.isEmpty(mPhotoPath)) {
            try {
                mBitmap = BitmapFactory.decodeFile(mPhotoPath);
                Bitmap bm = FileUtis.scaleBitmap(mBitmap, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
                mBitmap.recycle();
                cropImageView.setImageBitmap(bm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            cropImageView.setImageResoure(R.drawable.menv2);
        }
        findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 保存图片到本地
                Bitmap bm = cropImageView.getCropImage();
                FileUtis.saveBitmap2File(bm, Constants.FILENAME);

                Intent intent = getIntent();
                // 将剪裁图片路径传递回去
                intent.putExtra("path", Constants.FILENAME);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
