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
 * Created by liuhaizhu on 2016/12/10.
 */

public class LineProgressBar extends View implements ISmoothTarget {

    private final RectF rectF = new RectF();
    RectF rectFPercent = new RectF();
    private Paint mBgPaint;

    private int mColorBg;

    private int mColorFill;
    private float mPercent;
    private boolean mIsRadius = false;
    float radius = 0;
    private SmoothHandler mSmoothHandler;
    private float formX, viewW, fromY, viewH, innerViewH;

    private boolean mIsAnimStart;
    private OnProgressAnimListener mOnProgressAnimListener;
    private int mIndex;

    public void setIndex(int index) {
        mIndex = index;
    }

    public LineProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public LineProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LineProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LineProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mColorBg = Color.GRAY;
        mColorFill = Color.RED;

        mBgPaint = new Paint();
        mBgPaint.setColor(mColorBg);
        mBgPaint.setAntiAlias(true);

    }

    public void setValue(float formX, float viewW, float fromY, float viewH, float innerViewH,boolean isRadius) {
        this.formX = formX;
        this.viewW = viewW;
        this.fromY = fromY;
        this.viewH = viewH;
        this.mIsRadius = isRadius;
        this.innerViewH = innerViewH;
        rectF.left = formX;
        rectF.top = fromY + (viewH - innerViewH) / 2.0f;
        rectF.right = formX + viewW;
        rectF.bottom = fromY + (viewH + innerViewH) / 2.0f;
        if (mIsRadius) {
            radius = innerViewH / 2.0f;
        }
        invalidate();
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
        mIsAnimStart = true;
    }

    public void setSmoothPercent(float percent, long durationMillis, boolean is) {
        getSmoothHandler().loopSmooth(percent, durationMillis);
        mIsAnimStart = true;
    }

    private SmoothHandler getSmoothHandler() {
        if (mSmoothHandler == null) {
            mSmoothHandler = new SmoothHandler(new WeakReference<ISmoothTarget>(this));
            mSmoothHandler.setSmoothIncreaseDelayMillis(1);
        }
        return mSmoothHandler;
    }

    public void setColor(int backgroundColor, int fillColor) {
        if (this.mColorBg != backgroundColor) {
            this.mColorBg = backgroundColor;
            this.mBgPaint.setColor(backgroundColor);
        }

        if (this.mColorFill != fillColor) {
            this.mColorFill = fillColor;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        float drawPercent = mPercent;
        if (mColorBg != 0) {
            canvas.drawRoundRect(this.rectF, radius, radius, mBgPaint);
        }

        rectFPercent.left = this.rectF.left;
        rectFPercent.top = this.rectF.top;
        rectFPercent.right = drawPercent * viewW + formX;
        rectFPercent.bottom = this.rectF.bottom;

        Paint mFillPaint = new Paint();
        mFillPaint.setColor(mColorFill);
        mFillPaint.setAntiAlias(true);
        canvas.drawRoundRect(rectFPercent, radius, radius, mFillPaint);
        canvas.restore();
        if (mPercent == 1f && mIsAnimStart) {
            mIsAnimStart = false;
            if (mOnProgressAnimListener != null) {
                mOnProgressAnimListener.onLineAnimFinish(mIndex);
            }
        }

    }

    public void setOnProgressAnimListener(OnProgressAnimListener stateProgressView) {
        mOnProgressAnimListener = stateProgressView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, (int) (fromY+viewH));
    }
}
