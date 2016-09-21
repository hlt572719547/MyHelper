package com.example.stickyheader;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;


/**
 * Created by shamyl on 4/26/16.
 */
public class SectioningAdapterDemoActivity extends DemoActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(new SimpleDemoAdapter(5, 5));
	}
}
