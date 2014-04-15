package com.example.edaixi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class BaseFragment extends Fragment implements OnClickListener{
    private static final String TAG = "BaseFragmet";
    protected LinearLayout mContainer;
    protected LayoutInflater mInflater;
    protected int mTitleId;

    protected BaseFragment(int titleId) {
        mTitleId = titleId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        LinearLayout v = (LinearLayout)inflater.inflate(R.layout.linearlayout, container, false);
        ViewStub vs = (ViewStub)v.findViewById(R.id.bottom_navigation_bar_stub);
        vs.inflate();

        v.findViewById(R.id.back).setOnClickListener(this);
        v.findViewById(R.id.call).setOnClickListener(this);
        TextView tv = (TextView)v.findViewById(R.id.title);
        tv.setText(getResources().getText(mTitleId));

        mContainer = (LinearLayout) v.findViewById(R.id.total);
        View upperView = getUpperView();
        if (upperView != null) {
            mContainer.addView(upperView);
        }

        return v;
    }

    protected abstract View getUpperView();
    
    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        final int viewId = view.getId();
        if (viewId == R.id.back) {
            Log.d(TAG, "back");
        } else if (viewId == R.id.call) {
            Log.d(TAG, "call");
        }
    }
}
