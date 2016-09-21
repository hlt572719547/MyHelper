package com.example.helper;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mydictionary.R;

public class DownloadAdapter extends BaseAdapter {

    private Context context;
    private List Downders;

    public DownloadAdapter(Context context, List downders) {
        super();
        this.context = context;
        Downders = downders;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Downders.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return Downders.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = (View)LayoutInflater.from(context).inflate(R.layout.item_downloader, null);
            holder = new ViewHolder();
            holder.ivFileType = (ImageView)convertView.findViewById(R.id.iv_file_type);
            holder.tvFileName = (TextView)convertView.findViewById(R.id.tv_file_name);
            holder.tvFileSize = (TextView)convertView.findViewById(R.id.tv_file_size);
            holder.pbDownPercent = (ProgressBar)convertView.findViewById(R.id.pb_download_progress);
            holder.tvTimeLeft = (TextView)convertView.findViewById(R.id.tv_time_left);
            holder.tvDownSpeed = (TextView)convertView.findViewById(R.id.tv_downSpeed);
            holder.ivFileDownLoad = (ImageView)convertView.findViewById(R.id.iv_file_download);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        
        return convertView;
    }

    class ViewHolder {
        public ImageView ivFileType;
        public TextView tvFileName;
        public TextView tvFileSize;
        public ProgressBar pbDownPercent;
        public TextView tvTimeLeft;
        public TextView tvDownSpeed;
        public ImageView ivFileDownLoad;
    }
}
