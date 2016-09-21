package com.example.compass;

/**
 * Created by mtpeng on 7/10/15.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.mydictionary.R;

public class Floatview extends View {

    public int bubbleX, bubbleY,centerX,centerY,mRadius,mLevelViewRadius;
    public Paint mPain = new Paint();
    private Bitmap mBitmap;
    private int mBitmapWidth,mBitmapHeight;
    private Rect src;


    public Floatview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_level);
        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
        src = new Rect(0,0,mBitmapWidth,mBitmapHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect des = new Rect(bubbleX-mRadius,bubbleY-mRadius,bubbleX + mRadius,bubbleY + mRadius);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawBitmap(mBitmap, src, des, mPain);
        /*DisplayMetrics dm = getResources().getDisplayMetrics();
        float strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, dm);
        mPain.setColor(getResources().getColor(R.color.compass_backgroud_cross));
        mPain.setStrokeWidth(strokeWidth);
        canvas.drawLine(centerX - mRadius / 2, centerY, centerX + mRadius/2, centerY, mPain);
        canvas.drawLine(centerX, centerY - mRadius / 2, centerX, centerY + mRadius/2, mPain);
        mPain.setAntiAlias(true);
        mPain.setColor(getResources().getColor(R.color.compass_level_float_backgroud));
        canvas.drawCircle(bubbleX, bubbleY, mRadius, mPain);
        mPain.setColor(getResources().getColor(R.color.compass_level_cross));
        canvas.drawLine(bubbleX - mRadius / 2, bubbleY, bubbleX + mRadius/2, bubbleY, mPain);
        canvas.drawLine(bubbleX, bubbleY - mRadius / 2, bubbleX, bubbleY + mRadius/2, mPain);*/

    }
    public void recycleBitmap(){
        if (mBitmap != null) {
            mBitmap.recycle();
        }
    }
}