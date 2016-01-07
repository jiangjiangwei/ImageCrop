package com.sven;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sven.constants.Constants;
import com.sven.ui.ImageCropActivity;

import java.io.File;


public class MainActivity extends Activity {
    private Button btnTakePhoto, btnPickPhoto, btnCropBeauty;
    private ImageView ivCropImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initView() {
        btnTakePhoto = (Button) findViewById(R.id.btn_takeCamera);
        btnPickPhoto = (Button) findViewById(R.id.btn_pickPhotos);
        btnCropBeauty = (Button) findViewById(R.id.btn_cropBeauty);
        ivCropImage = (ImageView) findViewById(R.id.iv_cropImage);
    }

    private void initListener() {
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 下面这句指定调用相机拍照后的照片存储的路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Constants.PHOTONAME)));
                startActivityForResult(intent, Constants.TAKE_PHOTO);

            }
        });
        btnPickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 相册中选择
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, Constants.PICK_PHOTO);
            }
        });
        btnCropBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, ImageCropActivity.class), Constants.CROP_BEAUTY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.i("onActivityResult","data="+data);
        if (requestCode == Constants.TAKE_PHOTO && resultCode == RESULT_OK) {
            Intent intent = new Intent(MainActivity.this, ImageCropActivity.class);
            intent.putExtra("PHOTO_PATH",Constants.PHOTONAME);
            startActivityForResult(intent, Constants.CROP_BEAUTY);
        }
        if (requestCode == Constants.PICK_PHOTO && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Intent intent = new Intent(MainActivity.this, ImageCropActivity.class);
            intent.putExtra("PHOTO_PATH",picturePath);
            startActivityForResult(intent, Constants.CROP_BEAUTY);
        }

        if (requestCode == Constants.CROP_BEAUTY && resultCode == RESULT_OK) {
            if (data != null) {
                final String path = data.getStringExtra("path");
                Bitmap b = BitmapFactory.decodeFile(path);
                if (b != null) {
                    ivCropImage.setImageBitmap(b);
                }
            }
        }
    }
}
