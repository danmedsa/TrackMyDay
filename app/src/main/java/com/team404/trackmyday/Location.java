package com.team404.trackmyday;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        coord_view2 = (TextView) findViewById(R.id.coord_view2);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                latitude = location.getLatitude(); longitude = location.getLongitude();
                coord_view.append("\n " +latitude + " " +longitude);
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
        }else{
            configureButton();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;
        }
    }

    private void configureButton() {
        track_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");           //Collect Date and Time for location
                dateString = dateFormat.format(new Date());
                Date date = new Date();
                File path = getApplicationContext().getFilesDir();


                //Saves Ping information to ping.txt file
                File file = new File(path, "ping.txt");

                FileOutputStream stream = null;
                try {
                    stream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try{
                    stream.write(("["+latitude+", "+longitude+", "+date+", "+time+"]\n").getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //Reads ping.txt file (Use for Displaying locations)
                //Format is [lat, long, date, time]\n
                int length = (int) file.length();

                byte[] bytes = new byte[length];

                FileInputStream in = null;
                try {
                    in = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    in.read(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                String contents = new String(bytes);


                time = date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();

                coord_view2.setText("Latitude: "+latitude+"\nLongitude: "+longitude+"\nDate: "+dateString+"\nTime: "+time);
                Log.d("Contents",contents);
            }
        });

    }
    //returns latitude
    private double getLatitude() {
        return latitude;
    }

    //returnes longitude
    private double getLongitude() {
        return longitude;
    }




}
