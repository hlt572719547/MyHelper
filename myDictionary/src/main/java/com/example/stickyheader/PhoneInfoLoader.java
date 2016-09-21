package com.example.stickyheader;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.example.entity.UserInfo;
import com.example.helper.PinyinComparator;
import com.example.helper.PinyinUtils;
import com.example.mydictionary.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public class PhoneInfoLoader {

    private static PhoneInfoLoader mLoader;
    private ContentResolver mResolver;
    private Context mContext;
    private static final String[] PHONES_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.Contacts.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID };
    private static final String TAG = PhoneInfoLoader.class.getSimpleName();
    private List<UserInfo> userInfors = new ArrayList<>();
    private ArrayList<OnLoadCallback> onLoadCallbacks = new ArrayList<>();
    private boolean loading;
    private static int number = 50;
    private static int i = 0;

    public static PhoneInfoLoader getInstance(Context context) {
        if (mLoader == null) {
            mLoader = new PhoneInfoLoader(context);
        }
        return mLoader;
    }

    private PhoneInfoLoader(Context context) {
        mContext = context;
    }

    public void load(final OnLoadCallback onLoadCallback) {

        mResolver = mContext.getContentResolver();

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                // 获取手机联系人
                Cursor phoneCursor = mResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
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
                                    .openContactPhotoInputStream(mResolver, uri);
                            contactPhoto = BitmapFactory.decodeStream(input);
                        } else {
                            contactPhoto = BitmapFactory.decodeResource(
                                    mContext.getResources(), R.drawable.image01);
                        }
                        UserInfo userInfo = new UserInfo(contactName,
                                phoneNumber, PinyinUtils
                                .getAlpha(contactName));
                        //tempArray[i] = userInfo;
                        userInfors.add(userInfo);
                        i++;
                    }
                    phoneCursor.close();
                }
                /*Arrays.sort(tempArray, new PinyinComparator());
                for(int j=0; j<tempArray.length;j++){
                    userInfos.add(tempArray[j]);
                }*/
                Collections.sort(userInfors, new PinyinComparator());
                if(number == 50) {
                    onLoadCallback.onRandomUsersDidLoad(userInfors);
                } else
                    onLoadCallback.onRandomUserLoadFailure();
            }
        });
        t.start();
    }

    public interface OnLoadCallback {
        void onRandomUsersDidLoad(List<UserInfo> randomUsers);
        void onRandomUserLoadFailure();
    }
}
