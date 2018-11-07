package com.silvermoon.crud_application.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.silvermoon.crud_application.R;
import com.silvermoon.crud_application.util.DataBaseHelper;
import com.silvermoon.crud_application.db.UserTable;
import com.silvermoon.crud_application.util.PreferenceConnector;

import java.util.ArrayList;
import java.util.List;

import static com.silvermoon.crud_application.util.Util.make_toast;

public class UserActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, LocationListener {

    private TextView first_nameTxt, last_nameTxt, mobile_numberTxt, email_idTxt, locationTxt, addressTxt, updateBtn, deleteBtn, logoutBtn, show_result;
    private LinearLayout user_layout;
    private String UserId, current_location = "", preferenceId;
    private int REQUEST_LOCATION = 1;
    private LocationManager locationManager;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        init();
        UserId = PreferenceConnector.readString(this, PreferenceConnector.USERID, "");
        if (!UserId.equals("")) {
            set_value(new DataBaseHelper().getSingleRecord(UserId));
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


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
                    PreferenceConnector.clear(this);
                    Toast.makeText(this, "User Deleted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, RegistrationActivity.class);
                    startActivity(intent);
                    onBackPressed();
                }
                break;
            }
            case R.id.show_result: {
                if (!UserId.equals("")) {
                    user_layout.setVisibility(View.VISIBLE);
                    show_result.setVisibility(View.GONE);

                }
                break;

            }
            case R.id.logout: {
                PreferenceConnector.clear(this);
                Intent intent = new Intent(this, RegistrationActivity.class);
                startActivity(intent);
                onBackPressed();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng new_location = get_latLong();
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(new_location));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new_location));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new_location, 15.0f));
        new DataBaseHelper().update_location(UserId, current_location);
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
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        make_toast(this, "Please enable your Location");
        {
            if (!current_location.equals("")) {
                mapFragment.getMapAsync(this);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        check_permission();
    }

    private void init() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
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
        logoutBtn = findViewById(R.id.logout);
        show_result.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        user_layout.setVisibility(View.GONE);
    }

    private LatLng get_latLong() {
        String[] latlong = current_location.split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);
        return new LatLng(latitude, longitude);
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


    private void set_value(ArrayList<UserTable> userData) {
        if (!userData.isEmpty()) {
            first_nameTxt.setText(userData.get(0).first_name);
            last_nameTxt.setText(userData.get(0).last_name);
            mobile_numberTxt.setText(userData.get(0).mobile_number);
            email_idTxt.setText(userData.get(0).email_id);
            locationTxt.setText(userData.get(0).location);
            addressTxt.setText(userData.get(0).address);
            current_location = userData.get(0).current_location;
        }

    }

}

