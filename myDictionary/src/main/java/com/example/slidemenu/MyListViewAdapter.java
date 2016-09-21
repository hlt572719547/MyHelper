package com.example.slidemenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.mydictionary.R;

@SuppressLint("ParserError")
public class MyListViewAdapter extends BaseAdapter {
    private int[] idS = new int[] { R.drawable.emoji_000, R.drawable.emoji_001,
            R.drawable.emoji_002, R.drawable.emoji_003, R.drawable.emoji_004,
            R.drawable.emoji_005, R.drawable.emoji_006, R.drawable.emoji_007,
            R.drawable.emoji_008, R.drawable.emoji_009, R.drawable.emoji_010,
            R.drawable.emoji_011, R.drawable.emoji_012, R.drawable.emoji_013,
            R.drawable.emoji_014, R.drawable.emoji_015, R.drawable.emoji_016,
            R.drawable.emoji_017 };
    // private LinearLayout linearLayout;
    // private PopupWindow popupWindow;
    // private View popupView;
    // private ImageView image;
    private static final String TAG = "MainActivity";
    private Context context;
    private LayoutInflater inflater;
    private ImageView image;

    public MyListViewAdapter(Context context) {
        super();
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @SuppressLint("ParserError")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ViewHolderLinearLayout holderLinearLayout;
        if (convertView == null) {
            if (getItemViewType(position) == 1) {
                holder = new ViewHolder();
                convertView = inflater
                        .inflate(R.layout.listitem_layout_a, null);
                holder.textView = (TextView) convertView
                        .findViewById(R.id.textView1);
                holder.imageView = (ImageView) convertView
                        .findViewById(R.id.imageView1);
                convertView.setTag(holder);
            }
            if (getItemViewType(position) == 0) {
                holderLinearLayout = new ViewHolderLinearLayout();
                convertView = inflater
                        .inflate(R.layout.listitem_layout_b, null);

                holderLinearLayout.linearLayout = (LinearLayout) convertView
                        .findViewById(R.id.linear);
                holderLinearLayout.popupView = inflater.inflate(
                        R.layout.popview, null);

                PopupWindow popupWindow = new PopupWindow(context);
                popupWindow.setContentView(holderLinearLayout.popupView);
                holderLinearLayout.popupView.getBackground().setAlpha(110); // 設置透明度
                popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
                popupWindow.setWidth(LayoutParams.MATCH_PARENT);

                image = (ImageView) holderLinearLayout.popupView
                        .findViewById(R.id.imageView1);

                popupWindow.setOutsideTouchable(true);
                for (int i = 0; i < idS.length; i++) {
                    holderLinearLayout.linearLayout.addView(getView(i,
                            popupWindow, holderLinearLayout.linearLayout));
                }
                convertView.setTag(holderLinearLayout);
            }
        } else {
            if (getItemViewType(position) == 1) {
                holder = (ViewHolder) convertView.getTag();
                holder.imageView.setImageResource(R.drawable.aa);
                holder.textView.setText("Item--->>" + position);
            } else if (getViewTypeCount() == 0) {
                //

            }
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 6 ? 0 : 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

    class ViewHolderLinearLayout {
        View popupView;
        LinearLayout linearLayout;
    }

    private View getView(final int position, final PopupWindow popupWindow,
            final LinearLayout linearLayout) {
        View convertView = new View(context);
        convertView = inflater.inflate(R.layout.item, null);
        ImageView imageView = (ImageView) convertView
                .findViewById(R.id.imageView1);
        TextView textView = (TextView) convertView.findViewById(R.id.textView1);
        imageView.setImageResource(idS[position]);
        textView.setText("图片    " + position);
        final Button button = (Button) convertView.findViewById(R.id.button1);
        button.setTag(position);
        Log.i(TAG, "当前+" + position);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.showAtLocation(linearLayout, Gravity.CENTER, 0, 0);
                int pos = (Integer) button.getTag();
                Log.i(TAG, "监听当前+" + position);
                image.setBackgroundResource(idS[pos]);
            }
        });
        return convertView;
    }
}
