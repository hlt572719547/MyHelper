package com.example.mydictionary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class CallActivity extends Activity {
    EditText callnumber;
    Button call, call1, contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.call_activity);
        callnumber = (EditText) findViewById(R.id.callnumber);
        call = (Button) findViewById(R.id.call);
        call1 = (Button) findViewById(R.id.call1);
        contact = (Button) findViewById(R.id.contact);
        call.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String number = callnumber.getText().toString().trim();
                if (number.length() > 0) {
                    startActivity(new Intent(Intent.ACTION_CALL, Uri
                            .parse("tel:" + number)));
                }

            }

        });
        call1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String number = callnumber.getText().toString().trim();
                if (number.length() > 0) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri
                            .parse("tel:" + number)));
                }

            }
        });
        contact.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // Intent intent= new Intent("android.intent.action.DIAL");
                // intent.setClassName("com.android.contacts","com.android.contacts.DialtactsActivity");
                // startActivity(intent);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Contacts.CONTENT_URI);
                startActivity(intent);

            }

        });
    }
}
