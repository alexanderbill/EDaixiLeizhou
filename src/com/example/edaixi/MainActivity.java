package com.example.edaixi;

import de.greenrobot.event.Subscribe;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends FragmentActivity {
    public static final String TAG = "MainActivity";
    private String mId;
    private String mToken;
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
                saveLogin(null, null);
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
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mId = (String) bundle.get(LoginActivity.ID);
        mToken = (String) bundle.get(LoginActivity.TOKEN);
        saveLogin(mId, mToken);
        Log.d(TAG, "Login:" + mId + " " + mToken);
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
    }

    private void saveLogin(String id, String token) {
        SharedPreferences pref = getSharedPreferences(LoginActivity.TAG, MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(LoginActivity.ID, id);
        editor.putString(LoginActivity.TOKEN, token);
        EditorUtils.fastCommit(editor);
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
}
