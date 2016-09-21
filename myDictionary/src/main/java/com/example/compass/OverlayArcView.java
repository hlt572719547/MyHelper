package com.example.compass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.example.mydictionary.R;
/*
 ==========================================================================
 *HISTORY
 *
 *Tag            Date              Author        Description
 *============== ============ =============== ==============================
 *BUGFIX-977593  2015/12/07       xing.zhao      [Compass][Android 6.0]Traditional mode screen doesn't have data and blue slider fills whole circle
 *BUGFIX-1060840  2015/12/08      xing.zhao      [Compass][Android 6.0][GD][UE]Blue slider doesn't connect to axile wire, which is rough.
 *===========================================================================
 */
public class OverlayArcView extends View {

    private Paint mPaint;
    private int mProgress = 0;
    private int mLeft;
    private int mTop;
    private int mRight;
    private int mBottom;
    public OverlayArcView(Context context) {

        super(context);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.arc_draw));
        int dpSize = 11;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize, dm);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @SuppressLint("DrawAllocation") public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        RectF mRectF = new RectF((float) mLeft, (float) mTop, (float) mRight, (float) mBottom);
        Path path = new Path();
        //[BUGFIX]-Add-BEGIN by TSCD.xing.zhao,12/07/2015,977593,[Compass][Android 6.0]Traditional mode screen doesn't have data and blue slider fills whole circle
        //[BUGFIX]-Add-BEGIN by TSCD.xing.zhao,12/08/2015,1060840,[Compass][Android 6.0][GD][UE]Blue slider doesn't connect to axile wire, which is rough.
        if (mProgress > 180) {
            path.arcTo(mRectF, 270.5f, 360f - (float) mProgress, true);
            canvas.drawPath(path, mPaint);
        } else if (mProgress <= 180 && mProgress != 0) {
            path.arcTo(mRectF, 269.5f - (float) mProgress, (float) mProgress, true);
            canvas.drawPath(path, mPaint);
        } else if (mProgress == 0) {
            path.arcTo(mRectF, 270f, (float) mProgress, true);
            canvas.drawPath(path, mPaint);
        }
        //[BUGFIX]-Add-END by TSCD.xing.zhao
        //[BUGFIX]-Add-END by TSCD.xing.zhao
    }

    public void setProgerss(int progress) {
        mProgress = progress;
        postInvalidate();
    }

    public void setArcPosition(int left, int top, int right, int bottom) {
        mLeft = left;
        mTop = top;
        mRight = right;
        mBottom = bottom;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
