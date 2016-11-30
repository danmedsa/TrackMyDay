package com.team404.trackmyday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        String email = session.getUser();
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
