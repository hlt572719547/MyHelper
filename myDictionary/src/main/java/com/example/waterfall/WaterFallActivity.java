package com.example.waterfall;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;

import com.example.mydictionary.BaseActivity;
import com.example.mydictionary.R;

@SuppressLint("NewApi")
public class WaterFallActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_water_fall);
        
        WaterFall waterFall = (WaterFall)this.findViewById(R.id.waterfall);
        /*Intent intent = this.getIntent();
        waterFall.setColCount(intent.getIntExtra("colCount", 4));
        waterFall.setPageCount(intent.getIntExtra("pageCount", 30));
        waterFall.setCapacity(intent.getIntExtra("capacity", 5000));
        waterFall.setImageDirPath(intent.getStringExtra("imagePath"));
        Point p = new Point();
        getWindowManager().getDefaultDisplay().getSize(p);
        waterFall.screenWidth = p.x;*/
        waterFall.setup();
    }
}
