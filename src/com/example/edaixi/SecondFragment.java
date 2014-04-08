package com.example.edaixi;

import com.example.edaixi.UserCenter.AddressCenter;
import com.example.edaixi.UserCenter.CardCenter;
import com.example.edaixi.UserCenter.CouponsCenter;
import com.example.edaixi.UserCenter.InviteCenter;
import com.example.edaixi.UserCenter.OrdersCenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SecondFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout v = (LinearLayout)inflater.inflate(R.layout.linearlayout2, container, false);
        ViewStub vs = (ViewStub)v.findViewById(R.id.bottom_navigation_bar_stub);
        vs.inflate();

        LinearLayout scrollview = (LinearLayout) v
                .findViewById(R.id.scrollview);
        scrollview.addView(getView(inflater, R.string.addresses, R.drawable.addressimage, AddressCenter.class));
        scrollview.addView(getView(inflater, R.string.orders, R.drawable.ordersimage, OrdersCenter.class));
        scrollview.addView(getView(inflater, R.string.cards, R.drawable.cardimage, CardCenter.class));
        scrollview.addView(getView(inflater, R.string.coupons, R.drawable.couponsimage, CouponsCenter.class));
        scrollview.addView(getView(inflater, R.string.invites, R.drawable.inviteimage, InviteCenter.class));
        scrollview.addView(getView(inflater, R.string.logout, R.drawable.logoutimage, new View.OnClickListener() {
                    public void onClick(View v) {
                        EventDispatcher.post(new AppExit(true));
                    }
                }));

        TextView tv = (TextView)v.findViewById(R.id.title);
        tv.setText(getResources().getText(R.string.second));
        return v;
    }

    private View getView(LayoutInflater inflater, int textRes, int imageRes, final Class<?> a) {
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

    private View getView(LayoutInflater inflater, int textRes, int imageRes,
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
