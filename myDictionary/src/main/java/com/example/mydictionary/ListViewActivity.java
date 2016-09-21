package com.example.mydictionary;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.entity.CListView;
import com.example.entity.MySideBar;
import com.example.entity.MySideBar.OnTouchingLetterChangedListener;
import com.example.entity.UserInfo;
import com.example.helper.MyUserInfoAdapter;
import com.example.helper.PinyinComparator;
import com.example.helper.PinyinUtils;

@SuppressLint("HandlerLeak")
public class ListViewActivity extends BaseActivity implements
        CListView.OnRefreshLoadingMoreListener, OnTouchingLetterChangedListener {

    private static final String[] PHONES_PROJECTION = new String[] {
            Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };
    private CListView dlv_main;// 下拉ListView
    private static int number = 50;
    private static int i = 0;
    private final static int DRAG_INDEX = 1;// 下拉刷新标识
    private final static int LOADMORE_INDEX = 2;// 加载更多标识

    private List<UserInfo> userInfos;
    private TextView overlay;
    private MySideBar myView;
    private MyUserInfoAdapter adapter;
    private ContentResolver resolver;
    private OverlayThread overlayThread = new OverlayThread();

    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                number += 30;
                getUserInfos();
                adapter.notifyDataSetChanged();
            }
        }
    };
    private Handler _handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 2){
                adapter = new MyUserInfoAdapter(ListViewActivity.this, userInfos);
                dlv_main.setAdapter(adapter);
            }else if(msg.what == 3){
                adapter.notifyDataSetChanged();
            }
            super.handleMessage(msg);
        }
        
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.listview_activity);
        
        dlv_main = (CListView) findViewById(R.id.dlv_main);
        myView = (MySideBar) findViewById(R.id.myView);
        overlay = (TextView) findViewById(R.id.tvLetter);
        userInfos = new ArrayList<UserInfo>();
        myView.setOnTouchingLetterChangedListener(this);
        overlay.setVisibility(View.INVISIBLE);
        getUserInfos();

        dlv_main.setTextFilterEnabled(true);
        dlv_main.setOnRefreshListener(this);
    }

    private void getUserInfos() {

        resolver = this.getContentResolver();

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                
                // 获取手机联系人
                Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
                        PHONES_PROJECTION, null, null, null);

                UserInfo[] tempArray = new UserInfo[40];
                if (phoneCursor != null) {

                    while (phoneCursor.moveToPosition(i)) {
                        
                        if(i == number){
                            break;
                        }
                        // 得到手机号码
                        String phoneNumber = phoneCursor.getString(phoneCursor
                                .getColumnIndex(PHONES_PROJECTION[1]));
                        // 当手机号码为空的或者为空字段 跳过当前循环
                        if (TextUtils.isEmpty(phoneNumber))
                            continue;

                        // 得到联系人名称
                        String contactName = phoneCursor.getString(phoneCursor
                                .getColumnIndex(PHONES_PROJECTION[0]));

                        // 得到联系人ID
                        Long contactid = phoneCursor.getLong(phoneCursor
                                .getColumnIndex(PHONES_PROJECTION[3]));

                        // 得到联系人头像ID
                        Long photoid = phoneCursor.getLong(phoneCursor
                                .getColumnIndex(PHONES_PROJECTION[2]));

                        // 得到联系人头像Bitamp
                        Bitmap contactPhoto = null;

                        // photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                        if (photoid > 0) {
                            Uri uri = ContentUris.withAppendedId(
                                    ContactsContract.Contacts.CONTENT_URI,
                                    contactid);
                            InputStream input = ContactsContract.Contacts
                                    .openContactPhotoInputStream(resolver, uri);
                            contactPhoto = BitmapFactory.decodeStream(input);
                        } else {
                            contactPhoto = BitmapFactory.decodeResource(
                                    getResources(), R.drawable.image01);
                        }
                        UserInfo userInfo = new UserInfo(contactName,
                                phoneNumber, PinyinUtils
                                        .getAlpha(contactName));
                        //tempArray[i] = userInfo;
                        userInfos.add(userInfo);
                        i++;
                    }
                    phoneCursor.close();
                }
                /*Arrays.sort(tempArray, new PinyinComparator());
                for(int j=0; j<tempArray.length;j++){
                    userInfos.add(tempArray[j]);
                }*/
                Collections.sort(userInfos, new PinyinComparator());
                if(number == 50)
                    _handler.sendEmptyMessage(2);
                else
                    _handler.sendEmptyMessage(3);
            }
        });
        t.start();
    }

    private class OverlayThread implements Runnable {

        public void run() {
            overlay.setVisibility(View.GONE);
        }

    }

    @Override
    public void onTouchingLetterChanged(String s) {
        Log.i("coder", "s:" + s);

        overlay.setText(s);
        overlay.setVisibility(View.VISIBLE);
        _handler.removeCallbacks(overlayThread);
        _handler.postDelayed(overlayThread, 1000);
        if (alphaIndexer(s) > 0) {
            int position = alphaIndexer(s);
            Log.i("coder", "position:" + position);
            dlv_main.setSelection(position);
        }
    }

    public int alphaIndexer(String s) {
        int position = 0;
        for (int i = 0; i < userInfos.size(); i++) {

            if (userInfos.get(i).getSortFirstLetter().startsWith(s)) {
                position = i;
                break;
            }
        }
        Log.i("coder", "i" + position + userInfos.get(position));
        return position;
    }

    /***
     * 执行类 异步
     * 
     * 
     */
    class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        private int index;// 用于判断是下拉刷新还是点击加载更多

        public MyAsyncTask(Context context, int index) {
            this.index = index;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
                handler.sendEmptyMessage(1);

            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (index == DRAG_INDEX)
                dlv_main.onRefreshComplete();
            else if (index == LOADMORE_INDEX)
                dlv_main.onLoadMoreComplete(false);
        }

    }

    /***
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        new MyAsyncTask(this, DRAG_INDEX).execute();
    }

    /***
     * 点击加载更多
     */
    @Override
    public void onLoadMore() {
        new MyAsyncTask(this, LOADMORE_INDEX).execute();
    }
}