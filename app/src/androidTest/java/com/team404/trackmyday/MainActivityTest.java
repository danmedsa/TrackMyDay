package com.team404.trackmyday;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;

import org.junit.Before;

/**
 * Created by DanielMedina on 12/1/16.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity activity;

    public MainActivityTest(){
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
    }



    @SmallTest
    public void testButtonNotNull(){
        Button viewLocs = (Button) activity.findViewById(R.id.btn_view_locations);
        Button viewRep = (Button) activity.findViewById(R.id.btn_view_report);
        Button manualPing = (Button) activity.findViewById(R.id.btn_manual_ping);
        Button appUsage = (Button) activity.findViewById(R.id.btn_app_usage);
        Button contact = (Button) activity.findViewById(R.id.btn_contacts);
        Button addActivity = (Button) activity.findViewById(R.id.btn_input_activity);
        Button actEm = (Button) activity.findViewById(R.id.btn_activate_emergency);
        Button signout = (Button) activity.findViewById(R.id.btn_sign_out);

        assertNotNull(viewLocs);
        assertNotNull(viewRep);
        assertNotNull(addActivity);
        assertNotNull(manualPing);
        assertNotNull(actEm);
        assertNotNull(appUsage);
        assertNotNull(contact);
        assertNotNull(signout);

    }


}