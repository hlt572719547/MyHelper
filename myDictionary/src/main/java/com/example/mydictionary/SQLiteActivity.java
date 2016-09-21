package com.example.mydictionary;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.helper.SQLHelper;

public class SQLiteActivity extends Activity {
    Button write, read, update, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sqlactivity);
        write = (Button) findViewById(R.id.button1);
        read = (Button) findViewById(R.id.button2);
        update = (Button) findViewById(R.id.button3);
        delete = (Button) findViewById(R.id.button4);
        write.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                SQLHelper helper = new SQLHelper(SQLiteActivity.this, "chendb", null,
                        1);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", "aaa");
                values.put("money", "13.7");
                values.put("content", "egdfreigfeiugfvrvfreiugvireu");
                db.insert("user", null, values);
                db.close();
                Toast.makeText(getApplicationContext(), "write success",
                        Toast.LENGTH_SHORT).show();
            }

        });
        update.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SQLHelper helper = new SQLHelper(SQLiteActivity.this, "chendb", null,
                        1);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", "atoc");
                db.update("user", values, "name=?", new String[] { "aaa" });
                db.close();
                Toast.makeText(getApplicationContext(), "update success",
                        Toast.LENGTH_SHORT).show();
            }

        });
        delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SQLHelper helper = new SQLHelper(SQLiteActivity.this, "chendb", null,
                        1);
                SQLiteDatabase db = helper.getWritableDatabase();
                db.delete("user", "name=?", new String[] { "bbb" });
                db.close();
                Toast.makeText(getApplicationContext(), "delete success",
                        Toast.LENGTH_SHORT).show();
            }

        });
        read.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SQLHelper helper = new SQLHelper(SQLiteActivity.this, "chendb", null,
                        1);
                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor cursor = db.rawQuery("select * from user", null);
                while (cursor.moveToNext()) {
                    String name = cursor.getString(0);
                    Double money = Double.parseDouble(cursor.getString(1));
                    String a = cursor.getString(2);
                    Toast.makeText(getApplicationContext(),
                            "" + name + "  " + money + " " + a,
                            Toast.LENGTH_SHORT).show();
                }
                cursor.close();
                db.close();
            }
        });
    }
}
