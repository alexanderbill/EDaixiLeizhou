package com.example.edaixi.UserCenter;

import com.example.edaixi.BaseFragment;
import com.example.edaixi.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class CouponsCenter extends BaseFragment implements OnClickListener {

    public CouponsCenter() {
        super(R.string.title_coupons);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        return v;
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int id = view.getId();
        if (id == R.id.back) {
            dismiss();
        } else if (id == R.id.call) {
            dismiss();
        }
    }

    void dismiss() {
        getFragmentManager().popBackStack();
    }

    @Override
    protected View getUpperView() {
        // TODO Auto-generated method stub
        return null;
    }
}
