package com.example.mydictionary;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A BaseActivity has common routines and variables for an Activity that
 * contains a list of tweets and a text input field.
 * <p/>
 * Not the cleanest design, but works okay for several Activities in this app.
 */

public class BaseActivity extends Activity {
    
    protected SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        _onCreate(savedInstanceState);
    }

    protected void _onCreate(Bundle savedInstanceState) {
        mPref = PreferenceManager.getDefaultSharedPreferences(getApplication());
        // ExitActivityUtil.getInstance().addActivity(this);
    }

    protected void setHeader(String headerName) {
        TextView header = (TextView) findViewById(R.id.header);
        header.setText(headerName);
        ImageView back = (ImageView) findViewById(R.id.header_back);
        back.setVisibility(View.GONE);
    }

    protected void setHeader(String headerName,
            View.OnClickListener headerBackListener) {
        TextView header = (TextView) findViewById(R.id.header);
        ImageView back = (ImageView) findViewById(R.id.header_back);
        header.setText(headerName);
        back.setOnClickListener(headerBackListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ExitActivityUtil.getInstance().remove(this);
        System.gc();
    }
    
}
