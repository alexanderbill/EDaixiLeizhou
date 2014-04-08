package com.example.edaixi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ThirdFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout v = (LinearLayout)inflater.inflate(R.layout.linearlayout3, container, false);
        ViewStub vs = (ViewStub)v.findViewById(R.id.bottom_navigation_bar_stub);
        vs.inflate();

        TextView tv = (TextView)v.findViewById(R.id.title);
        tv.setText(getResources().getText(R.string.third));
        return v;
    }
}
