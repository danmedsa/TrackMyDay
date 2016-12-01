package com.team404.trackmyday;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Background extends Service {
    private String userId;
    private String dateString, time;
    private double lat, lon;
    String email;
    public Background() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // Log.d("Recieve","Background");
        Timer t = new Timer();

        if(intent.getStringExtra("Email") == null){
            email = "Guest";
        }else{
            email = intent.getStringExtra("Email");
        }

        if(intent.getStringExtra("UpdateCoor").equals("Change")){
            //Log.d("Update", "New Coordinates");
            lat = Double.parseDouble(intent.getStringExtra("latitude"));
            lon = Double.parseDouble(intent.getStringExtra("longitude"));
            UpdateCoordinates(lat, lon);
        }else {
            Log.d("TIMER", "Start");
            t.scheduleAtFixedRate(new TimerTask() {
                                      @Override
                                      public void run() {
                                          Log.d("Background", "Works");
                                          SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");           //Collect Date and Time for location
                                          Date date = new Date();
                                          dateString = dateFormat.format(new Date());
                                          time = date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();



                                          final FirebaseDatabase database = FirebaseDatabase.getInstance();

                                          final DatabaseReference myRef = database.getReference().child("Users").child(email);


                                          userId = myRef.push().getKey();
                                          UserLocationModel users = new UserLocationModel(lat, lon, dateString, time);
                                          myRef.child(userId).setValue(users);
                                      }

                                  },
                    //Set how long before to start calling the TimerTask (in milliseconds)
                    0,
                    //Set the amount of time between each execution (in milliseconds)
                    300000);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void UpdateCoordinates(double latitude, double longitude){
        lat = latitude;
        lon = longitude;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
