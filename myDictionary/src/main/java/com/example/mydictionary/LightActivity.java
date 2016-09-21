package com.example.mydictionary;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class LightActivity extends Activity {
    private boolean index = false;
    private boolean index2 = false;
    Button ib, b;
    private Parameters parameter = null;
    Camera camera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.light_activity);
        ib = (Button) findViewById(R.id.lightbutton);
        b = (Button) findViewById(R.id.lightbutton1);
        ib.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (false == index) {
                    camera = Camera.open();
                    camera.startPreview();
                    parameter = camera.getParameters();
                    parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameter);
                    index = true;
                    ib.setText("关闭");
                } else if (true == index) {
                    camera.release();
                    index = false;
                    ib.setText("开启");
                }
            }

        });
        b.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (false == index2) {
                    index2 = true;
                    b.setText("SOS关闭");
                    new Thread(new Runnable(){

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            while (index2) {
                                camera = Camera.open();
                                camera.startPreview();
                                parameter = camera.getParameters();
                                parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
                                camera.setParameters(parameter);
                                camera.release();

                            }
                        }
                        
                    }).start();
                } else if (true == index2) {
                    index2 = false;
                    b.setText("SOS开启");
                }
            }

        });
    }

}
