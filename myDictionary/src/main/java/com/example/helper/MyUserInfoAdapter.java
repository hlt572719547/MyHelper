package com.example.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.entity.UserInfo;
import com.example.mydictionary.R;

public class MyUserInfoAdapter extends BaseAdapter {

    private Context context;
    private List<UserInfo> contacts;
    private LayoutInflater inflater;

    public MyUserInfoAdapter(Context context, List<UserInfo> contacts) {
        super();
        this.contacts = new ArrayList<UserInfo>();
        this.context = context;
        this.contacts = contacts;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater
                    .inflate(R.layout.item_listviewactivity, null);
            holder.ivPhoto = (ImageView)convertView.findViewById(R.id.iv_photo);
            holder.tvFirstLetter = (TextView)convertView.findViewById(R.id.tv_first_letter);
            if(position == 0){
                holder.tvFirstLetter.setVisibility(View.VISIBLE);
                holder.tvFirstLetter.setText(PinyinUtils.getFirstLetter(contacts.get(position).getName()));
            }else if(PinyinUtils.getFirstLetter(contacts.get(position).getName())
                    .equals(PinyinUtils.getFirstLetter(contacts.get(position-1).getName()))){
                holder.tvFirstLetter.setVisibility(View.GONE);
            }else{
                holder.tvFirstLetter.setVisibility(View.VISIBLE);
                holder.tvFirstLetter.setText(PinyinUtils.getFirstLetter(contacts.get(position).getName()));
            }
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvName.setText(contacts.get(position).getName());
            holder.tvPhoneNumber = (TextView) convertView
                    .findViewById(R.id.tv_phone_number);
            holder.tvPhoneNumber.setText(contacts.get(position).getNumber());
            /*convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }*/

        return convertView;
    }
}

class ViewHolder {
    public TextView tvFirstLetter;
    public ImageView ivPhoto;
    public TextView tvName;
    public TextView tvPhoneNumber;
}
