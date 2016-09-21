package com.example.stickyheader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mydictionary.R;

/**
 * Created by Administrator on 2016/6/3.
 */
public class MainPageFragment extends Fragment {

    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        setupDemoRecyclerView();
        return view;
    }

    void setupDemoRecyclerView() {
        DemoModel[] demos = {
                new DemoModel(getString(R.string.demo_list_item_addressbook_title),
                        getString(R.string.demo_list_item_addressbook_description),
                        AddressBookDemoActivity.class),

                new DemoModel(getString(R.string.demo_list_item_callbacks_title),
                        getString(R.string.demo_list_item_callbacks_description),
                        HeaderCallbacksDemoActivity.class),

                new DemoModel(getString(R.string.demo_list_item_sections_title),
                        getString(R.string.demo_list_item_sections_description),
                        SectioningAdapterDemoActivity.class)
        };

        recyclerView.setAdapter(new DemoAdapter(getContext(), demos, new DemoAdapter.ItemClickListener() {
            @Override
            public void onItemClick(DemoModel demoModel) {
                startActivity(new Intent(getActivity(), demoModel.activityClass));
            }
        }));
        recyclerView.setLayoutManager(new StickyHeaderLayoutManager());
    }
}
