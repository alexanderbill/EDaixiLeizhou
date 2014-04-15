package com.example.edaixi.UserCenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.edaixi.BaseFragment;
import com.example.edaixi.FileUtils;
import com.example.edaixi.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddressCenter extends BaseFragment implements OnClickListener {

    public AddressCenter() {
        super(R.string.title_addresses);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        JSONArray data = FileUtils.openAssetFileJSONArray("jsonAddressTest.json",
                "addresses");
        if (data != null) {
            for (int i = 0; i < data.length(); i++) {
                try {
                    JSONObject o = data.getJSONObject(i);
                    mContainer.addView(getView(inflater,
                            o.getString("city"), o.getString("area"),
                            o.getString("address")));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return v;
    }

    private View getView(LayoutInflater inflater, String city, String area, String address) {
        final LinearLayout linearLayout = (LinearLayout) inflater.inflate(
                R.layout.address_item, null);
        ImageView image = (ImageView) linearLayout.findViewById(R.id.edit);
        image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
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

    @Override
    protected View getUpperView() {
        // TODO Auto-generated method stub
        return null;
    }
}
