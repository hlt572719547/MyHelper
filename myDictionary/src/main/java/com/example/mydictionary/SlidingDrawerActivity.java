package com.example.mydictionary;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Toast;

public class SlidingDrawerActivity extends Activity {
     SlidingDrawer drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.slidingdrawer_activity);
        drawer = (SlidingDrawer) findViewById(R.id.drawer);
        drawer.setOnDrawerCloseListener(new OnDrawerCloseListener(){

            @Override
            public void onDrawerClosed() {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "关闭啦", Toast.LENGTH_SHORT).show();
            }
            
        });
        drawer.setOnDrawerOpenListener(new OnDrawerOpenListener(){

            @Override
            public void onDrawerOpened() {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "打开啦", Toast.LENGTH_SHORT).show();

            }
            
        });
    }

}
