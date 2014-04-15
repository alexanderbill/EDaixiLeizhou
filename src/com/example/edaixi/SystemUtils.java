package com.example.edaixi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Looper;
import android.text.TextUtils;


public class SystemUtils {
    private static MainActivity sActivity;
    private static final String TAG = "LoginActivity";
    private static final String ID = "LoginID";
    private static final String TOKEN = "TOKEN";
    private static String mId;
    private static String mToken;

    public static MainActivity getActivity() {
        return sActivity;
    }

    public static void setActivity(MainActivity activity) {
        sActivity = activity;
    }

    public static void mainThreadEnforce(String checkpoint) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException(checkpoint + " accessed from non-main thread"
                    + Looper.myLooper());
        }
    }

    public static boolean checkLogin(int pos) {
        SharedPreferences pref = sActivity.getSharedPreferences(TAG, 0);
        mId = pref.getString(ID, null);
        mToken = pref.getString(TOKEN, null);
        if (TextUtils.isEmpty(mId) || TextUtils.isEmpty(mToken)) {
            startLoginActivity(pos);
            return true;
        }
        return false;
    }

    public static void saveLogin(String id, String token) {
        SharedPreferences pref = sActivity.getSharedPreferences(TAG, 0);
        Editor editor = pref.edit();
        mId = id;
        mToken = token;
        editor.putString(ID, mId);
        editor.putString(TOKEN, mToken);
        EditorUtils.fastCommit(editor);
    }

    public static String getId() {
        return mId;
    }

    private static void startLoginActivity(int pos) {
        Intent intent = new Intent(sActivity, LoginActivity.class);
        intent.putExtra("pos", pos);
        sActivity.startActivityForResult(intent, 1);
        sActivity.overridePendingTransition(R.animator.non_fade, R.animator.non_fade);
    }
}
