package com.team404.trackmyday;

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
    public Background() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timer t = new Timer();

        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
            Log.d("Background", "Works");
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");           //Collect Date and Time for location
                Date date = new Date();
                dateString = dateFormat.format(new Date());
                time = date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
                String email = "Background Ping Test";
                final FirebaseDatabase database = FirebaseDatabase.getInstance();

                final DatabaseReference myRef = database.getReference().child("Users").child(email);



                userId = myRef.push().getKey();
                UserLocationModel users = new UserLocationModel(101.010,010.101,dateString,time);
                myRef.child(userId).setValue(users);
            }

        },
                //Set how long before to start calling the TimerTask (in milliseconds)
                0,
                //Set the amount of time between each execution (in milliseconds)
                60000);



        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
