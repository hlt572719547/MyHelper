package com.example.helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import dalvik.system.DexClassLoader;

/**
 * Created by Administrator on 2016/7/18.
 */
public class Utils {

    private Context mContext;
    public void getFromRemote() {
        Intent intent = new Intent("com.IZZY");
        PackageManager pm = mContext.getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);

        ResolveInfo resolveInfo = resolveInfos.get(0);
        ActivityInfo activityInfo = resolveInfo.activityInfo;
        //拿到目标类的包名
        String packageName = activityInfo.packageName;

        //拿到目标类所在的apk或者jar存放的路径
        String dexPath = activityInfo.applicationInfo.sourceDir;
        //该路径为拿到目标类dex文件存放在调用者里的路径
        String dexOutputDir = mContext.getApplicationInfo().dataDir;
        //拿到目标类所使用的C/C++库存放路径
        String nativeLibraryDir = activityInfo.applicationInfo.nativeLibraryDir;
        //拿到类装载器
        ClassLoader classLoader = mContext.getClassLoader();

        //DexClassLoader参数分别对应以上四个参数
        DexClassLoader dcl = new DexClassLoader(dexPath,dexOutputDir,nativeLibraryDir,classLoader);
        try {
            //装载目标类
            Class<?> clazz = dcl.loadClass(packageName + ".Share");
            //拿到构造器并实例化对象
            Constructor<?> constructor = clazz.getConstructor();
            Object o = constructor.newInstance();
            //拿到成员方法
            Method display = clazz.getMethod("display", String.class);
            display.invoke(o, "I AM IZZY");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
