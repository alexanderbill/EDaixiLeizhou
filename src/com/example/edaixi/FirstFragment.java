package com.example.edaixi;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FirstFragment extends BaseFragment implements AndroidHttpRequest.Listener  {

    public FirstFragment() {
        super(R.string.first);
    }

    private final static String TAG = "FirstFragment";
    private AndroidHttpRequest mRequest;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final String URL = "http://www.baidu.com";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        /*mRequest = new AndroidHttpRequest(SystemUtils.getActivity(), URL);
        mRequest.setListener(this, null);
        mRequest.setBody("");
        mRequest.start();*/
        payload(null, 0, 0);

        return v;
    }

    @Override
    public void connected(int statusCode, String statusString, Header[] headers) {
        // TODO Auto-generated method stub
        Log.d(TAG, "connected:" + statusCode + " " + statusString);
    }

    @Override
    public void payload(byte[] data1, int offset, int length) {
        // TODO Auto-generated method stub
        JSONArray data = FileUtils.openAssetFileJSONArray("jsonProductTest.json",
                "products");
        if (data != null) {
            for (int i = 0; i < data.length(); i++) {
                try {
                    JSONObject o = data.getJSONObject(i);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    if (i % 2 == 1) {
                         mContainer.addView(getInnerView());
                    } else if (i == data.length() - 1) {
                        mContainer.addView(getInnerView());
                    }
                }
            }
        }
        Log.d(TAG, "payload " + data);
    }

    @Override
    protected View getUpperView() {
        final ImageView iv = (ImageView) mInflater.inflate(
                R.layout.layout1_up, null);
        return iv;
    }

    protected View getInnerView() {
        final LinearLayout linearLayout = (LinearLayout) mInflater.inflate(
                R.layout.label1_item, null);
        ImageView text = (ImageView) linearLayout.findViewById(R.id.image1);
        text.setImageResource(R.drawable.clothes1);
        text = (ImageView) linearLayout.findViewById(R.id.image2);
        text.setImageResource(R.drawable.shoes1);
        return linearLayout;
    }

    @Override
    public void error(int code, String reason) {
        // TODO Auto-generated method stub
        Log.d(TAG, "error:" + code + " " + reason);
    }
}
