package com.team404.trackmyday;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by DanielMedina on 11/30/16.
 */
public class AddActivityTest extends ActivityInstrumentationTestCase2<AddActivity> {

    AddActivity activity;

    public AddActivityTest(){
        super(AddActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        activity = getActivity();

    }

    @SmallTest
    public void testEditTextNotNull(){
        EditText name = (EditText) activity.findViewById(R.id.locationName_txt);
        EditText actv = (EditText) activity.findViewById(R.id.ActivityTxt);
        assertNotNull(name);
        assertNotNull(actv);
    }

    @SmallTest
    public void testButtonNotNull(){
        Button addActivity = (Button) activity.findViewById(R.id.addActivityBtn);
        Button showLocations = (Button) activity.findViewById(R.id.showLocations);
        assertNotNull(addActivity);
        assertNotNull(showLocations);
    }

    @SmallTest
    public void testAddActivity(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("Users").child("Guest");

        final ArrayList<UserLocationModel> userLocationModelArrayList=new ArrayList<UserLocationModel>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserLocationModel selected;
                String id = "";
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    UserLocationModel users = dsp.getValue(UserLocationModel.class);

                    assertEquals(users.getName().equals("Google"), true);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //  Log.e(TAG, "Failed to read user", error.toException());
            }
        });

    }

}