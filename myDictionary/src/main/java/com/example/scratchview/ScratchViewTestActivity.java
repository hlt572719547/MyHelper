package com.example.scratchview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mydictionary.R;

public class ScratchViewTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratchview);


    }

    public void onTextViewDemoClick(View v) {
        startActivity(new Intent(this, DemoClothingActivity.class));
    }

    public void onImageViewDemoClick(View v) {
        startActivity(new Intent(this, CaptchaActivity.class));
    }

}
