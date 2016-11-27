package com.team404.trackmyday;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class GoogleMapsApiLocator extends FragmentActivity implements  OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private GoogleMap mMap;
    private MapView mMapView;

    //private LocationRequest locationRequest;

    private GoogleApiClient mGoogleApiClient;
    public static Location location;
    boolean isFirstloadedmap=false;

    private ArrayList<UserLocationModel> userLocationModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_maps_api_locator);
        initMap(savedInstanceState);

        stopLocationService();
    }
    private void initMap(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        mMapView = (MapView)findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(GoogleMapsApiLocator.this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //mMap = mMapView.getMap();

        mMapView.getMapAsync(this);


        //loadLocationonMap();


    }




    private void loadLocationonMap(ArrayList<UserLocationModel> userLocationModelArrayList) {

        if (mMap != null && userLocationModelArrayList!=null && userLocationModelArrayList.size()>0) {


            // InitLocationManager();
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setRotateGesturesEnabled(true);//
            // CompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(false);
            // map.getUiSettings().setZoomControlsEnabled(isCancelled());//
            // ZoomGesturesEnabled(enabled)RotateGesturesEnabled(false);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userLocationModelArrayList.get(0).getLatitude(),userLocationModelArrayList.get(0).getLongitude()), 15));



            for(int i=0;i<userLocationModelArrayList.size();i++){

                mMap.addMarker(new MarkerOptions().position(new LatLng(userLocationModelArrayList.get(i).getLatitude(),userLocationModelArrayList.get(i).getLongitude()))
                        .icon((BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED))));


            }


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

        LocationManager locationManager = (LocationManager) GoogleMapsApiLocator.this
                .getSystemService(GoogleMapsApiLocator.this.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//
//
            if (mGoogleApiClient != null) {
                if (!mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            } else if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(GoogleMapsApiLocator.this)
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
                GoogleMapsApiLocator.this);
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

//
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

//


    private void startLocationService()
    {
        if(!LocationService.isRunning()) {
            startService(new Intent(GoogleMapsApiLocator.this, LocationService.class));
        }
    }


    private void stopLocationService()
    {
        if(LocationService.isRunning()) {
            stopService(new Intent(GoogleMapsApiLocator.this, LocationService.class));
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        startLocationService();
    }

    //Method to read from firebase db//
    @Override
    public void onMapReady(GoogleMap googleMap) {
        String email = "Test2";
        mMap = googleMap;

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            email = extras.getString("Account");
        }

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("Users").child(email);


        userLocationModelArrayList=new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    UserLocationModel users =dsp.getValue(UserLocationModel.class);
                    userLocationModelArrayList.add(users); //add result into array list
                }




                for(int i =0;i<userLocationModelArrayList.size();i++){

                   Date dbsavedDate = GetFormatedtDateWithDateYearMonthTime(userLocationModelArrayList.get(i).getDateString());

                    int dbMonth=dbsavedDate.getMonth();
                    int dbyear=dbsavedDate.getYear();
                    int dbdate=dbsavedDate.getDate();


                    Date currentdate = new Date();

                    if(dbMonth==currentdate.getMonth() && dbdate==currentdate.getDate()&& dbyear==currentdate.getYear()){

                    }else{
                        userLocationModelArrayList.remove(i);
                    }








                }







                loadLocationonMap(userLocationModelArrayList);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //  Log.e(TAG, "Failed to read user", error.toException());
            }
        });



    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    public  Date GetFormatedtDateWithDateYearMonthTime(String date) {
        Date dataFrom = new Date();
        try {
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

            dataFrom = df.parse(date);
        } catch (Exception ex) {
            Log.w("Exception", ex.getMessage());
        }

        return dataFrom;

    }
}
