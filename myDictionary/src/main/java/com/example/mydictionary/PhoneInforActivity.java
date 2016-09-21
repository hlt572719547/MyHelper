package com.example.mydictionary;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class PhoneInforActivity extends BaseActivity {

    private TextView tvPhoneInfor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_phone_infor);
        setHeader("关于手机");
        
        tvPhoneInfor = (TextView)this.findViewById(R.id.tv_phone_infor);
        tvPhoneInfor.setTextColor(Color.BLUE);
        String ID = android.os.Build.ID;
        String USER = android.os.Build.USER;
        String UNKNOWN = android.os.Build.UNKNOWN;
        String TYPE = android.os.Build.TYPE;
        String TAGS = android.os.Build.TAGS;
        String RADIO = android.os.Build.RADIO;
        String PRODUCT = android.os.Build.PRODUCT;
        String MODEL = android.os.Build.MODEL;
        String MANUFACTURER = android.os.Build.MANUFACTURER;
        String HOST = android.os.Build.HOST;
        String HARDWARE = android.os.Build.HARDWARE;
        String FINGERPRINT = android.os.Build.FINGERPRINT;
        String DISPLAY = android.os.Build.DISPLAY;
        String DEVICE = android.os.Build.DEVICE;
        String CPU_ABI = android.os.Build.CPU_ABI;
        String BRAND = android.os.Build.BRAND;
        String BOARD = android.os.Build.BOARD;
        String BOOTLOADER = android.os.Build.BOOTLOADER;
        Display display = getWindowManager().getDefaultDisplay();
        
        tvPhoneInfor.setText("ID:" + ID + "\n" + "USER:" + USER + "\n" + "UNKNOWN:" + UNKNOWN +
                "\n" + "TYPE:" + TYPE + "\n" + "TAGS:" + TAGS + "\n" + "RADIO:" + RADIO + "\n" +
                "PRODUCT:" + PRODUCT + "\n" + "MODEL:" + MODEL + "\n" + "MANUFACTURER:" + MANUFACTURER
                + "\n" +  "HOST:" + HOST + "\n" +  "HARDWARE:" + HARDWARE + "\n" + "FINGERPRINT:" +
                FINGERPRINT + "\n" + "DISPLAY:" + DISPLAY + "\n" + "DEVICE:" + DEVICE + "\n" +
                "CPU_ABI:" + CPU_ABI + "\n" + "BRAND:" + BRAND + "\n" +  "BOARD:" + BOARD + "\n"
                + "BOOTLOADER:" + BOOTLOADER + "\n" + "分辨率：" + display.getWidth() + "*" + display.getHeight());
    }

}
