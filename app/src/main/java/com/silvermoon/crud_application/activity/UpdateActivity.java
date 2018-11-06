package com.silvermoon.crud_application.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.silvermoon.crud_application.R;
import com.silvermoon.crud_application.util.DataBaseHelper;
import com.silvermoon.crud_application.db.UserTable;

import java.util.ArrayList;
import java.util.List;

import static com.silvermoon.crud_application.util.Util.emailPattern;
import static com.silvermoon.crud_application.util.Util.make_toast;
import static com.silvermoon.crud_application.util.Util.states;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private EditText firstNameEText, lastNameEText, mobileEText, emailEText, addressEText, passwordEText, confirmPassEText;
    private Spinner locationSpinner;
    private TextView update;
    private String firstName, lastName, mobileNumber, emailId, location, address, password, confirmPassword;
    private String UserId;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            UserId = bundle.getString("UserId");
            set_value(new DataBaseHelper().getSingleRecord(UserId));
        }
    }

    private void init() {
        firstNameEText = findViewById(R.id.first_nameEt);
        lastNameEText = findViewById(R.id.last_nameEt);
        mobileEText = findViewById(R.id.mobile_numberEt);
        emailEText = findViewById(R.id.emailEt);
        addressEText = findViewById(R.id.addressEt);
        passwordEText = findViewById(R.id.passwordEt);
        confirmPassEText = findViewById(R.id.confirm_passwordEt);
        locationSpinner = findViewById(R.id.location_sp);
        update = findViewById(R.id.update);

        adapter = new ArrayAdapter(this, R.layout.simple_spinar_item, states);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        locationSpinner.setOnItemSelectedListener(this);
        update.setOnClickListener(this);

    }

    private void validation_check() {
        if (firstName.equals("")) {
            firstNameEText.setText("");
            make_toast(this, "Please enter your First Name");
        } else if (lastName.equals("")) {
            lastNameEText.setText("");
            make_toast(this, "Please enter your Last Name");
        } else if (mobileNumber.equals("") || mobileNumber.length() < 10) {
            mobileEText.setText("");
            make_toast(this, "Please provide a valid mobile number");
        } else if (!emailId.matches(emailPattern) || emailId.length() < 1) {
            emailEText.setText("");
            make_toast(this, "Please provide Correct email");
        } else if (address.equals("") || address.length() < 2) {
            addressEText.setText("");
            make_toast(this, "Please Enter Your Address");
        } else if (password.length() < 6 || password.length() > 18) {
            passwordEText.setText("");
            make_toast(this, "Password length should be min 6 and max 18");
        } else if (!confirmPassword.equals(password)) {
            confirmPassEText.setText("");
            make_toast(this, "Confirm Password should be same as password");
        } else {
            int userId = new DataBaseHelper().updateRecords(UserId, firstName, lastName, mobileNumber, emailId, location, address, password);
            Intent intent = new Intent(this, UserActivity.class);
            intent.putExtra("UserId", userId);
            startActivity(intent);
            onBackPressed();

        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.update) {
            firstName = firstNameEText.getText().toString().trim();
            lastName = lastNameEText.getText().toString().trim();
            mobileNumber = mobileEText.getText().toString().trim();
            emailId = emailEText.getText().toString().trim();
            address = addressEText.getText().toString().trim();
            password = passwordEText.getText().toString().trim();
            confirmPassword = confirmPassEText.getText().toString().trim();
            validation_check();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        location = states[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        location = states[0];
    }

    private void set_value(ArrayList<UserTable> userData) {
        if (!userData.isEmpty()) {
            firstNameEText.setText(userData.get(0).first_name);
            lastNameEText.setText(userData.get(0).last_name);
            mobileEText.setText(userData.get(0).mobile_number);
            emailEText.setText(userData.get(0).email_id);
            addressEText.setText(userData.get(0).address);
            passwordEText.setText(userData.get(0).password);
            confirmPassEText.setText(userData.get(0).password);
            location = userData.get(0).location;
            locationSpinner.setSelection(adapter.getPosition(location));
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
