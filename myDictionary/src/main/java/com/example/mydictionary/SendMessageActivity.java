package com.example.mydictionary;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendMessageActivity extends Activity {
    EditText phonenumber;
    EditText message;
    Button send, tomessage, insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sendmessage_activity);
        phonenumber = (EditText) findViewById(R.id.phonenumber);
        message = (EditText) findViewById(R.id.message);
        send = (Button) findViewById(R.id.send);
        tomessage = (Button) findViewById(R.id.tomessage);
        insert = (Button) findViewById(R.id.inserttomessage);
        send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String number = phonenumber.getText().toString().trim();
                String m = message.getText().toString().trim();
                if (number.length() > 0 && m.length() > 0) {
                    SmsManager ss = SmsManager.getDefault();
                    PendingIntent pi1 = PendingIntent.getActivity(
                            SendMessageActivity.this, 0, new Intent(
                                    SendMessageActivity.this,
                                    SendMessageActivity.class), 0);
                    ss.sendTextMessage(number, null, m, pi1, null);
                    Toast.makeText(getApplicationContext(), "成功发送了短信",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "都要写上，不能为空",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });
        tomessage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Uri uri = Uri.parse("smsto:13888888888");
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                // 只携带短信内容用下面的Intent动作
                // Intent it = new Intent(Intent.ACTION_VIEW);
                it.putExtra("sms_body", "Intent携带过来的短信内容和电话号码");
                startActivity(it);
            }

        });
        insert.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ContentValues cv = new ContentValues();
                cv.put("type", "1");
                cv.put("address", "95588");
                cv.put("body",
                        "尊敬的中国工商银行金卡用户您好，您尾号为6288的工商金卡"
                                + getCurrentTime()
                                + "收入988,000,000.00元,余额为1088,606,330.36元，您可以登录工行官网(www.icbc.com.cn)查看账单明细。感谢您对工商银行的支持，谢谢");
                getContentResolver().insert(Uri.parse("content://sms/inbox"),
                        cv);
                Toast.makeText(getApplicationContext(), "去短信里看看吧！",
                        Toast.LENGTH_SHORT).show();
            }

        });

    }

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd日HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }
}
