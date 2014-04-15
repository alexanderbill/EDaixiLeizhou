package com.example.edaixi.UserCenter;

import com.example.edaixi.BaseFragment;
import com.example.edaixi.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class InviteCenter extends BaseFragment implements OnClickListener {

    public InviteCenter() {
        super(R.string.title_invites);
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
        LinearLayout view = (LinearLayout) mInflater.inflate(R.layout.detail_invite, null, false);
        return view;
    }
}
