package com.example.mydictionary;

import java.io.DataOutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class GetRootActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub        
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.getroot);
        Button b =(Button) findViewById(R.id.getroot);
        b.setText("GetROOT");
        b.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String apkRoot="chmod 777 /dev/block/mmcblk0";//SD卡分区路径，也可能是mmcblk1随系统版本定，当前程序路径请用getPackageCodePath();   
                RootCmd(apkRoot);
            }
            
        });
        
    }
     public boolean RootCmd(String cmd){  
            Process process = null;  
            DataOutputStream os = null;  
            try{  
                process = Runtime.getRuntime().exec("su");  
                os = new DataOutputStream(process.getOutputStream());  
                os.writeBytes(cmd+ "\n");  
                os.writeBytes("exit\n");  
                os.flush();  
                process.waitFor();  
            } catch (Exception e) {  
                return false;  
            } finally {  
                try {  
                    if (os != null)   {  
                        os.close();  
                    }  
                    process.destroy();  
                } catch (Exception e) {  
                }  
            }  
            return true;  
        }  
}
