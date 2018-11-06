package com.silvermoon.crud_application.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.silvermoon.crud_application.R;
import com.silvermoon.crud_application.db.UserTable;
import com.silvermoon.crud_application.util.DataBaseHelper;

import java.util.ArrayList;

import static com.silvermoon.crud_application.util.Util.make_toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEText, passEText;
    private TextView loginTv;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        emailEText = findViewById(R.id.emailEt);
        passEText = findViewById(R.id.passwordEt);
        loginTv = findViewById(R.id.login);
        loginTv.setOnClickListener(this);
    }

    private void check_user() {
        email = emailEText.getText().toString().trim();
        password = passEText.getText().toString().trim();
        ArrayList<UserTable> userData = new DataBaseHelper().check_user_exist(email);
        if (userData.get(0).email_id.equals(email) && userData.get(0).password.equals(password)) {
            Intent intent = new Intent(this, UserActivity.class);
            intent.putExtra("UserId", userData.get(0).getId());
            startActivity(intent);
            finish();
        } else {
            make_toast(this, "User doesn't exist");
            onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        check_user();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        finish();
    }
}
