package com.example.edaixi;

import com.example.edaixi.UserCenter.AddressCenter;

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

public class FourthFragment extends BaseFragment {
    public FourthFragment() {
        super(R.string.fourth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        mContainer.addView(getView(inflater, R.string.welcome,
                R.drawable.welcome, AddressCenter.class));
        mContainer.addView(getView(inflater, R.string.grade, R.drawable.grade,
                AddressCenter.class));
        mContainer.addView(getView(inflater, R.string.F_Q, R.drawable.fandq,
                AddressCenter.class));
        mContainer.addView(getView(inflater, R.string.protocal,
                R.drawable.protocal, AddressCenter.class));
        mContainer.addView(getView(inflater, R.string.feedback,
                R.drawable.feedback, AddressCenter.class));
        mContainer.addView(getView(inflater, R.string.upgrade,
                R.drawable.upgrade, AddressCenter.class));
        mContainer.addView(getView(inflater, R.string.about, R.drawable.about,
                AddressCenter.class));
        mContainer.addView(getView(inflater, R.string.blog, 0,
                AddressCenter.class));
        mContainer.addView(getView(inflater, R.string.wechat, 0,
                AddressCenter.class));
        mContainer.addView(getView(inflater, R.string.website,
                0, AddressCenter.class));
        mContainer.addView(getView(inflater, R.string.phone, 0,
                AddressCenter.class));
        return v;
    }

    private View getView(LayoutInflater inflater, int textRes, int imageRes,
            final Class<?> a) {
        final LinearLayout linearLayout = (LinearLayout) inflater.inflate(
                R.layout.label4_item, null);
        ImageView image = (ImageView) linearLayout.findViewById(R.id.image);
        if (imageRes == 0) {
            image.setVisibility(View.GONE);
        } else {
            image.setImageResource(imageRes);
        }
        TextView text = (TextView) linearLayout.findViewById(R.id.text);
        text.setText(getResources().getText(textRes));
        Button go = (Button) linearLayout.findViewById(R.id.go);
        go.setBackgroundResource(R.drawable.go4);
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

    @Override
    protected View getUpperView() {
        // TODO Auto-generated method stub
        return null;
    }
}
