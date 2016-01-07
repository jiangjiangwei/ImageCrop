package com.sven.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by sven on 2016/1/6.
 * 图片的缩放，拖拽自定义控件
 */
public class ImageScaleView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener {
    /**
     * first visible layout
     */
    private boolean once = true;
    /**
     * 图片的矩阵
     */
    private Matrix mMatrx;
    /**
     * 图片临时状态的矩阵
     */
    private Matrix mTempMatrix = new Matrix();
    /**
     * 两指按下的点
     */
    private PointF mStartPoint = new PointF();
    /**
     * 两点的中心点
     */
    private PointF mMidPoint = new PointF();
    /**
     * 两指按下的距离
     */
    private float oldDis = 1f;
    /**
     * 截图框左边距
     */
    private int mLeftPadding;
    /**
     * 截图框上边距
     */
    private int mTopPadding;
    /**
     * 状态
     */
    private int state = NONE;
    /**
     * 默认状态
     */
    private static final int NONE = 0;
    /**
     * 拖拽状态
     */
    private static final int DRAG = 1;
    /**
     * 缩放状态
     */
    private static final int ZOOM = 2;

    public ImageScaleView(Context context) {
        this(context, null);
    }

    public ImageScaleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置imageview的缩放类型为矩阵缩放，这点很关键
        super.setScaleType(ScaleType.MATRIX);
        this.mLeftPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mLeftPadding, getResources().getDisplayMetrics());
        init();
    }

    private void init() {
        mMatrx = new Matrix();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        //第一次layout
        if (once) {
            Drawable drawable = getDrawable();
            if (drawable == null) {
                return;
            }
            //图片的宽高
            int dw = drawable.getIntrinsicWidth();
            int dh = drawable.getIntrinsicHeight();
            //控件的宽高
            int w = getWidth();
            int h = getHeight();
            float scale = 1.0f;
            if (w >= dw && h >= dh) {
                //nothing to do
            }
            if (w >= dw && h < dh) {
                scale = (h * 1.0f) / dh;
            }
            if (w < dw && h >= dh) {
                scale = (w * 1.0f) / dw;
            }
            if (w < dw && h < dh) {
                scale = Math.min((w * 1.0f) / dw, (h * 1.0f) / dh);
            }

            //平移到控件中间
            mMatrx.postTranslate((w - dw) / 2, (h - dh) / 2);
            //缩放
            mMatrx.postScale(scale, scale, w / 2, h / 2);
            setImageMatrix(mMatrx);
            once = false;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mTempMatrix.set(mMatrx);
                mStartPoint.set(event.getX(), event.getY());
                state = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDis = getDistance(event);
                if (oldDis > 10f) {
                    mTempMatrix.set(mMatrx);
                    getMidPoint(mMidPoint, event);
                    state = ZOOM;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (state == DRAG) {
                    mMatrx.set(mTempMatrix);
                    mMatrx.postTranslate(event.getX() - mStartPoint.x, event.getY() - mStartPoint.y);
                } else if (state == ZOOM) {
                    float newDis = getDistance(event);
                    if (newDis > 10f) {
                        mMatrx.set(mTempMatrix);
                        float scale = newDis / oldDis;
                        mMatrx.postScale(scale, scale, mMidPoint.x, mMidPoint.y);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                state = NONE;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                state = NONE;
                break;
        }
        setImageMatrix(mMatrx);
        return true;
//        return super.onTouchEvent(event);
    }

    /**
     * 计算两点距离
     *
     * @param event
     * @return
     */
    private float getDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 计算中心点
     *
     * @param point
     * @param event
     */
    private void getMidPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * 设置左边距
     *
     * @param padding
     */
    public void setLeftPadding(int padding) {
        this.mLeftPadding = padding;
    }

    /**
     * 获取到剪裁的图片
     *
     * @return
     */
    public Bitmap getCropImage() {
        this.mTopPadding = (getHeight() - (getWidth() - 2 * mLeftPadding)) / 2;
        Bitmap bitmap = null;
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        draw(canvas);
        bitmap = Bitmap.createBitmap(bitmap, mLeftPadding, mTopPadding, getWidth() - 2 * mLeftPadding, getHeight() - 2 * mTopPadding);
        return bitmap;
    }
}
