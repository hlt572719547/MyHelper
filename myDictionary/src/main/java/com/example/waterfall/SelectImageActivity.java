package com.example.waterfall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mydictionary.R;

public class SelectImageActivity extends Activity{

    private EditText etColCount;
    private EditText etpageCount;
    private EditText etCapacity;
    private TextView tvImagePath;
    private Button btnpath;
    private Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        
        etColCount = (EditText)this.findViewById(R.id.etcolcount);
        etpageCount = (EditText)this.findViewById(R.id.etpagecount);
        etCapacity = (EditText)this.findViewById(R.id.etcapacity);
        tvImagePath = (TextView)this.findViewById(R.id.tviamgepath);
        
        btnpath = (Button)this.findViewById(R.id.btnPath);
        btnpath.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                
            }
        });
        
        btnSubmit = (Button)this.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                String colCount = etColCount.getText().toString();
                String pageCount = etpageCount.getText().toString();
                String capacity = etCapacity.getText().toString();
                //String imagePath = tvImagePath.getText().toString();
                
                if(colCount == null){
                    Toast.makeText(SelectImageActivity.this,
                            "请设置列数！", Toast.LENGTH_SHORT).show();
                    etColCount.setText("");
                }
                if(pageCount == null){
                    Toast.makeText(SelectImageActivity.this,
                            "请设置每次加载量！", Toast.LENGTH_SHORT).show();
                    etpageCount.setText("");
                }
                if(capacity == null){
                    Toast.makeText(SelectImageActivity.this,
                            "请设置容量！", Toast.LENGTH_SHORT).show();
                    etCapacity.setText("");
                }
                if(Integer.parseInt(colCount)<1 || Integer.parseInt(colCount) >5){
                    Toast.makeText(SelectImageActivity.this,
                            "sorry，输入值不合法，请重新输入！", Toast.LENGTH_SHORT).show();
                    etColCount.setText("");
                }
                if(Integer.parseInt(pageCount)<20 || Integer.parseInt(pageCount)>40){
                    Toast.makeText(SelectImageActivity.this,
                            "sorry，输入值不合法，请重新输入！", Toast.LENGTH_SHORT).show();
                    etpageCount.setText("");
                }
                if(Integer.parseInt(capacity)<1000 || Integer.parseInt(capacity)>10000){
                    Toast.makeText(SelectImageActivity.this,
                            "sorry，输入值不合法，请重新输入！", Toast.LENGTH_SHORT).show();
                    etCapacity.setText("");
                }
                /*if(imagePath.trim().equals("") || imagePath == null){
                    Toast.makeText(SelectImageActivity.this,
                            "请选择要加载的图片！", Toast.LENGTH_SHORT).show();
                    tvImagePath.setText("");
                }*/
                
                Intent intent = new Intent(SelectImageActivity.this, WaterFallActivity.class);
                intent.putExtra("colCount", colCount);
                intent.putExtra("pageCount", pageCount);
                intent.putExtra("capacity", capacity);
                //intent.putExtra("imagePath", imagePath);
                startActivity(intent);
            }
        });
    }
    
}
