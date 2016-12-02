package com.team404.trackmyday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity implements
        View.OnClickListener {
    private EditText activity;
    private EditText locationName;
    private TextView lat_txt;
    private TextView lon_txt;
    private Button setActivity;
    private Button locations;

    private double lat;
    private double lon;
    private UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        activity = (EditText) findViewById(R.id.ActivityTxt);
        locationName = (EditText) findViewById(R.id.locationName_txt);
        lat_txt = (TextView) findViewById(R.id.latitude);
        lon_txt = (TextView) findViewById(R.id.longitude);

        locations = (Button) findViewById(R.id.addActivityBtn);
        setActivity = (Button) findViewById(R.id.showLocations);

        setActivity.setOnClickListener(this);
        locations.setOnClickListener(this);

        session = UserSession.getInstance();

        Intent intent = getIntent();
        if(intent != null){
            String caller = intent.getStringExtra("caller");
            if(caller != null){
                lat = intent.getDoubleExtra("lat",0);
                lon = intent.getDoubleExtra("lon",0);
                lat_txt.setText(String.valueOf(lat));
                lon_txt.setText(String.valueOf(lon));

            }
        }

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        //String email = session.getUser();
        String email = "Guest@";
        final DatabaseReference myRef = database.getReference().child("Users").child(cleanUpEmail(email));
    }

    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.showLocations:
                Intent intent = new Intent(this, GoogleMapsApiLocator.class);
                intent.putExtra("caller", "AddActivity");
                startActivity(intent);
                break;

            case R.id.addActivityBtn:
                //TODO add in database

                Intent intent1 = getIntent();
                if(intent1 != null){
                    String caller = intent1.getStringExtra("caller");
                    if(caller != null){
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference myRef = database.getReference().child("Users").child("Guest");//session.getUser());

                        final ArrayList<UserLocationModel> userLocationModelArrayList=new ArrayList<UserLocationModel>();

                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                UserLocationModel selected;
                                String id = "";
                                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                                    UserLocationModel users = dsp.getValue(UserLocationModel.class);
                                    if(users.getLatitude() == lat && users.getLongitude() == lon) {
                                        userLocationModelArrayList.add(users); //add result into array list
                                        id = dsp.getKey();
                                    }
                                }
                                String actName = String.valueOf(activity.getText());
                                UserActivity act = new UserActivity(actName);

                                for(UserLocationModel loc: userLocationModelArrayList){
                                    if(loc.getLatitude() == lat && loc.getLongitude() == lon){
                                        loc.addActivities(act);
                                        loc.addName(String.valueOf(locationName.getText()));
                                        //String userId = myRef.push().getKey();
                                        UserLocationModel users = loc;
                                        Map<String, Object> updates = new HashMap<>();
                                        updates.put("activity",loc.getActivity());
                                        updates.put("dateString",loc.getDateString());
                                        updates.put("latitude",loc.getLatitude());
                                        updates.put("longitude", loc.getLongitude());
                                        updates.put("name", loc.getName());
                                        updates.put("time", loc.getTime());
                                        myRef.child(id).updateChildren(updates);
                                    }
                                    Log.e("Location"," Lat: " + loc.getLatitude() + " Lon: " + loc.getLongitude());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                //  Log.e(TAG, "Failed to read user", error.toException());
                            }
                        });
                    }else{
                        Toast.makeText(this, "Select A Location first",Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
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

}
