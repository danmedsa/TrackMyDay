package com.team404.trackmyday;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by James on 11/27/2016.
 */

public class GeneratedReport extends AppCompatActivity {

    //Needed variables
    public ArrayList<UserLocationModel> locations = new ArrayList<UserLocationModel>();
    public int count;
    private String startDate, endDate;

    public ListView lv;
    private CustomReportAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_lv);

        //Get email and period from ReportOptionsActivity
        Bundle extras = getIntent().getExtras();
        String email = extras.getString("Email");
        String period = extras.getString("Period");

        //Initialize
        adapter = new CustomReportAdapter(this, locations);
        lv = (ListView) findViewById(R.id.report_lv);
        lv.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        getLocations(email,period);

    }

    //Get the locations of the user based on their email and time period chosen
    private void getLocations(String email, String period) {


        //Set up connection to Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("Users").child("Guest");

        //Determine whether it is a daily, weekly, or monthly report_lv
        getPeriodDate(period);

        //Initiate Firebase query
        Query query = myRef.orderByChild("dateString").startAt(startDate).endAt(endDate);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String tempTime;
                String[] separatedTime;
                int hour, minute, second, pm = 0;
                int pHour, pMinute, pSecond;
                double duration = 0;

                int hDiff = 0, mDiff = 0, sDiff = 0;

                //Clear any locations stored in the ArrayList
                locations.clear();

                UserLocationModel tempLocation = new UserLocationModel();
                UserLocationModel previousLocation = new UserLocationModel();
                count = 0;

                //Collect the locations between the two dates for the specified email
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    tempLocation = child.getValue(UserLocationModel.class);
                    tempTime = tempLocation.getTime();
                    separatedTime = tempTime.split(":");
                    hour = Integer.parseInt(separatedTime[0]);
                    minute = Integer.parseInt(separatedTime[1]);
                    second = Integer.parseInt(separatedTime[2]);


                    //Convert time to 12 hr clock
                    if(hour > 12) {
                        hour -= 12;
                        pm = 1;
                    }
                    tempTime = tempTime.format("%02d:%02d:%02d",hour,minute,second);

                    if (pm == 1)
                        tempTime += " PM";
                    else
                        tempTime += " AM";

                    tempLocation.setTime(tempTime);

                    //Calculate duration
                    if (locations.size() != 0){
                        previousLocation = locations.get(locations.size()-1);
                        if (previousLocation.getLongitude() == tempLocation.getLongitude() && previousLocation.getLatitude() == tempLocation.getLatitude()){
                            //Separate the time into ints
                            tempTime = previousLocation.getTime();
                            separatedTime = tempTime.split(":");
                            String[] tempSecond = separatedTime[2].split(" ");
                            pHour = Integer.parseInt(separatedTime[0]);
                            pMinute = Integer.parseInt(separatedTime[1]);
                            pSecond = Integer.parseInt(tempSecond[0]);
                            
                            //Calculate the difference
                            hDiff = hour - pHour;
                            mDiff = minute - pMinute;
                            sDiff = second - pSecond;

                            duration += sDiff + (mDiff * 60) + (hDiff * 3600);
                            duration = duration/60;
                            
                            //Instead of adding repeated data to locations, i.e. same GPS, set duration
                            locations.get(locations.size()-1).setDuration(duration);
                        }
                        else {
                            locations.add(tempLocation);
                        }
                    }
                    else
                        locations.add(tempLocation);

/*
                    //Display retrieved information
                    System.out.println("Location " + (count) + ":\n\tLatitude: " + locations.get(count).getLatitude() + "\n\tLongitude: " + locations.get(count).getLongitude() + "\n\tDate: " + locations.get(count).getDateString() + "\n\tTime: " + tempTime);//locations.get(count).getTime());
                   */ count++;

                    duration = 0;

                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                displayToast("Error connecting to database. Try again later");
            }

        });
    }


    //Calculate the dates of the time period
    private void getPeriodDate(String period){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        this.endDate = dateFormat.format(new Date());

        Calendar temp = Calendar.getInstance();

        switch (period){
            case "DAILY":
                this.startDate = dateFormat.format(temp.getTime());
                break;
            case "WEEKLY":
                temp.add(Calendar.DATE, -7);
                this.startDate = dateFormat.format(temp.getTime());
                break;
            case "MONTHLY":
                temp.add(Calendar.DATE, -30);
                this.startDate = dateFormat.format(temp.getTime());
                break;
        }
    }

    private void displayToast(String s)
    {
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM,0,0);
        toast.show();
    }

}
