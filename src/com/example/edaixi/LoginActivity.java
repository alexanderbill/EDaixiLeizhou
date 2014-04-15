package com.example.edaixi;

import com.example.edaixi.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class LoginActivity extends FragmentActivity implements OnClickListener {

    private EditText mPhoneText;
    private EditText mPassText;
    private int mPos;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linearlayout);

        Intent intent = getIntent();
        mPos = intent.getExtras().getInt("pos");
        ViewStub vs = (ViewStub) findViewById(R.id.bottom_navigation_bar_stub);
        vs.inflate();
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.call).setVisibility(View.INVISIBLE);
        TextView tv = (TextView) findViewById(R.id.title);
        tv.setText(R.string.app_name);

        LinearLayout contentContainer = (LinearLayout) findViewById(R.id.total);
        contentContainer.setPadding(0, 0, 0, 0);
        ScrollView scrollview = (ScrollView) contentContainer.getParent();
        scrollview.setBackgroundResource(R.color.main_bg);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        LinearLayout loginView = (LinearLayout) inflater.inflate(R.layout.login, null, false);

        Button passButton = (Button) loginView.findViewById(R.id.getpass);
        passButton.setOnClickListener(this);
        Button loginButton = (Button) loginView.findViewById(R.id.login);
        loginButton.setOnClickListener(this);

        mPhoneText = (EditText) loginView.findViewById(R.id.inputnumber);
        mPassText = (EditText) loginView.findViewById(R.id.inputpass);
        contentContainer.addView(loginView);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        int id = view.getId();
        if (id == R.id.back) {
            finish();
        } else if (id == R.id.getpass) {
            String phone = mPhoneText.getText().toString();
            submitPhone(phone);
        } else if (id == R.id.login) {
            String phone = mPhoneText.getText().toString();
            String pass = mPassText.getText().toString();
            checkLogin(phone, pass);
        }
    }

    private void submitPhone(String phone) {

    }

    private void checkLogin(String id, String token) {
        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(token)) {
            Intent intent = new Intent();
            intent.putExtra("pos", mPos);
            setResult(Activity.RESULT_OK, intent);
            finish();
            SystemUtils.saveLogin(id, token);
        } else {
            setResult(Activity.RESULT_OK, null);
        }
        finish();
    }
}
