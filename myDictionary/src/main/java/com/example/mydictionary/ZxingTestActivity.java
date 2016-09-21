package com.example.mydictionary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.xys.libzxing.zxing.activity.*;

/**
 * Created by Administrator on 2016/6/14.
 */
public class ZxingTestActivity extends BaseActivity {

    private TextView tvZxing;
    private Button btnZxing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing);

        tvZxing = (TextView)this.findViewById(R.id.zxingText);
        btnZxing = (Button)this.findViewById(R.id.btn_zxing);
        btnZxing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ZxingTestActivity.this, CaptureActivity.class), 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            tvZxing.setText(String.valueOf(data.getExtras().get("result")));
        }
    }
}
