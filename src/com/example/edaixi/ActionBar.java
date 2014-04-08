package com.example.edaixi;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class ActionBar extends LinearLayout implements OnClickListener {

    private final static String TAG = "ActionBar";

    public ActionBar(Context context) {
        super(context);
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.call).setOnClickListener(this);
    }

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
