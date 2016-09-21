package com.example.mydictionary;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class CameraActivity extends Activity {
    private Button b1, b2;
    private ImageView iv;
    String strImgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.camera_activity);
        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        iv = (ImageView) findViewById(R.id.imageView1);
        b2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Bitmap b1 = getDiskBitmap(strImgPath);
                Options opts = new Options();
                opts.inJustDecodeBounds = false;
                opts.inSampleSize = 4;
                Bitmap b1 = BitmapFactory.decodeFile(strImgPath, opts);            
                iv = (ImageView) findViewById(R.id.imageView1);
                iv.setImageBitmap(b1);
            }

        });
        b1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                letCamera();
            }

        });
    }

    protected void letCamera() {
        // TODO Auto-generated method stub
        Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        strImgPath = Environment.getExternalStorageDirectory().toString()
                + "/chen/";//
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date()) + ".jpg";//
        File out = new File(strImgPath);
        if (!out.exists()) {
            out.mkdirs();
        }
        out = new File(strImgPath, fileName);
        strImgPath = strImgPath + fileName;//
        Uri uri = Uri.fromFile(out);
        imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(imageCaptureIntent, 1);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case 1://
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();
            }
            break;
        default:
            break;
        }
    }
}
