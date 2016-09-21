package com.example.waterfall;

import java.io.IOException;
import java.io.InputStream;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class FlowingView extends View implements View.OnClickListener, View.OnLongClickListener{

    private Context context;
    //瀑布流中单元的唯一编号
    private int index;
    //单元中药显示的图片
    private Bitmap imageBmp;
    //图像文件的路径
    private String imagePath;
    //单元的宽度，也是图像的宽度
    private int width;
    //单元的高度，也是图像的高度
    private int height;
    //画笔
    private Paint paint;
    //图像绘制区域
    private Rect rect;
    //这个单元的底部到它所在列的底部的距离
    private int footHeight;
    
    
    public FlowingView(Context context, int index, int width) {
        super(context);
        this.context = context;
        this.index = index;
        this.width = width;
        init();
    }
    
     /**
     * 基本初始化
     */
    private void init(){
        setOnClickListener(this);
        setOnLongClickListener(this);
        paint = new Paint();
        paint.setAntiAlias(true);
    }
    
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        setMeasuredDimension(width, height);
    }

     /**
     * 图像绘制
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        canvas.drawColor(R.color.white);
        if(imageBmp != null && rect != null){
            canvas.drawBitmap(imageBmp, null, rect, paint);
        }
        super.onDraw(canvas);
    }

    @Override
    public void onClick(View v) {
        //单击全屏显示
        
    }
    
    @Override
    public boolean onLongClick(View v) {
        //长按弹出对话框提示删除等操作
        return false;
    }
    
    /**
     * 按比例放大缩小图像
     */
    /*public Bitmap getSuitableImage(Bitmap bitmap,float x){
          int bmpw=bitmap.getWidth();
          int bmoh=bitmap.getHeight();
          float sx=(float)x/bmpw;//要强制转换，不转换我的在这总是死掉。
          Matrix matrix = new Matrix();
          matrix.postScale(sx, sx); // 长和宽放大缩小的比例
          Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bmpw,
                  bmoh, matrix, true);
          return resizeBmp;
    }*/
    
    /**
     * 异步加载图像
     */
    public void loadImage(){
        InputStream inStream = null;
        try {
            inStream = getContext().getAssets().open(imagePath);
            imageBmp = BitmapFactory.decodeStream(inStream);
            inStream.close();
            inStream = null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(imageBmp != null){
            //Bitmap suitablebmp = getSuitableImage(imageBmp, width);
            int bmpWidth = imageBmp.getWidth();
            int bmpHeight = imageBmp.getHeight();
            height = (int) (bmpHeight * width / bmpWidth);
            rect = new Rect(0, 0, width, height);
        }
    }
    
    /**
     * 重新加载回收了的图片
     */
    public void reLoad(){
        if(imageBmp == null){
            new Thread(new Runnable() {
                
                @Override
                public void run() {
                    InputStream inStream = null;
                    try {
                        inStream = getContext().getAssets().open(imagePath);
                        imageBmp = BitmapFactory.decodeStream(inStream);
                        inStream.close();
                        inStream = null;
                        postInvalidate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    
    /**
     * 防止oom回收图片
     */
    public void recycle(){
        if(imageBmp == null || imageBmp.isRecycled())
            return;
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                imageBmp.recycle();
                imageBmp = null;
                postInvalidate();
            }
        }).start();
    }

    public int getIndex() {
        return index;
    }

    public Bitmap getImageBmp() {
        return imageBmp;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getViewHeight() {
        return height;
    }

    public Rect getRect() {
        return rect;
    }

    public int getFootHeight() {
        return footHeight;
    }

    public void setFootHeight(int footHeight) {
        this.footHeight = footHeight;
    }
}
