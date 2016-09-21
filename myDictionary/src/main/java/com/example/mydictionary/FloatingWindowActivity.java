package com.example.mydictionary;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
public class FloatingWindowActivity extends BaseActivity implements OnClickListener {

        private WindowManager windowManager = null; 
        private WindowManager.LayoutParams windowManagerParams = null; 

        private float mTouchX; 
        private float mTouchY; 
        private float x; 
        private float y; 
        private float mStartX; 
        private float mStartY; 
        
        private View view;
        private ImageView icon;
        private LinearLayout floats;
        private LinearLayout btn1;
        private LinearLayout btn2;
        private LinearLayout btn3;
        @Override
        public void onCreate(Bundle savedInstanceState) { 
            super.onCreate(savedInstanceState); 
            requestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏 
            getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , 
                    WindowManager.LayoutParams. FLAG_FULLSCREEN);//全屏 
            setContentView(R.layout.activity_main); 
            setHeader("悬浮穿");
            view = LayoutInflater.from(this).inflate(R.layout.activity_floating_window, null);
            icon = (ImageView) view.findViewById(R.id.icon);
            floats = (LinearLayout) view.findViewById(R.id.floats);
            btn1 = (LinearLayout) floats.findViewById(R.id.btn1);
            btn2 = (LinearLayout) floats.findViewById(R.id.btn2);
            btn3 = (LinearLayout) floats.findViewById(R.id.btn3);
            btn1.setOnClickListener(this);
            btn2.setOnClickListener(this);
            btn3.setOnClickListener(this);
            createView();  
        } 

        private void createView() { 
            
            // 1、获取WindowManager对象 
            windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); 
            // 2、设置LayoutParams(全局变量）相关参数 
            windowManagerParams = new WindowManager.LayoutParams();
//            windowManagerParams = ((FloatApplication) getApplication()).getWindowParams(); 
            //3、设置相关的窗口布局参数 （悬浮窗口效果）
            windowManagerParams.type = LayoutParams.TYPE_PHONE; // 设置window type 
            windowManagerParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明 
            //4、设置Window flag == 不影响后面的事件  和  不可聚焦
            windowManagerParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL 
                    | LayoutParams.FLAG_NOT_FOCUSABLE; 
            /* 
             * 注意，flag的值可以为： 
             * LayoutParams.FLAG_NOT_TOUCH_MODAL 不影响后面的事件 
             * LayoutParams.FLAG_NOT_FOCUSABLE  不可聚焦 
             * LayoutParams.FLAG_NOT_TOUCHABLE 不可触摸 
             */
            //5、 调整悬浮窗口至左上角，便于调整坐标 
            windowManagerParams.gravity = Gravity.LEFT | Gravity.TOP;  
            // 以屏幕左上角为原点，设置x、y初始值 
            windowManagerParams.x = 0; 
            windowManagerParams.y = 80; 
            //6、设置悬浮窗口长宽数据 
            windowManagerParams.width = LayoutParams.WRAP_CONTENT; 
            windowManagerParams.height = LayoutParams.WRAP_CONTENT; 
            
            //获得屏幕的宽高
                    Display display = windowManager.getDefaultDisplay();
                    final int screenWith = display.getWidth();
                    int screenHeight = display.getHeight();
                    System.out.println("screenWith="+screenWith+",screenHeight="+screenHeight);
            
            icon.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    //1、获取到状态栏的高度 
                    Rect frame =  new  Rect();   
                    icon.getWindowVisibleDisplayFrame(frame); 
                    int  statusBarHeight = frame.top; 
                    System.out.println("状态栏高度:"+statusBarHeight); 
                    //2、获取相对屏幕的坐标，即以屏幕左上角为原点 。y轴坐标= y（获取到屏幕原点的距离）-状态栏的高度
                    x = event.getRawX(); 
                    y = event.getRawY() - statusBarHeight; // statusBarHeight是系统状态栏的高度 
                    
                    System.out.println("x="+x+",y="+y);
                    //3、处理触摸移动
                    switch (event.getAction()) { 
                    case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作 
                            // 获取相对View的坐标，即以此View左上角为原点 
                            mTouchX = event.getX(); 
                            mTouchY = event.getY(); 
                            mStartX = x; 
                            mStartY = y; 
                            
                            System.out.println(",mTouchX=" + mTouchX + ",mTouchY="
                                            + mTouchY);
                            break; 

                    case MotionEvent.ACTION_MOVE: // 捕获手指触摸移动动作 
                            updateViewPosition(); 
                            break; 

                    case MotionEvent.ACTION_UP: // 捕获手指触摸离开动作 
                        float left = x-mTouchX;   
                        if(left <= screenWith/2){//图标icon吸附在左边
                               x = mTouchX;
                           }else {//图标icon吸附在右边
                            x = mTouchX + screenWith;
                        }
                            
                            updateViewPosition(); 
                            //移动终点的坐标，重置为0
                            mTouchX = mTouchY = 0; 
                            //移动距离少于5 ,则视为点击，触发点击的回调
                            if ((x - mStartX) < 5 && (y - mStartY) < 5) { 
                                    onClick(v); 
                            }
                            break; 
                    } 
                    return true; 
                }
            });
            
            windowManager.addView(view, windowManagerParams); // 显示myFloatView图像 
        } 

        /**
         * 用于更新 悬浮窗位置参数
         * */
        private void updateViewPosition() { 
                windowManagerParams.x = (int) (x - mTouchX); 
                windowManagerParams.y = (int) (y - mTouchY); 
                
                System.out.println("wp.x="+windowManagerParams.x+",wp.y="+windowManagerParams.y);
                // 刷新屏幕显示 
                windowManager.updateViewLayout(view, windowManagerParams); 
        } 

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
            case R.id.icon:
                if (floats.getVisibility() == View.VISIBLE)
                {
                    floats.setVisibility(View.GONE);
                } else {
                    floats.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn1:
                Toast.makeText(this,"亲，我是论坛！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn2:
                Toast.makeText(this,"亲，我是手机验证！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn3:
                windowManager.removeView(view);
                finish();
                break;
            default:
                break;
            }
            
        } 
        
        public void onDestroy() { 
            super.onDestroy(); 
            // 在程序退出(Activity销毁）时销毁悬浮窗口 
//            windowManager.removeView(view); 
        } 
}
