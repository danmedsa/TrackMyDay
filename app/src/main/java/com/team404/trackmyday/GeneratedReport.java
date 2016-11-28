package com.team404.trackmyday;

import android.content.Intent;

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

public class GeneratedReport {

    //List to hold needed locations
    private static ArrayList<UserLocationModel> locations = new ArrayList<UserLocationModel>();
    public static int count;
    private String startDate, endDate;

    //Get the locations of the user based on their email and time period chosen
    public void getLocations(String email, String period){

        //Set up connection to Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("Users").child(email);

        //Determine whether it is a daily, weekly, or monthly report
        getPeriodDate(period);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Query query = myRef.orderByChild("dateString").startAt(startDate).endAt(endDate);

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //Clear any locations stored in the ArrayList
                        locations.clear();

                        UserLocationModel tempLocation = new UserLocationModel();
                        count = 0;

                        //Collect the locations between the two dates for the specified email
                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                            tempLocation = child.getValue(UserLocationModel.class);

                            locations.add(tempLocation);

                            //Display retrieved information
                            System.out.println("Location " + (count+1) + ":\n\tLatitude: " + locations.get(count).getLatitude() + "\n\tLongitude: " + locations.get(count).getLongitude() + "\n\tDate: " +locations.get(count).getDateString() + "\n\tTime: "+locations.get(count).getTime());
                            count++;
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                temp.add(Calendar.DATE, -1);
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

}
