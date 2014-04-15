package com.example.edaixi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ThirdFragment extends BaseFragment {
    public ThirdFragment() {
        super(R.string.third);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        return v;
    }

    @Override
    protected View getUpperView() {
        // TODO Auto-generated method stub
        return null;
    }
}
