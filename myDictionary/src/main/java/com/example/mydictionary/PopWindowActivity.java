package com.example.mydictionary;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class PopWindowActivity extends Activity {
    Button show;
    View view;
    PopupWindow pop;
    ListView list;

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popwin_activity);
        view = LayoutInflater.from(this).inflate(R.layout.popmenu, null);
        final String a[] = {
                "哈哈                                                                                                ",
                "乖乖                                                                                                ",
                "默默                                                                                                ",
                "嘟嘟                                                                                                " };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                PopWindowActivity.this, android.R.layout.simple_list_item_1, a);
        list = (ListView) view.findViewById(R.id.poplistView);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "您选了" + a[arg2],
                        Toast.LENGTH_SHORT).show();
            }

        });
        pop = new PopupWindow(view, LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());

        show = (Button) findViewById(R.id.show);
        show.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showAsDropDown(v);
            }

        });

    }

    public void showAsDropDown(View parent) {
        pop.showAsDropDown(parent,
                10,
                // 保证尺寸是根据屏幕像素密度来的
                PopWindowActivity.this.getResources().getDimensionPixelSize(
                        R.dimen.shadow_width));

        // 使其聚集
        pop.setFocusable(true);
        // 设置允许在外点击消失
        pop.setOutsideTouchable(true);
        // 刷新状态
        pop.update();
    }
}
