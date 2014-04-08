package com.example.edaixi;

import android.os.Looper;


public class SystemUtils {
    private static MainActivity sActivity;

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

}
