package com.example.slidemenu;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class BaseFragment extends Fragment implements OnPageChangeListener {

	private TextView tvTitle;
	private ImageButton ibBack;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//tvTitle = (TextView)this.findViewById(R.id.tv_title);
		//ibBack = (ImageButton)this.findViewById(R.id.ib_back);
	}
	public void setTitle(String title) {
		tvTitle.setText(title);
	}
	
	public void setTitleAndListener (String title, OnClickListener listener) {
		tvTitle.setText(title);
		if (listener != null) {
			ibBack.setOnClickListener(listener);
		} else {
			ibBack.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
				}
			});
		}
	}

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
    }
}
