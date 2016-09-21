package com.example.mydictionary;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ViewPagerActivity extends Activity implements OnClickListener {
    private View view1, view2, view3;// 需要显示的View
    private TextView tv1, tv2, tv3;
    private ViewPager viewPager;
    private TranslateAnimation transAnima;
    private ImageView line;
    private ListView lv;
    private ArrayList<View> views;
    private ArrayList<String> titles;
    private int currentIndex = 0;
    int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        initView();
    }

    private void initView() {
        DisplayMetrics dms = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dms);
        width = dms.widthPixels / 3;
        line = (ImageView) findViewById(R.id.iv_line);
        LayoutInflater lf = getLayoutInflater().from(this);
        view1 = lf.inflate(R.layout.layout_1, null);
        view2 = lf.inflate(R.layout.layout_2, null);
        view3 = lf.inflate(R.layout.layout_3, null);

        views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);

        tv1 = (TextView) findViewById(R.id.tv_1);
        tv2 = (TextView) findViewById(R.id.tv_2);
        tv3 = (TextView) findViewById(R.id.tv_3);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);

        titles = new ArrayList<String>();
        titles.add("title1");
        titles.add("title2");
        titles.add("title3");

        viewPager = (ViewPager) findViewById(R.id.viewpage);
        viewPager.setAdapter(new MyPageAdapter());
        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, new String[] { "item1",
                        "item2", "item3", "item4", "item5", "item6", "item7",
                        "item8", "item9" }));
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                System.out.println("----pageSelected-----");
                initAnimation(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void initAnimation(int moveto) {
        transAnima = new TranslateAnimation(currentIndex * width, moveto
                * width, 0, 0);
        transAnima.setFillAfter(true);
        transAnima.setDuration(300);
        line.startAnimation(transAnima);
        currentIndex = moveto;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == tv1) {
            currentIndex = 0;
        }

        if (v == tv2) {
            currentIndex = 1;
        }

        if (v == tv3) {
            currentIndex = 2;
        }
        viewPager.setCurrentItem(currentIndex);
        // initAnimation(currentIndex);
    }

    private class MyPageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView(views.get(position));
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return super.getItemPosition(object);
        }

        /*@Override
        public CharSequence getPageTitle(int position) {
            // TODO Auto-generated method stub
            return titles.get(position);
        }
*/
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(views.get(position));
            return views.get(position);
        }

    }

}
