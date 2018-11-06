package com.silvermoon.crud_application.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.silvermoon.crud_application.R;
import com.silvermoon.crud_application.util.DataBaseHelper;
import com.silvermoon.crud_application.db.UserTable;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView first_nameTxt, last_nameTxt, mobile_numberTxt, email_idTxt, locationTxt, addressTxt, updateBtn, deleteBtn, show_result;
    private LinearLayout user_layout;
    private String UserId;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        init();

    }

    private void init() {
        user_layout = findViewById(R.id.user_linear_layout);
        show_result = findViewById(R.id.show_result);
        first_nameTxt = findViewById(R.id.first_nameTv);
        last_nameTxt = findViewById(R.id.last_nameTv);
        mobile_numberTxt = findViewById(R.id.mobile_numberTv);
        email_idTxt = findViewById(R.id.emailTv);
        locationTxt = findViewById(R.id.locationTv);
        addressTxt = findViewById(R.id.addressTv);
        updateBtn = findViewById(R.id.update);
        deleteBtn = findViewById(R.id.delete);
        show_result.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        user_layout.setVisibility(View.INVISIBLE);
    }


    private void set_value(ArrayList<UserTable> userData) {
        if (!userData.isEmpty()) {
            first_nameTxt.setText(userData.get(0).first_name);
            last_nameTxt.setText(userData.get(0).last_name);
            mobile_numberTxt.setText(userData.get(0).mobile_number);
            email_idTxt.setText(userData.get(0).email_id);
            locationTxt.setText(userData.get(0).location);
            addressTxt.setText(userData.get(0).address);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.update: {
                {
                    if (!UserId.equals("")) {
                        Intent intent = new Intent(this, UpdateActivity.class);
                        intent.putExtra("UserId", UserId);
                        startActivity(intent);
                        onBackPressed();
                    }
                }
                break;
            }
            case R.id.delete: {
                if (!UserId.equals("")) {
                    new DataBaseHelper().deleteRecord(UserId);
                    UserId = "";
                    Toast.makeText(this, "User Deleted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, RegistrationActivity.class);
                    startActivity(intent);
                    onBackPressed();
                }
                break;
            }
            case R.id.show_result: {
                bundle = getIntent().getExtras();
                if (bundle != null) {
                    UserId = String.valueOf(bundle.getInt("UserId"));
                    set_value(new DataBaseHelper().getSingleRecord(UserId));
                    user_layout.setVisibility(View.VISIBLE);
                    show_result.setVisibility(View.GONE);
                }

            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
