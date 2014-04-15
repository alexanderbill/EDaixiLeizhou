package com.example.edaixi;

import com.example.edaixi.UserCenter.AddressCenter;
import com.example.edaixi.UserCenter.InviteCenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SecondFragment extends BaseFragment {

    public SecondFragment() {
        super(R.string.second);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mContainer.addView(getInnerView(inflater, R.string.addresses, R.drawable.addressimage, AddressCenter.class));
        mContainer.addView(getInnerView(inflater, R.string.invites, R.drawable.inviteimage, InviteCenter.class));
        mContainer.addView(getInnerView(inflater, R.string.logout, R.drawable.logoutimage, new View.OnClickListener() {
                    public void onClick(View v) {
                        EventDispatcher.post(new AppExit(true));
                    }
                }));

        return v;
    }

    @Override
    protected View getUpperView() {
        final TextView iv = (TextView) mInflater.inflate(
                R.layout.layout2_up, null);
        iv.setText(SystemUtils.getId());
        return iv;
    }

    protected View getInnerView(LayoutInflater inflater, int textRes, int imageRes, final Class<?> a) {
        final LinearLayout linearLayout = (LinearLayout) inflater.inflate(
                R.layout.label2_item, null);
        ImageView image = (ImageView) linearLayout.findViewById(R.id.image);
        image.setImageResource(imageRes);
        TextView text = (TextView) linearLayout.findViewById(R.id.text);
        text.setText(getResources().getText(textRes));
        Button go = (Button) linearLayout.findViewById(R.id.go);
        go.setBackgroundResource(R.drawable.go2);
        OnClickListener onclick = new View.OnClickListener() {
            public void onClick(View v) {
                Fragment fragment = null;
                try {
                    fragment = (Fragment) a.newInstance();
                } catch (java.lang.InstantiationException e) {
                    return;
                } catch (IllegalAccessException e) {
                    return;
                }
                EventDispatcher.post(new ShowFragmentOperation(fragment,
                        ShowFragmentOperation.Type.Add));
            }
        };
        go.setOnClickListener(onclick);
        linearLayout.findViewById(R.id.total).setOnClickListener(onclick);
        return linearLayout;
    }

    private View getInnerView(LayoutInflater inflater, int textRes, int imageRes,
            final OnClickListener listener) {
        final LinearLayout linearLayout = (LinearLayout) inflater.inflate(
                R.layout.label2_item, null);
        ImageView image = (ImageView) linearLayout.findViewById(R.id.image);
        image.setImageResource(imageRes);
        TextView text = (TextView) linearLayout.findViewById(R.id.text);
        text.setText(getResources().getText(textRes));
        Button go = (Button) linearLayout.findViewById(R.id.go);
        go.setBackgroundResource(R.drawable.go2);
        go.setOnClickListener(listener);
        linearLayout.setOnClickListener(listener);
        return linearLayout;
    }
}
