package com.example.mydictionary;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.entity.Gallery3D;
import com.example.helper.GralleryImageAdapter;

public class Grallery3DActivity extends Activity {

    private TextView tvTitle;     
    private Gallery3D gallery;     
    private GralleryImageAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grallery_layout);
        initRes();
    }
    
    private void initRes(){
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        gallery = (Gallery3D) findViewById(R.id.mygallery);

        adapter = new GralleryImageAdapter(this);     
        adapter.createReflectedImages();
        gallery.setAdapter(adapter);
        gallery.setSelection(Integer.MAX_VALUE/2);
        gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position = position % adapter.titles.length;
                tvTitle.setText(adapter.titles[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        gallery.setOnItemClickListener(new OnItemClickListener() {    
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position % adapter.titles.length;
                Toast.makeText(getApplicationContext(), "img " + (position+1) + " selected", Toast.LENGTH_SHORT).show();
            }
        });
    }
}