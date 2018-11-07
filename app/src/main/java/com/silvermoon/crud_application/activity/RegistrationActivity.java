package com.silvermoon.crud_application.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.silvermoon.crud_application.db.UserTable;
import com.silvermoon.crud_application.util.DataBaseHelper;
import com.silvermoon.crud_application.util.PreferenceConnector;

import java.util.ArrayList;

import static com.silvermoon.crud_application.util.Util.emailPattern;
import static com.silvermoon.crud_application.util.Util.make_toast;
import static com.silvermoon.crud_application.util.Util.states;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, LocationListener {

    private EditText firstNameEText, lastNameEText, mobileEText, emailEText, addressEText, passwordEText, confirmPassEText;
    private Spinner locationSpinner;
    private TextView submit, login;
    private String firstName, lastName, mobileNumber, emailId, location, address, password, confirmPassword, current_location = "", preferenceId;
    private int REQUEST_LOCATION = 1;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!PreferenceConnector.readString(this, PreferenceConnector.USERID, "").equals("")) {
            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_registration);
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            check_permission();
            init();
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
        submit = findViewById(R.id.submit);
        login = findViewById(R.id.login);
        submit.setOnClickListener(this);
        login.setOnClickListener(this);

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.simple_spinar_item, states);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);
        locationSpinner.setOnItemSelectedListener(this);
        location = states[0];

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
            ArrayList<UserTable> userData = new DataBaseHelper().check_user_exist(emailId);
            {
                if (!userData.isEmpty()) {
                    if (userData.get(0).email_id.equals(emailId)) {
                        make_toast(this, "User Already Exist.Please Login");
                    } else {
                        login_user();
                    }
                } else {
                    login_user();
                }

            }


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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit: {
                firstName = firstNameEText.getText().toString().trim();
                lastName = lastNameEText.getText().toString().trim();
                mobileNumber = mobileEText.getText().toString().trim();
                emailId = emailEText.getText().toString().trim();
                address = addressEText.getText().toString().trim();
                password = passwordEText.getText().toString().trim();
                confirmPassword = confirmPassEText.getText().toString().trim();
                validation_check();
                break;
            }
            case R.id.login: {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }

    private void login_user() {
        int UserId = new DataBaseHelper().insertDate(firstName, lastName, mobileNumber, emailId, location, address, password, current_location);
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("UserId", UserId);
        PreferenceConnector.writeString(this, PreferenceConnector.USERID, String.valueOf(UserId));
        startActivity(intent);
        finish();
    }

    private void check_permission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                check_permission();

            } else {
                make_toast(this, "Permission declined. Please enable it to get current location");
                current_location = "";
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        current_location = location.getLatitude() + "," + location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        make_toast(this, "location disabled. Please enable your location");

    }
}
