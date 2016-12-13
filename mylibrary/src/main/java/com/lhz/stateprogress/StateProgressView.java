package com.lhz.stateprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lhz.R;
import com.lhz.interfaces.OnProgressAnimListener;
import com.lhz.progress.CircleProgressBar;
import com.lhz.progress.LineProgressBar;
import com.lhz.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.GRAY;
import static android.graphics.Color.GREEN;

/**
 * Created by liuhaizhu on 2016/12/12.
 */

public class StateProgressView extends FrameLayout implements OnProgressAnimListener {

    private Context mContext;
    /**
     * height of line
     */
    private float mInnerLineHeight;
    /**
     * radius of fill circle
     */
    private float mCircleFillRadius;

    private float mCircleStokeRadius;

    private float mStateViewHeight;

    private float mTextHeight;

    private int mColorLineNormal = GRAY;

    private int mColorLineState = GREEN;

    private int mColorCircleStoke = GRAY;

    private int mColorCircleFilled = Color.GREEN;

    private int mColorTextNormal = GRAY;

    private int mColorTextState = 0;

    private boolean mHasText = false;
    private boolean mIsLineRadius = false;

    private int mItemCount;

    private int mStateItemCount;

    private List<String> mItems = new ArrayList<>();

    private List<LineProgressBar> mListLine = new ArrayList<>();
    private List<CircleProgressBar> mListCircle = new ArrayList<>();
    private List<TextView> mListText = new ArrayList<>();
    private long mDuration = 400;

    public StateProgressView(Context context) {
        super(context);
        init(context, null, 0, 0);

    }

    public StateProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }


    public StateProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StateProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleRes, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mContext = context;
        mCircleFillRadius = DensityUtil.dip2px(context, 12);
        mCircleStokeRadius = DensityUtil.dip2px(context, 16);
        mStateViewHeight = DensityUtil.dip2px(context, 40);
        mTextHeight = DensityUtil.dip2px(context, 32);
        mInnerLineHeight = DensityUtil.dip2px(context, 10);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StateProgressView, defStyleAttr, defStyleRes);
        for (int i = 0, count = a.getIndexCount(); i < count; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.StateProgressView_color_circle_fill) {
                mColorCircleFilled = a.getColor(attr, GRAY);
            } else if (attr == R.styleable.StateProgressView_color_circle_stroke) {
                mColorCircleStoke = a.getColor(attr, GREEN);
            } else if (attr == R.styleable.StateProgressView_color_line_normal) {
                mColorLineNormal = a.getColor(attr, GRAY);
            } else if (attr == R.styleable.StateProgressView_color_line_state) {
                mColorLineState = a.getColor(attr, GREEN);
            } else if (attr == R.styleable.StateProgressView_color_text_normal) {
                mColorTextNormal = a.getColor(attr, GRAY);
            } else if (attr == R.styleable.StateProgressView_color_text_state) {
                mColorTextState = a.getColor(attr, 0);
            } else if (attr == R.styleable.StateProgressView_line_height) {
                mInnerLineHeight = a.getDimension(attr, mInnerLineHeight);
            } else if (attr == R.styleable.StateProgressView_radius_circle_fill) {
                mCircleFillRadius = a.getDimension(attr, mCircleFillRadius);
            } else if (attr == R.styleable.StateProgressView_radius_circle_stroke) {
                mCircleStokeRadius = a.getDimension(attr, mCircleStokeRadius);
            } else if (attr == R.styleable.StateProgressView_state_height) {
                mStateViewHeight = a.getDimension(attr, mStateViewHeight);
            } else if (attr == R.styleable.StateProgressView_text_height) {
                mTextHeight = a.getDimension(attr, mTextHeight);
            } else if (attr == R.styleable.StateProgressView_is_line_radius) {
                mIsLineRadius = a.getBoolean(attr, false);
            }

        }
        a.recycle();
    }

    public void setItems(List<String> list, int stateCount, int duration) {
        if (list != null && list.size() > 0) {
            mItems = list;
            mItemCount = list.size();
            mHasText = true;
            startAnim(stateCount, duration);
        }
    }

    public void setItems(List<String> list) {
        if (list != null && list.size() > 0) {
            mItems = list;
            mItemCount = list.size();
            mHasText = true;
            addViews();
        }
    }

    public void setItems(int totalCount) {
        mItemCount = totalCount;
        mHasText = false;
        addViews();
    }

    private void addViews() {
        removeAllViews();
        mListCircle.clear();
        mListLine.clear();
        mListText.clear();
        float width = getMeasuredWidth();
        float y = mStateViewHeight / 2;
        for (int i = 0; i < mItemCount; i++) {
            float x = width * (1 + 2f * i) / (2f * mItemCount);
            addCircleBar(i, x, y);
        }
    }

    public void startAnim(int stateItemCount, long duration) {
        addViews();
        if (mListCircle.size() > 0 && stateItemCount >= 0) {
            mStateItemCount = stateItemCount;
            mDuration = duration;
            mListCircle.get(0).setSmoothPercent(1, mDuration);
        }
    }


    private void addCircleBar(int index, float x, float y) {
        CircleProgressBar cpb = new CircleProgressBar(mContext);
        cpb.setValue(x, y, mCircleFillRadius, mCircleStokeRadius - mCircleFillRadius);
        cpb.setIndex(index);
        cpb.setColor(mColorCircleStoke, mColorCircleFilled);
        cpb.setOnProgressAnimListener(this);
        mListCircle.add(cpb);
        addView(cpb);
        if (mHasText && mItems.size() > index) {
            addText(index, y);
        }
        if (index < mItemCount - 1) {
            LineProgressBar lpb = new LineProgressBar(mContext);
            float fromX = x + mCircleStokeRadius;
            float viewW = getMeasuredWidth() / mItemCount - 2f * mCircleStokeRadius;
            lpb.setValue(fromX, viewW, 0, 2f * y, mInnerLineHeight, mIsLineRadius);
            lpb.setColor(mColorLineNormal, mColorLineState);
            lpb.setOnProgressAnimListener(this);
            lpb.setIndex(index);
            addView(lpb);
            mListLine.add(lpb);
        }
    }

    private void addText(int index, float y) {
        TextView tv = new TextView(mContext);
        tv.setGravity(Gravity.CENTER);
        tv.setSingleLine();
        tv.setText(mItems.get(index));
        addView(tv);
        ViewGroup.LayoutParams para;
        para = tv.getLayoutParams();
        para.height = (int) mTextHeight;
        para.width = getMeasuredWidth() / mItemCount;
        LayoutParams params = new LayoutParams(para);
        params.setMargins(para.width * index, (int) (2f * y + 10), 0, 0);
        tv.setLayoutParams(params);
        tv.setTextColor(mColorTextNormal);
        mListText.add(tv);
    }

    @Override
    public void onCircleAnimFinish(int index) {

        if (index < mItemCount - 1) {
            if (mStateItemCount == index) {
                mListLine.get(index).setSmoothPercent(1, mDuration, false);
            } else {
                mListLine.get(index).setSmoothPercent(1, mDuration);
            }
        }
    }

    @Override
    public void onChangeText(int index) {
        if (mHasText && mColorTextState != 0 && index < mListText.size()) {
            mListText.get(index).setTextColor(mColorTextState);
        }
    }

    @Override
    public void onLineAnimFinish(int index) {
            if (index + 1 == mStateItemCount) {
                mListCircle.get(index+1).setSmoothPercent(1, mDuration, false);
            } else if(index+1<mStateItemCount){
                mListCircle.get(index+1).setSmoothPercent(1, mDuration);
            }
    }
}
