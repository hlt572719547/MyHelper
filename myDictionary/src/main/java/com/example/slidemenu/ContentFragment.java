package com.example.slidemenu;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.mydictionary.R;

public class ContentFragment extends BaseFragment implements OnTabChangeListener{

	private FragmentTabHost mTabHost;
	private ViewPager pager;
	private String[] titleArray = {"学习","资讯","小管家","互动","更多"};
	private Class[] frgmentlist = {StudyFragment.class, NewsFragment.class,ManageFragment.class,
			MessageFragment.class, MoreFragment.class};
	private int[] drawables = {R.drawable.img_1, R.drawable.img_2, R.drawable.img_3, 
			R.drawable.img_4, R.drawable.img_5};
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view  = inflater.inflate(R.layout.layout_content, null);
		pager = (ViewPager)view.findViewById(R.id.pager);
		mTabHost = (FragmentTabHost)view.findViewById(R.id.tabhost);
		mTabHost.setOnTabChangedListener(this);

		int count = titleArray.length;

		for (int i = 0; i < count; i++) {
			TabSpec tabSpec = mTabHost.newTabSpec(titleArray[i])
					.setIndicator(getTabItemView(i));
			mTabHost.addTab(tabSpec);
			mTabHost.setTag(i);
			try {
				fragments.add((Fragment)frgmentlist[i].newInstance());
			} catch (Exception e) {
				Log.e("ContentFragment", e.getMessage());
			}
			
		}
		//pager.setAdapter(new MyPagerAdapter(getFragmentManager(), fragments));
		return view;
	}
	
	private View getTabItemView(int i) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_tab_item, null);
		ImageView mImageView = (ImageView) view
				.findViewById(R.id.tab_icon);
		TextView mTextView = (TextView) view.findViewById(R.id.tab_title);
		mImageView.setBackgroundResource(drawables[i]);
		mTextView.setText(titleArray[i]);
		return view;
	}
	
	@Override
	public void onTabChanged(String tabId) {
		int position = mTabHost.getCurrentTab();
		getFragmentManager().beginTransaction().replace(android.R.id.tabcontent, fragments.get(position));
		//pager.setCurrentItem(position);
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		TabWidget widget = mTabHost.getTabWidget();
		int oldFocusability = widget.getDescendantFocusability();
		widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		mTabHost.setCurrentTab(arg0);
		widget.setDescendantFocusability(oldFocusability);
		mTabHost.getTabWidget().getChildAt(arg0) 
				.setBackgroundResource(R.drawable.shadow);
	}
}
