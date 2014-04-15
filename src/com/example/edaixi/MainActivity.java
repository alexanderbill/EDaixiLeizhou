package com.example.edaixi;

import de.greenrobot.event.Subscribe;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MainActivity extends FragmentActivity {
    public static final String TAG = "MainActivity";
    private FragmentTabHost mTabHost;
    private EventHandler mEventHandler = new EventHandler();
    private final int[] mIndicatorTitle = { R.string.first, R.string.second,
            R.string.third, R.string.fourth };
    private final int[] mIndicatorImage = { R.drawable.home_icon,
            R.drawable.person_icon, R.drawable.orders_icon,
            R.drawable.more_icon };
    @SuppressWarnings("rawtypes")
    private final Class[] mIndicatorClass = { FirstFragment.class,
            SecondFragment.class, ThirdFragment.class, FourthFragment.class };

    private class EventHandler {
        @Subscribe
        public void onShowFragmentOperation(ShowFragmentOperation op) {
            handleShowFragmentOperation(op);
        }

        @Subscribe
        public void onExit(AppExit exit) {
            if (exit.mClear) {
                SystemUtils.saveLogin(null, null);
            }
            finish();
        }
    }

    private void handleShowFragmentOperation(ShowFragmentOperation op) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (op.transition != FragmentTransaction.TRANSIT_UNSET) {
            ft.setTransition(op.transition);
        } else {
            ft.setCustomAnimations(R.animator.fragment_enter,
                    R.animator.fragment_exit, R.animator.fragment_enter,
                    R.animator.fragment_exit);
        }
        switch (op.type) {
        case Replace:
            ft.replace(R.id.main_fragment_container, op.fragment);
            break;
        case Add:
            ft.add(R.id.main_fragment_container, op.fragment);
            break;
        }
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUtils.setActivity(this);
        setContentView(R.layout.activity_main);
        EventDispatcher.register(mEventHandler, EventDispatcher.Group.Main);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        for (int i = 0; i < mIndicatorTitle.length; i++) {
            View indicator = getIndicatorView(mIndicatorTitle[i],
                    mIndicatorImage[i], R.layout.indicator);
            mTabHost.addTab(
                    mTabHost.newTabSpec(
                            (String) getResources().getText(mIndicatorTitle[i]))
                            .setIndicator(indicator), mIndicatorClass[i], null);
        }

        mTabHost.getTabWidget().getChildAt(1).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!SystemUtils.checkLogin(1)) {
                    mTabHost.setCurrentTab(1);
                }
            }
        });
        mTabHost.getTabWidget().getChildAt(2).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!SystemUtils.checkLogin(2)) {
                    mTabHost.setCurrentTab(2);
                }
            }
        });
    }

    private View getIndicatorView(int txtId, int imageId, int layoutId) {
        View v = getLayoutInflater().inflate(layoutId, null);
        ImageView iv = (ImageView) v.findViewById(R.id.tabImage);
        iv.setImageResource(imageId);
        return v;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        EventDispatcher.unregister(mEventHandler);
        mTabHost = null;
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg2 != null) {
            int pos = arg2.getExtras().getInt("pos");
            mTabHost.setCurrentTab(pos);
        }
    }
}
