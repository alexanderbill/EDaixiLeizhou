package com.example.edaixi;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public class ShowFragmentOperation {
    public enum Type {
        Replace,
        Add
    }

    public Fragment fragment;
    public final Type type;
    /**
     * transition type, from FragmentTransaction. pass TRANSIT_UNSET to
     * get default behavior.
     */
    public final int transition;

    public ShowFragmentOperation(Fragment fragment) {
        this(fragment, Type.Replace, FragmentTransaction.TRANSIT_UNSET);
    }

    public ShowFragmentOperation(Fragment fragment, Type type) {
        this(fragment, type, FragmentTransaction.TRANSIT_UNSET);
    }

    public ShowFragmentOperation(Fragment fragment, Type type, int transition) {
        this.fragment = fragment;
        this.type = type;
        this.transition = transition;
    }
}
