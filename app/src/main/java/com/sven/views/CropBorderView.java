package com.sven.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by sven on 2016/1/6.
 * 剪裁框
 * 四个半透明区域和一个中间空白区域
 */
public class CropBorderView extends View {
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 左边距
     */
    private int mHorizontalPadding;//default
    /**
     * 上边距，默认左右两边边距一样
     */
    private int mVerticalPadding;
    /**
     * 白线宽度
     */
    private int mBoderWidth = 1;
    /**
     * 剪裁框宽度
     */
    private int mCropWidth;

    public CropBorderView(Context context) {
        this(context, null);
    }

    public CropBorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropBorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.i("CropBorderView", "mHorizontalPadding=" + mHorizontalPadding);
        mHorizontalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources().getDisplayMetrics());
        mBoderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mBoderWidth, getResources().getDisplayMetrics());
        init();
    }

    private void init() {
        mPaint = new Paint();
        //因为画的是矩形框，设置抗锯齿
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mCropWidth = (getWidth() - 2 * mHorizontalPadding);
        mVerticalPadding = (getHeight() - mCropWidth) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画半透明区域
        mPaint.setColor(Color.parseColor("#aa000000"));
        mPaint.setStyle(Paint.Style.FILL);
        //左边矩形
        canvas.drawRect(0, 0, mHorizontalPadding, getHeight(), mPaint);
        //顶部矩形
        canvas.drawRect(mHorizontalPadding, 0, mHorizontalPadding + mCropWidth, mVerticalPadding, mPaint);
        //右边矩形
        canvas.drawRect(mHorizontalPadding + mCropWidth, 0, getWidth(), getHeight(), mPaint);
        //底部矩形
        canvas.drawRect(mHorizontalPadding, mVerticalPadding + mCropWidth, mHorizontalPadding + mCropWidth, getHeight(), mPaint);
        //中间区域白线
        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaint.setStrokeWidth(mBoderWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(mHorizontalPadding, mVerticalPadding, mHorizontalPadding + mCropWidth, mVerticalPadding + mCropWidth, mPaint);
    }

    /**
     * 设置左边距
     *
     * @param padding
     */
    public void setHorizontalPadding(int padding) {
        this.mHorizontalPadding = padding;
    }

}
