package com.example.compass;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;

public class PermissionUtil {

    //Default result to {@link OnRequestPermissionsResultCallback#onRequestPermissionsResult(int, String[], int[])}.
    public static final int CHECK_REQUEST_PERMISSION_RESULT = 3;
    // 1414762 -lin.zhou, add -001 , begin
    public static final int CHECK_REQUEST_PERMISSION_LOCATION = 4;
    // 1414762 -lin.zhou, add -001 , end

    /*
     * Add PermissionLifecycleCallbacks on application.oncreate to check permissions.
     */
    @Deprecated
    public static Application.ActivityLifecycleCallbacks getActivityLifecycleCallbacks(String[] permissions){
        return new PermissionLifecycleCallbacks(permissions);
    }

    /*
     * @param activity The target activity.
     * @param permissions The requested permissions.
     * @param requestCode Application specific request code to match with a result
     *    reported to {@link OnRequestPermissionsResultCallback#onRequestPermissionsResult(
     *    int, String[], int[])}.
     */
    public static void checkAndRequestPermissions(final @NonNull Activity activity,
                                          final @NonNull String[] permissions, final int requestCode){
        List<String> requestList = new ArrayList<String>();
        for(String perm : permissions){
            if(PermissionChecker.checkSelfPermission(activity, perm) != PackageManager.PERMISSION_GRANTED){
                requestList.add(perm);
            }
        }

        if(requestList.size() > 0){
            ActivityCompat.requestPermissions(activity, requestList.toArray(new String[requestList.size()]), requestCode);
        }
    }


    /*
     * Check all permissions when resume the activity.
     */
    @Deprecated
    static class PermissionLifecycleCallbacks implements Application.ActivityLifecycleCallbacks{

        String[] permissions;

        public PermissionLifecycleCallbacks(@NonNull String[] permissions) {
            this.permissions = permissions;
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            checkAndRequestPermissions(activity, permissions, CHECK_REQUEST_PERMISSION_RESULT);
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }


}


