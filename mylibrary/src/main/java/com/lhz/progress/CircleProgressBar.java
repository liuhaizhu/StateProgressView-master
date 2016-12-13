package com.lhz.progress;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.lhz.interfaces.OnProgressAnimListener;
import com.lhz.smooth.ISmoothTarget;
import com.lhz.smooth.SmoothHandler;

import java.lang.ref.WeakReference;

/**
 *
 */

public class CircleProgressBar extends View implements ISmoothTarget {

    private final RectF mFillRectF = new RectF();
    private Paint mStrokePaint;
    private Paint mFillPaint;
    private int mColorStroke;
    private int mColorFill;
    private float mPercent;
    private SmoothHandler mSmoothHandler;
    private float mCenterX;
    private float mCenterY;
    private float mStroke;
    private float mInnerCircle;
    private boolean mIsAnimStart;

    public void setIndex(int index) {
        mIndex = index;
    }

    public void setOnProgressAnimListener(OnProgressAnimListener onCircleProgressListener) {
        mOnCircleProgressListener = onCircleProgressListener;
    }

    private int mIndex;
    private OnProgressAnimListener mOnCircleProgressListener;
    public CircleProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void setValue(float centerX, float centerY, float innerCircle, float stroke) {
        mCenterX = centerX;
        mCenterY = centerY;
        mInnerCircle = innerCircle;
        mStroke=stroke;
        mStrokePaint.setStrokeWidth(stroke);
        mFillRectF.left = mCenterX - mInnerCircle;
        mFillRectF.top = mCenterY - mInnerCircle;
        mFillRectF.right = mCenterX + mInnerCircle;
        mFillRectF.bottom = mCenterY + mInnerCircle;
    }


    public void setColor(final int colorStoke, final int colorFill) {
        if (mColorStroke != colorStoke) {
            mColorStroke = colorStoke;
            this.mStrokePaint.setColor(colorStoke);
        }
        if (mColorFill != colorFill) {
            mColorFill = colorFill;
            this.mFillPaint.setColor(colorFill);
        }
        invalidate();
    }

    private void init(Context context, AttributeSet attrs) {
        mColorStroke = Color.GRAY;
        mColorFill = Color.RED;
        setPaintStroke();
        setPaintFill();
    }

    @Override
    public float getPercent() {
        return this.mPercent;
    }

    @Override
    public void setPercent(float percent) {
        percent = Math.min(1, percent);
        percent = Math.max(0, percent);

        if (mSmoothHandler != null) {
            mSmoothHandler.commitPercent(percent);
        }

        if (this.mPercent != percent) {
            this.mPercent = percent;
            invalidate();
        }
    }

    @Override
    public void setSmoothPercent(float percent) {
        getSmoothHandler().loopSmooth(percent);
    }

    @Override
    public void setSmoothPercent(float percent, long durationMillis) {
        getSmoothHandler().loopSmooth(percent, durationMillis);
        mIsAnimStart=true;
    }
    public void setSmoothPercent(float percent, long durationMillis,boolean is) {
        getSmoothHandler().loopSmooth(percent, durationMillis);
        mIsAnimStart = is;
    }

    private SmoothHandler getSmoothHandler() {
        if (mSmoothHandler == null) {
            mSmoothHandler = new SmoothHandler(new WeakReference<ISmoothTarget>(this));
        }
        return mSmoothHandler;
    }

    protected void setPaintStroke() {
        mStrokePaint = new Paint();
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setColor(mColorStroke);
        mStrokePaint.setAntiAlias(true);
    }

    protected void setPaintFill() {
        mFillPaint = new Paint();
        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setColor(mColorFill);
        mFillPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawCircle(mCenterX, mCenterY, mInnerCircle, mStrokePaint);
        float totalD = 360 * mPercent;
        float startD = 180 - totalD / 2;
        if (mColorFill != 0) {
            canvas.drawArc(mFillRectF,
                    startD,
                    totalD,
                    false,
                    mFillPaint);
        }
        canvas.restore();
        if(mPercent==1f){
            if(mIsAnimStart) {
                mIsAnimStart = false;
                if (mOnCircleProgressListener != null) {
                    mOnCircleProgressListener.onCircleAnimFinish(mIndex);
                    mOnCircleProgressListener.onChangeText(mIndex);
                }
            }else {
                if (mOnCircleProgressListener != null) {
                    mOnCircleProgressListener.onChangeText(mIndex);
                }
            }
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, (int) (mCenterY+mStroke+mInnerCircle));
    }

}
