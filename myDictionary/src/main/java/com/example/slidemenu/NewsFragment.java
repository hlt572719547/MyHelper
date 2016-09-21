package com.example.slidemenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mydictionary.R;

public class NewsFragment extends Fragment {
    private View view;
    private ListView listView;
    private MyListViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment_b, container, false);
        listView = (ListView) view.findViewById(R.id.listView1);
        adapter=new MyListViewAdapter(getActivity());
        listView.setAdapter(adapter);
        return view;
    }
}
