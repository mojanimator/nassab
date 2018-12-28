package com.mmr.nassab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mmr.nassab.Util.NetUtils;
import com.mmr.nassab.Util.Utils;

/**
 * Created by Mojtaba Rajabi on 11/12/2018.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_username;
    private EditText et_password;
    private SharedPreferences sharedpreferences;
    private String response;
    private String username, password;
    SharedPreferences.Editor editor;

    private Button btnRegister, btnLogin;
    Intent intent;
    private View view;
    NetUtils netUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        view = this.getWindow().getDecorView().findViewById(R.id.layout_login);
        Utils.overrideFonts(this, view);
        netUtils = new NetUtils(this);

        intent = new Intent();

        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                netUtils.registerEditLoginLogout("login", username, password, "", "", "", "");

                break;
            case R.id.btn_register:
                intent.setClass(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
        }
    }
}
