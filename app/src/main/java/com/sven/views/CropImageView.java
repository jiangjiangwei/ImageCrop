package com.sven.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * Created by sven on 2016/1/6.
 * 将ImageScaleView和CropBorderView组合在一起
 */
public class CropImageView extends RelativeLayout {
    private ImageScaleView mImageScaleView;
    private CropBorderView mCropBorderView;
    /**
     * 设置剪裁框的左边距
     */
    private int mHorizontalPadding;

    public CropImageView(Context context) {
        this(context, null);
    }

    public CropImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHorizontalPadding = 40;
        mImageScaleView = new ImageScaleView(context);
        mCropBorderView = new CropBorderView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        addView(mImageScaleView, layoutParams);
        addView(mCropBorderView, layoutParams);
        mImageScaleView.setLeftPadding(mHorizontalPadding);
        mCropBorderView.setHorizontalPadding(mHorizontalPadding);
    }

    /**
     * 获取到剪裁的图片
     *
     * @return
     */
    public Bitmap getCropImage() {
        return mImageScaleView.getCropImage();
    }


    public void setImageResoure(int resId) {
        mImageScaleView.setImageResource(resId);
    }

    public void setImageBitmap(Bitmap bm) {
        mImageScaleView.setImageBitmap(bm);
    }

    public void setImageDrawable(Drawable drawable) {
        mImageScaleView.setImageDrawable(drawable);
    }

}
