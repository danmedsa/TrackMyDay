package com.team404.trackmyday;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MyLocationGoogleMapActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, LocationListener,
        com.google.android.gms.location.LocationListener {


    private GoogleMap mMap;
    private MapView mMapView;

    private LocationRequest locationRequest;

    private GoogleApiClient mGoogleApiClient;
    public static android.location.Location location;
    boolean isFirstloadedmap=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_location_google_map_activity);
        initMap(savedInstanceState);

        stopLocationService();
    }
    private void initMap(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        mMapView = (MapView)findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(MyLocationGoogleMapActivity.this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMap = mMapView.getMap();


        //loadLocationonMap();


    }




    private void loadLocationonMap() {

        if (mMap != null) {


            // InitLocationManager();
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setRotateGesturesEnabled(true);//
            // CompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(false);
            // map.getUiSettings().setZoomControlsEnabled(isCancelled());//
            // ZoomGesturesEnabled(enabled)RotateGesturesEnabled(false);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 15));

            mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude()))
                    .icon((BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED))));
        }



    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }



    @Override
    protected void onResume() {
        super.onResume();

        CheckGPS();

        //loadLocationonMap();

    }

    private void CheckGPS() {

        LocationManager locationManager = (LocationManager) MyLocationGoogleMapActivity.this
                .getSystemService(MyLocationGoogleMapActivity.this.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//
//
            if (mGoogleApiClient != null) {
                if (!mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            } else if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(MyLocationGoogleMapActivity.this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this).build();

                mGoogleApiClient.connect();
            }

        } else {
            showGPSDisabledAlertToUser();

        }

    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MyLocationGoogleMapActivity.this);
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
            if (ActivityCompat.checkSelfPermission(MyLocationGoogleMapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {




        if (location != null) {

            MyLocationGoogleMapActivity.location=location;

            if(mMap!=null){
                //mMap.clear();

                if(!isFirstloadedmap){
                    isFirstloadedmap=true;
                    mMap.clear();

                    loadLocationonMap();
                }




            }



            MyLocationGoogleMapActivity.location = location;
            float acc = location.getAccuracy();
            // Toast.makeText(getActivity(), String.valueOf(acc),
            // Toast.LENGTH_LONG).show();
            if (acc <= 50) {


            } else {
                try {
                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                    if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && locationRequest != null) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && locationRequest != null) {
                                    if (ActivityCompat.checkSelfPermission(MyLocationGoogleMapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MyLocationGoogleMapActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                                            mGoogleApiClient, locationRequest, MyLocationGoogleMapActivity.this);
                                }
                            }
                        }, 50000);

                    }
                } catch (Exception ex) {
                }
            }
        }

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

    private void startLocationService()
    {
        if(!LocationService.isRunning()) {
            startService(new Intent(MyLocationGoogleMapActivity.this, LocationService.class));
        }
    }


    private void stopLocationService()
    {
        if(LocationService.isRunning()) {
            stopService(new Intent(MyLocationGoogleMapActivity.this, LocationService.class));
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        startLocationService();
    }


}
