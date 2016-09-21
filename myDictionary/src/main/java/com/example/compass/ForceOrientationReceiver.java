package com.example.compass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class ForceOrientationReceiver extends BroadcastReceiver {

    public static final String ACTION_FORCE_ORIENTATION = "com.jrdcom.ACTION_FORCE_ORIENTATION";
    public static final String ORIENTATION = "ORIENTATION";
    public static final int ORIENTATION_DEFAULT = 0;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_UNSPECIFIED = 2;

    @Override
    public void onReceive(Context context, Intent intent) {
        int orientation = intent.getIntExtra(ORIENTATION, ORIENTATION_DEFAULT);
        SharedPreferences sp = context.getSharedPreferences(CompassMainActivity.SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(ORIENTATION, orientation).commit();
        if(CompassMainActivity.getInstance() != null && !CompassMainActivity.getInstance().isFinishing()){
            CompassMainActivity.getInstance().setforceOrientation(orientation);
        }
        Log.i(CompassMainActivity.TAG, "ForceOrientationReceiver: " + orientation);
    }

}
