package com.team404.trackmyday;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.EditText;

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

}