package com.example.edaixi.UserCenter;

import com.example.edaixi.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CouponsCenter extends Fragment implements OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout v = (LinearLayout)inflater.inflate(R.layout.fragment_container, container, false);
        ViewStub vs = (ViewStub)v.findViewById(R.id.bottom_navigation_bar_stub);
        vs.inflate();
        v.findViewById(R.id.back).setOnClickListener(this);
        v.findViewById(R.id.call).setOnClickListener(this);
        TextView tv = (TextView) v.findViewById(R.id.title);
        tv.setText(R.string.title_coupons);

        ViewGroup contentContainer = (ViewGroup) v.findViewById(R.id.container);
        LinearLayout bookmarksView = (LinearLayout) inflater.inflate(R.layout.detail_coupons, null, false);
        contentContainer.addView(bookmarksView);

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
}
