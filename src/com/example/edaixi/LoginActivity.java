package com.example.edaixi;

import com.example.edaixi.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoginActivity extends FragmentActivity implements OnClickListener {

    public static final String TAG = "LoginActivity";
    public static final String ID = "LoginID";
    public static final String TOKEN = "TOKEN";
    private EditText mPhoneText;
    private EditText mPassText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        if (checkLogin()) {
            return;
        }
        ViewStub vs = (ViewStub) findViewById(R.id.bottom_navigation_bar_stub);
        vs.inflate();
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.call).setVisibility(View.INVISIBLE);
        TextView tv = (TextView) findViewById(R.id.title);
        tv.setText(R.string.app_name);

        ViewGroup contentContainer = (ViewGroup) findViewById(R.id.container);
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

    private boolean checkLogin() {
        SharedPreferences pref = getSharedPreferences(TAG, MODE_PRIVATE);
        String id = pref.getString(ID, null);
        String token = pref.getString(TOKEN, null);
        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(token)) {
            startMainActivity(id, token);
            return true;
        }
        return false;
    }

    private void startMainActivity(String id, String token) {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra(ID, id);
        intent.putExtra(TOKEN, token);
        startActivity(intent);
        finish();
        overridePendingTransition(R.animator.non_fade, R.animator.non_fade);
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
            startMainActivity(id, token);
        }
    }
}
