package com.team404.trackmyday;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener, LocationListener {

	private static final String LOG_TAG = "LocationService";

	//new
	private LocationRequest locationRequest;
	private GoogleApiClient mGoogleApiClient;

	private static boolean isRunning = false;


	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(LOG_TAG, "Service Started.");
		// Toast.makeText(this, "Service Started...",
		// Toast.LENGTH_SHORT).show();
		isRunning = true;
		//InitLocationManager();
		InitGoogleApiClient(this);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// handleCommand(intent);
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		isRunning = true;
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(LOG_TAG, "Service Stopped.");
		disConnectGoogleApi();
		isRunning = false;
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}


	public static boolean isRunning() {
		return isRunning;
	}


	public interface onUpdateLocation {
		void updateDetailsLocation(Location location);

		void isLocationFound(Boolean isLocation);
	}

	///
	@Override
	public void onLocationChanged(Location loc) {
		// TODO Auto-generated method stub
		if (loc != null) {

			MyLocationGoogleMapActivity.location = loc;

			float acc = loc.getAccuracy();

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
									if (ActivityCompat.checkSelfPermission(LocationService.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationService.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
											mGoogleApiClient, locationRequest, LocationService.this);
								}
							}
						}, 5000);

					}
				} catch (Exception ex) {
				}
			}
		}
	}


	// /
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {

			locationRequest = LocationRequest.create();
			locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			locationRequest.setInterval(1000 * 5);
			locationRequest.setFastestInterval(5);
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
		// Log.i(tag, "GoogleApiClient connection has been suspend");
	}

	// @Override
	// public void onDisconnected() {
	// // TODO Auto-generated method stub
	//
	// }

	private void InitGoogleApiClient(Context context) {

		LocationManager locationManager = (LocationManager) context
				.getSystemService(context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

			if (mGoogleApiClient != null) {
				if (!mGoogleApiClient.isConnected()) {
					mGoogleApiClient.connect();
				}
			} else if (mGoogleApiClient == null) {
				mGoogleApiClient = new GoogleApiClient.Builder(context)
						.addApi(LocationServices.API)
						.addConnectionCallbacks(this)
						.addOnConnectionFailedListener(this).build();

				mGoogleApiClient.connect();
			}

		} else {


		}
	}

    private void disConnectGoogleApi()
    {
        mGoogleApiClient.disconnect();
    }





}
