package com.team404.trackmyday;
//https://track-my-day-a2c6f.firebaseio.com/

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Carl Carter on 10/21/2016.
 */


public class Location extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,
        com.google.android.gms.location.LocationListener {

    private android.location.Location userlocation;
    private String userId;
    private Button track_btn;
    private TextView coord_view, coord_view2;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String dateString, time;
    private double latitude, longitude;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_retun);

        track_btn = (Button) findViewById(R.id.track_btn);
        coord_view = (TextView) findViewById(R.id.coord_view);
        coord_view2 = (TextView) findViewById(R.id.coord_view2); //Button Creation

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                userlocation=location;
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                coord_view.append("\n " + latitude + " " + longitude); //Updates when GPS pings new location
            }



            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
            }
            return;
        } else {
            configureButton();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;
        }
    }

    private void configureButton() {
        track_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Location.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Location.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");           //Collect Date and Time for location
                dateString = dateFormat.format(new Date());
                Date date = new Date();
                File path = getApplicationContext().getFilesDir();

                time = date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();

                coord_view2.setText("Latitude: "+latitude+"\nLongitude: "+longitude+"\nDate: "+dateString+"\nTime: "+time);

                if(userlocation==null){
                    return;
                }

                saveLocDB();//Saves Ping data to database

            }
        });

    }

    private void saveLocDB(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");           //Collect Date and Time for location
        dateString = dateFormat.format(new Date());
        String email = "Group";

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            email = extras.getString("Account");
        }

        final DatabaseReference myRef = database.getReference().child("Users").child(email);



        userId = myRef.push().getKey();
        UserLocationModel users = new UserLocationModel(latitude,longitude,dateString,time);
        myRef.child(userId).setValue(users);





//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//               long pingCount = dataSnapshot.getChildrenCount();
//                //Data pushed to Firebase
//                Map<String, Object> newping = new HashMap<String, Object>();
//                newping.put(String.valueOf(pingCount),latitude+", "+longitude+", "+dateString+", "+time);
//                myRef.updateChildren(newping);
//                Log.d("Children", String.valueOf(pingCount));
//
//
//
//
//
//
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });



    }



    private String retrieveLocDB(){
        //Will implement retrieve after save works correctly
        return "PlaceHodlder";
    }
    //returns latitude
    private double getLatitude() {
        return latitude;
    }

    //returns longitude
    private double getLongitude() {
        return longitude;
    }


    @Override
    protected void onResume() {
        super.onResume();
        CheckGPS();
    }

    private void CheckGPS() {

        LocationManager locationManager = (LocationManager) Location.this
                .getSystemService(Location.this.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//
//
            if (mGoogleApiClient != null) {
                if (!mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            } else if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(Location.this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this).build();

                mGoogleApiClient.connect();
            }

        } else {
            showGPSDisabledAlertToUser();

        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {

            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000 * 5);
            locationRequest.setFastestInterval(5);
            // locationClient.requestLocationUpdates(request,
            // listener)ionUpdates(locationRequest,
            // DriverLocationFragment.this);
            //       locationClient.requestLocationUpdates(locationRequest,
            //   DriverLocationFragment.this);
            if (ActivityCompat.checkSelfPermission(Location.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, locationRequest, this);

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Location.this);
        alertDialogBuilder
                .setMessage(
                        getResources().getString(R.string.gps_message))
                // "GPS & Google Location Services must be clicked on to use Stop2Text")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent callGPSSettingIntent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);

                        dialog.cancel();

                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(android.location.Location location) {


        userlocation=location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        coord_view.append("\n " + latitude + " " + longitude); //Updates when GPS pings new location



    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
