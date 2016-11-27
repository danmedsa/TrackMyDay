package com.team404.trackmyday;
//https://track-my-day-a2c6f.firebaseio.com/

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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


public class Location extends AppCompatActivity {


    private Button track_btn;
    private TextView coord_view, coord_view2;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String dateString, time;
    private double latitude, longitude;

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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.INTERNET
            }, 10);
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
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");           //Collect Date and Time for location
                dateString = dateFormat.format(new Date());
                Date date = new Date();
                File path = getApplicationContext().getFilesDir();

                saveLocDB();//Saves Ping data to database


                time = date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();

                coord_view2.setText("Latitude: "+latitude+"\nLongitude: "+longitude+"\nDate: "+dateString+"\nTime: "+time);

            }
        });

    }

    private void saveLocDB(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");           //Collect Date and Time for location
        dateString = dateFormat.format(new Date());
        String email = "Test2";

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            email = extras.getString("Account");
        }

        final DatabaseReference myRef = database.getReference().child("Users").child(email);


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               long pingCount = dataSnapshot.getChildrenCount();
                //Data pushed to Firebase
                Map<String, Object> newping = new HashMap<String, Object>();
                newping.put(String.valueOf(pingCount),latitude+", "+longitude+", "+dateString+", "+time);
                myRef.updateChildren(newping);
                Log.d("Children", String.valueOf(pingCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



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




}
