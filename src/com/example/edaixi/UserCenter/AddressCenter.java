package com.example.edaixi.UserCenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.edaixi.FileUtils;
import com.example.edaixi.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddressCenter extends Fragment implements OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout v = (LinearLayout)inflater.inflate(R.layout.fragment_container, container, false);
        ViewStub vs = (ViewStub)v.findViewById(R.id.bottom_navigation_bar_stub);
        vs.inflate();
        v.findViewById(R.id.back).setOnClickListener(this);
        v.findViewById(R.id.call).setOnClickListener(this);
        TextView tv = (TextView) v.findViewById(R.id.title);
        tv.setText(R.string.title_addresses);

        ViewGroup contentContainer = (ViewGroup) v.findViewById(R.id.container);
        LinearLayout bookmarksView = (LinearLayout) inflater.inflate(R.layout.address, null, false);
        contentContainer.addView(bookmarksView);

        JSONArray data = FileUtils.openAssetFileJSONArray("jsonAddressTest.json",
                "addresses");
        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject o = data.getJSONObject(i);
                bookmarksView.addView(getView(inflater,
                        o.getString("city"),
                        o.getString("area"),
                        o.getString("address")));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return v;
    }

    private View getView(LayoutInflater inflater, String city, String area, String address) {
        final LinearLayout linearLayout = (LinearLayout) inflater.inflate(
                R.layout.address_item, null);
        TextView text = (TextView) linearLayout.findViewById(R.id.city);
        text.setText(city);
        text = (TextView) linearLayout.findViewById(R.id.area);
        text.setText(area);
        text = (TextView) linearLayout.findViewById(R.id.address);
        text.setText(address);
        OnClickListener onclick = new View.OnClickListener() {
            public void onClick(View v) {
                
            }
        };
        linearLayout.findViewById(R.id.total).setOnClickListener(onclick);
        return linearLayout;
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
}
