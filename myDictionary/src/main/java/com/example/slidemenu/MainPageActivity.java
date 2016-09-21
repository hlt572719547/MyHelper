package com.example.slidemenu;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.mydictionary.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


public class MainPageActivity extends SlidingFragmentActivity implements OnTabChangeListener,OnPageChangeListener{

	private String[] titleArray = {"学习","资讯","小管家","互动","更多"};
	private Class[] frgmentlist = {StudyFragment.class, NewsFragment.class,ManageFragment.class,
			MessageFragment.class, MoreFragment.class};
	private int[] drawables = {R.drawable.img_1, R.drawable.img_2, R.drawable.img_3, 
			R.drawable.img_4, R.drawable.img_5};
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	
	private FragmentTabHost mTabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_main);
        mTabHost = (FragmentTabHost)findViewById(R.id.tabhost);
		mTabHost.setOnTabChangedListener(this);

        // check if the content frame contains the menu frame
        if (findViewById(R.id.menu_frame) == null) {
            setBehindContentView(R.layout.menu_frame);
            getSlidingMenu().setSlidingEnabled(true);
            getSlidingMenu()
                    .setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            // add a dummy view
            View v = new View(this);
            setBehindContentView(v);
            getSlidingMenu().setSlidingEnabled(false);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeEnabled(false);
        sm.setBehindScrollScale(0.25f);
        sm.setFadeDegree(0.25f);

        //sm.setBackgroundImage(R.drawable.img_frame_background);
        sm.setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (percentOpen * 0.25 + 0.75);
                canvas.scale(scale, scale, -canvas.getWidth() / 2,
                        canvas.getHeight() / 2);
            }
        });

        /*sm.setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (1 - percentOpen * 0.25);
                canvas.scale(scale, scale, 0, canvas.getHeight() / 2);
            }
        });*/
        
        int count = titleArray.length;

		for (int i = 0; i < count; i++) {
			/*mTabHost.addTab(mTabHost.newTabSpec(String.valueOf(i)).setIndicator(titleArray[i],getResources().getDrawable(drawables[i]))
					.setContent(null));*/
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
		
        // set the Above View Fragment
        /*if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(
                    savedInstanceState, "mContent");
        }

        if (mContent == null) {
            mContent = new ContentFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, mContent).commit();

        // set the Behind View Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, new MenuFragment()).commit();*/
    }

    private View getTabItemView(int i) {
		View view = LayoutInflater.from(this).inflate(R.layout.layout_tab_item, null);
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
		//getFragmentManager().beginTransaction().replace(android.R.id.tabcontent, fragments.get(position));
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
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }
}
