package com.team404.trackmyday;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class AddActivity extends AppCompatActivity {

    private Button addActivity;
    private EditText activityName;
    private TextView lat;
    private TextView lon;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        addActivity = (Button) findViewById(R.id.addActivityBtn);
        activityName = (EditText) findViewById(R.id.ActivityTxt);
        lat = (TextView) findViewById(R.id.latitude);
        lon = (TextView) findViewById(R.id.longitude);
        name = (TextView) findViewById(R.id.locName);

        LocationEntity loc = new LocationEntity(0.000,0.0000,"Imaginary", new Date());
    }
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.addActivityBtn:
                Toast toast = Toast.makeText(getApplicationContext(), "Activity "+ activityName.getText() +" Added" , Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM,0,0);
                toast.show();
                break;
        }
    }
}
