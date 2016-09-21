 package com.example.waterfall;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class WaterFall extends ScrollView {
    
    //延迟发送message的handler
    private DelayHandler delayHandler;
    //添加单元到瀑布流的handler
    private AddItemHandler addItemHandler;
    
    //scrollview直接包裹的linearlayout
    private LinearLayout containerLayout;
    //存放所有列的layout
    private ArrayList<LinearLayout> colLayoutArray;
    
    //当前所处的页面(即加载了次数)
    private int currentPage;
    
    //咩一列中顶部还未回收的最小行数
    private int[] currentTopIndex;
    //每一列中底部还未回收的最大行数
    private int[] currentBottomIndex;
    //每一列中加载了的最大行数
    private int[] maxLoadBottomIndex;
    //每一列的高度
    private int[] colHeight;
    //屏幕宽度
    public int screenWidth;
    //图片路径文件夹
    private String imageDirPath;
    //所有的图片资源路径
    //private ArrayList<String> imageFilePaths;
    private String[] imageFilePaths;
    
    //瀑布流的列数
    private int colCount;
    //每一次加载的单位数量
    private int pageCount;
    //瀑布流的容量
    private int capacity;
    //列的宽度
    private int colWidth;
    //是否是第一页
    private boolean isFirstPage;

    private Random random;
    
    public WaterFall(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public WaterFall(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaterFall(Context context) {
        super(context);
        init();
    }

    /**
    * 初始化
    */
    private void init(){
        delayHandler = new DelayHandler(this);
        addItemHandler = new AddItemHandler(this);
        random = new Random();
        colCount = 4;//默认情况下是4列
        pageCount = 30;//默认每次加载30个瀑布流单元
        capacity = 10000;//默认容纳10000张图
        colWidth = (getResources().getDisplayMetrics().widthPixels)/colCount;
        
        colHeight = new int[colCount];
        currentTopIndex = new int[colCount];
        currentBottomIndex = new int[colCount];
        maxLoadBottomIndex = new int[colCount];
        colLayoutArray = new ArrayList<LinearLayout>();
        //imageFilePaths = new ArrayList<String>();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch(ev.getAction()){
        case MotionEvent.ACTION_DOWN:
            break;
        case MotionEvent.ACTION_UP:
            delayHandler.sendMessageDelayed(delayHandler.obtainMessage(), 200);
            break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int w, int h, int oldw, int oldh) {
        int viewHeight = getHeight();
        if (h > oldh) {//向下滚动
                if (h > 2 * viewHeight) {
                        for (int i = 0; i < colCount; i++) {
                                LinearLayout colLayout = colLayoutArray.get(i);
                                //回收上方超过两屏bitmap
                                FlowingView topItem = (FlowingView) colLayout.getChildAt(currentTopIndex[i]);
                                if (topItem.getFootHeight() < h - 2 * viewHeight) {
                                        topItem.recycle();
                                        currentTopIndex[i] ++;
                                }
                                //重载下方进入(+1)两屏以内bitmap
                                FlowingView bomItem = (FlowingView) colLayout.getChildAt(Math.min(currentBottomIndex[i] + 1, maxLoadBottomIndex[i]));
                                if (bomItem.getFootHeight() <= h + 3 * viewHeight) {
                                        bomItem.reLoad();
                                        currentBottomIndex[i] = Math.min(currentBottomIndex[i] + 1, maxLoadBottomIndex[i]);
                                }
                        }
                }
        } else {//向上滚动
                for (int i = 0; i < colCount; i++) {
                        LinearLayout colLayout = colLayoutArray.get(i);
                        //回收下方超过两屏bitmap
                        FlowingView bomItem = (FlowingView) colLayout.getChildAt(currentBottomIndex[i]);
                        if (bomItem.getFootHeight() > h + 3 * viewHeight) {
                                bomItem.recycle();
                                currentBottomIndex[i] --;
                        }
                        //重载上方进入(-1)两屏以内bitmap
                        FlowingView topItem = (FlowingView) colLayout.getChildAt(Math.max(currentTopIndex[i] - 1, 0));
                        if (topItem.getFootHeight() >= h - 2 * viewHeight) {
                                topItem.reLoad();
                                currentTopIndex[i] = Math.max(currentTopIndex[i] - 1, 0);
                        }
                }
        }
        super.onScrollChanged(w, h, oldw, oldh);
    }

    public void setup(){
        containerLayout = new LinearLayout(getContext());
        containerLayout.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        addView(containerLayout, layoutParams);
        
        for(int i = 0; i < colCount; i++){
            LinearLayout colLayout = new LinearLayout(getContext());
            LinearLayout.LayoutParams colLayoutParams = new LinearLayout.LayoutParams(
                    colWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
            colLayout.setPadding(2, 2, 2, 2);
            colLayout.setOrientation(LinearLayout.VERTICAL);
            
            containerLayout.addView(colLayout, colLayoutParams);
            colLayoutArray.add(colLayout);
        }
        
        try {
            /*File dir = new File(Environment.getExternalStorageDirectory() + "/hlt");
            File file[] = dir.listFiles();
            for (int i = 0; i < file.length; i++) {
                if (file[i].isFile())
                {
                    String icon=file[i].getAbsolutePath();
                    if (icon.endsWith(".png") || icon.endsWith(".jpg"))
                        imageFilePaths.add(icon);
                }
            }*/
            imageFilePaths = getContext().getAssets().list("images");
        } catch (Exception e) {
            e.printStackTrace();
        }
        addNextPage(true);
    }
    
    private void addItem(FlowingView flowingView){
        int minHeightCol = getMinHeightColIndex();
        colLayoutArray.get(minHeightCol).addView(flowingView);
        colHeight[minHeightCol] += flowingView.getViewHeight();
        flowingView.setFootHeight(colHeight[minHeightCol]);
        
        if(!isFirstPage){
            maxLoadBottomIndex[minHeightCol]++;
            currentBottomIndex[minHeightCol]++;
        }
    }
    
    /**
     * 
    */
    private void addNextPage(boolean isFirstPage){
        this.isFirstPage = isFirstPage;
        
        for(int i = pageCount * currentPage; i<pageCount*
                (currentPage+1) && i<capacity; i++){
            new Thread(new PrepareFlowingViewThread(i)).run();
        }
        currentPage ++;
    }
    
    /**
     * 获取所有列中最大的高度
    */
    private int getMaxColHeight(){
        int maxHeight = colHeight[0];
        for(int i = 1; i < colHeight.length; i++){
            if(colHeight[i] > maxHeight){
                maxHeight = colHeight[i];
            }
        }
        return maxHeight;
    }
    
    /**
     * 获取高度最小的列的行号
    */
    private int getMinHeightColIndex(){
        int index = 0;
        for(int i = 1; i < colHeight.length; i++){
            if(colHeight[i] < colHeight[index]){
                index = i;
            }
        }
        return index;
    }
    /**
     * 这里之所以要用一个Handler，是为了使用他的延迟发送message的函数
     * 延迟的效果在于，如果用户快速滑动，手指很早离开屏幕，然后滑动到了底部的时候，
     * 因为信息稍后发送，在手指离开屏幕到滑动到底部的这个时间差内，依然能够加载图片
     * @author hlt
     *
     */
    private static class DelayHandler extends Handler{

        private WeakReference<WaterFall> waterFallWR;
        private WaterFall waterFall;
        
        public DelayHandler(WaterFall waterFall) {
            waterFallWR = new WeakReference<WaterFall>(waterFall);
            this.waterFall = waterFallWR.get();
        }

        @Override
        @SuppressLint("NewApi")
        public void handleMessage(Message msg) {
            if(waterFall.getScrollY() + waterFall.getHeight() >=
                    waterFall.getMaxColHeight() - 50){
                waterFall.addNextPage(false);
            }else if(waterFall.getScaleY() == 0){
                
            }else{
                
            }
            super.handleMessage(msg);
        }
        
    }
    
     /**
     * 添加单元到瀑布流中的Handler
     * @author hlt
     *
     */
    private static class AddItemHandler extends Handler{
        private WeakReference<WaterFall> waterFallWR;
        private WaterFall waterFall;

        public AddItemHandler(WaterFall waterFall) {
            waterFallWR = new WeakReference<WaterFall>(waterFall);
            this.waterFall = waterFallWR.get();
        }

        @Override
        public void handleMessage(Message msg) {
            
            switch(msg.what){
            case 2:
                FlowingView flowingView = (FlowingView)msg.obj;
                waterFall.addItem(flowingView);
                break;
            }
            super.handleMessage(msg);
        }
    }
    
     /**
     * 异步加载要添加的FlowingView
     * @author hlt
     *
     */
    private class PrepareFlowingViewThread implements Runnable{

        public int id;
        
        public PrepareFlowingViewThread(int id) {
            super();
            this.id = id;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            FlowingView flowingView = new FlowingView(getContext(), id, colWidth);
            String imageFilePath = "images/" + imageFilePaths[random.nextInt(imageFilePaths.length)];
            flowingView.setImagePath(imageFilePath);
            flowingView.loadImage();
            addItemHandler.sendMessage(addItemHandler.obtainMessage(2, flowingView));
        }
        
    }

    public void setImageDirPath(String imageDirPath) {
        this.imageDirPath = imageDirPath;
    }

    public void setColCount(int colCount) {
        this.colCount = colCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
}
