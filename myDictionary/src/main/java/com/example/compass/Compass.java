package com.example.compass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

import com.example.mydictionary.R;


public class Compass extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "BaseActivity";
    protected static HashMap<String, String[]> permissionMap = new HashMap<String, String[]>();
    private static final String KEY_NAME = "com.example.compass";

    // 1407296 -lin.zhou, modify -001 , begin
    private static final String PERMISSIONBLOCK_SHAREPREFERENCES = "AndroidCompass.Permission";
    private static final String PERMISSIONBLOCK_ISFIRST = "isFirst";
    // 1407296 -lin.zhou, modify -001 , begin

    static {
        //TODO:Define Activity class and permission here
        permissionMap.put(KEY_NAME, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.CAMERA,
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 1095228 -yunwen.jiang, modify -001 , begin
        setContentView(R.layout.activity_base);
        if(Build.VERSION.SDK_INT < 23) {
            startActivity(new Intent(Compass.this, CompassMainActivity.class));
            finish();
            return;
        }
        // 1407296 -lin.zhou, modify -002 , begin
        boolean isSkip = false;
        // 1407296 -lin.zhou, modify -002 , begin

        SharedPreferences mSharedPreferences = getSharedPreferences(CompassMainActivity.SP_NAME, Context.MODE_PRIVATE);
        boolean isFirst = mSharedPreferences.getBoolean(PERMISSIONBLOCK_ISFIRST, true);

        if(PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // 1407296 -lin.zhou, modify -003 , begin
            isSkip = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION);

            if(!isFirst){
                if(!isSkip){
                    // 1414762 -lin.zhou, modify -001 , begin
                    startActivity(new Intent(Compass.this, CompassMainActivity.class));
                    finish();
                    return;
                    // 1414762 -lin.zhou, modify -001 , begin
                }
            }
            // 1407296 -lin.zhou, modify -003 , end
        }else{
            startActivity(new Intent(Compass.this, CompassMainActivity.class));
            finish();
            return;
        }
        String[] permissions = permissionMap.get(KEY_NAME);
        if(permissionMap.get(KEY_NAME) != null){
//            PermissionUtil.checkAndRequestPermissions(this, permissions, PermissionUtil.CHECK_REQUEST_PERMISSION_RESULT);

            List<String> requestList = new ArrayList<String>();
            for(String perm : permissions){
                if(PermissionChecker.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED){
                    requestList.add(perm);
                }
            }
            if(requestList.size() > 0){
                ActivityCompat.requestPermissions(this, requestList.toArray(new String[requestList.size()]), PermissionUtil.CHECK_REQUEST_PERMISSION_RESULT);
            }else{
                startActivity(new Intent(Compass.this, CompassMainActivity.class));
                finish();
                return;
            }
            // 1095228 -yunwen.jiang, modify -001 , end
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){

        Log.i(TAG, "No permission, " + "onRequestPermissionsResult");
        if(PermissionUtil.CHECK_REQUEST_PERMISSION_RESULT == requestCode){
            // 1414762 -lin.zhou, modify -002 , begin
            startActivity(new Intent(Compass.this, CompassMainActivity.class));
            finish();
            // 1414762 -lin.zhou, modify -002 , end
        }

    }
    // 1095228 -yunwen.jiang, modify -001 , begin
    static public boolean isAllPerimissionAlown(Activity activity){
        if(Build.VERSION.SDK_INT < 23) {
            return true;
        }
        String[] permissions = permissionMap.get(KEY_NAME);
        for(String perm : permissions){
            if(PermissionChecker.checkSelfPermission(activity, perm) != PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(activity,perm+" Denied",Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }
    // 1095228 -yunwen.jiang, modify -001 , end
}
