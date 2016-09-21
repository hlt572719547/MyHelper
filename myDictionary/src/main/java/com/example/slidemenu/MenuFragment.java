package com.example.slidemenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mydictionary.R;


public class MenuFragment extends Fragment implements OnClickListener {

    private View inflate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.layout_menu, null);
        inflate.findViewById(R.id.relative1).setOnClickListener(this);
        inflate.findViewById(R.id.relative2).setOnClickListener(this);
        inflate.findViewById(R.id.relative3).setOnClickListener(this);
        inflate.findViewById(R.id.relative4).setOnClickListener(this);
        inflate.findViewById(R.id.relative5).setOnClickListener(this);
        return inflate;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.relative1:
            Toast.makeText(getActivity(), "item +1 被点击", Toast.LENGTH_SHORT)
                    .show();
            break;
        case R.id.relative2:
            Toast.makeText(getActivity(), "item +2 被点击", Toast.LENGTH_SHORT)
                    .show();
            break;
        case R.id.relative3:
            Toast.makeText(getActivity(), "item +3 被点击", Toast.LENGTH_SHORT)
                    .show();
            break;
        case R.id.relative4:
            Toast.makeText(getActivity(), "item +4 被点击", Toast.LENGTH_SHORT)
                    .show();
            break;
        case R.id.relative5:
            Toast.makeText(getActivity(), "item +5 被点击", Toast.LENGTH_SHORT)
                    .show();
            break;

        default:
            break;
        }
    }
}
