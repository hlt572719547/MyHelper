package com.example.mydictionary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.compass.Compass;
import com.example.compass.CompassMainActivity;
import com.example.datepick.DatePickerActivity;
import com.example.faceofftagglebutton.FaceToggleButtonTestActivity;
import com.example.scratchview.ScratchViewTestActivity;
import com.example.slidemenu.MainPageActivity;
import com.example.stickyheader.StickyHeaderTestActivity;
import com.example.waterfall.WaterFallActivity;

public class MainActivity extends BaseActivity {

    private ListView mLvDemo;
    private static String tags[] = {"关于手机", "瀑布流", "悬浮窗", "拍照", "SlidingDrawer", "短信", "电话", "手电筒",
        "数据库操作", "获得ROOT权限", "PopWindow","3D画廊+左右循环", "ViewPager", "获取手机联系人，模糊查询，下拉刷新和点击查看更多", 
        "随外界声音改变音量", "自定义时间选择对话框", "侧滑菜单布局","Compass", "笑脸切换按钮", "刮刮奖效果view", "stickyHeader的应用",
        "二维码扫一扫"};
    private static final Class activitys[] = {
        PhoneInforActivity.class, WaterFallActivity.class,
        FloatingWindowActivity.class, CameraActivity.class,
        SlidingDrawerActivity.class, SendMessageActivity.class, CallActivity.class,
        LightActivity.class,SQLiteActivity.class,
        GetRootActivity.class,PopWindowActivity.class,
        Grallery3DActivity.class, ViewPagerActivity.class,
        ListViewActivity.class,SoundActivity.class,
        DatePickerActivity.class,MainPageActivity.class,
        Compass.class, FaceToggleButtonTestActivity.class,
        ScratchViewTestActivity.class, StickyHeaderTestActivity.class,
        ZxingTestActivity.class};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setHeader("demo集锦");
        mLvDemo = (ListView)this.findViewById(R.id.lvDemo);
        mLvDemo.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,tags));
        
        mLvDemo.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                startActivity(new Intent(MainActivity.this, activitys[arg2]));
            }
        });
    }
}
