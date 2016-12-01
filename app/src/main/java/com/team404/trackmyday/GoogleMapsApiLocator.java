package com.team404.trackmyday;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class GoogleMapsApiLocator extends FragmentActivity implements  OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private GoogleMap mMap;
    private MapView mMapView;

    //private LocationRequest locationRequest;

    private GoogleApiClient mGoogleApiClient;
    public static Location location;
    boolean isFirstloadedmap=false;
    boolean selectingActivity = false;
    double selected_lat;
    double selected_lon;

    private ArrayList<UserLocationModel> userLocationModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_maps_api_locator);
        initMap(savedInstanceState);

        Intent intent = getIntent();
        if(intent != null) {
            String caller = intent.getStringExtra("caller");
            if(caller == null){
                caller = "";
            }
                if (caller.equals("AddActivity")) {
                    selectingActivity = true;
                } else {
                    selectingActivity = false;
                }
        }else{
            selectingActivity = false;
        }
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

            mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
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

                mMap.addMarker(new MarkerOptions().position(new LatLng(userLocationModelArrayList.get(i).getLatitude(),userLocationModelArrayList.get(i).getLongitude())).title(getAddress1(GoogleMapsApiLocator.this,userLocationModelArrayList.get(i).getLatitude(),userLocationModelArrayList.get(i).getLongitude()))
                        .icon((BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED))));


            }

            if(selectingActivity == true) {
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        selected_lat = marker.getPosition().latitude;
                        selected_lon = marker.getPosition().longitude;
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                        intent.putExtra("lat", selected_lat);
                        intent.putExtra("lon", selected_lon);
                        intent.putExtra("caller", "GoogleMap");
                        startActivity(intent);
                        finish();
                        return false;

                    }
                });
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
        String email = "Group@";
        mMap = googleMap;

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            email = extras.getString("Account");
        }
        email = "Group@";
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("Users").child(cleanUpEmail(email));


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

    private String cleanUpEmail(String email) {
        Log.e("DatabaseKey",email);

        String temp = "";
        int i = 0;
        while(email.charAt(i) != '@'){
            temp += email.charAt(i);
            i++;
        }
        Log.e("DatabaseKey",temp);
        return temp;
    }

    public static String getAddress1(Context context, double lat, double lng) {

        String add = "";
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            if (geocoder != null) {
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                if (addresses != null && addresses.size() > 0) {
                    Address obj = addresses.get(0);
                    add = obj.getAddressLine(0) + " " + obj.getLocality();
                    // add = add + "\n" + obj.getPaName();
                    add = add + "\n" + obj.getCountryCode();
                    add = add + "," + obj.getAdminArea();
                    add = add + "\n" + obj.getPostalCode();
                    // add = add + "," + obj.getSubAdminArea();
                    // add = add + "," + obj.getLocality();
                    // add = add + "\n" + obj.getSubThoroughfare();
                    // Constants.masjids.setProvince(obj.getAdminArea());
                    // Constants.masjids.setTown(obj.getSubAdminArea());
                    // Constants.masjids.setCountry(obj.getCountryName());

                    add = add.replaceAll("null", "");
                }
            }
            Log.v("IGA", "Address" + add);
        } catch (IOException e) {
            e.printStackTrace();
            // Toast.makeText(getActivity(), e.getMessage(),
            // Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return add;

    }




    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter(){
            myContentsView = getLayoutInflater().inflate(R.layout.custom_window_marker, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
            tvTitle.setText(marker.getTitle());
            TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
            tvSnippet.setText(marker.getSnippet());

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }




}
