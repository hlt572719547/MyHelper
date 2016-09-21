package com.example.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DbHelper extends SQLHelper{

    private static String databaseName = "downloader.db";
    private static int version = 1;
    public DbHelper(Context context) {
        super(context, databaseName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db = getWritableDatabase();
            String sql = "create table download_info(_id integer PRIMARY KEY AUTOINCREMENT, thread_id integer, "
                    + "start_pos integer, end_pos integer, compelete_size integer,url varchar(50))";
            db.execSQL(sql);
            super.onCreate(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        
    }
    
    
}
