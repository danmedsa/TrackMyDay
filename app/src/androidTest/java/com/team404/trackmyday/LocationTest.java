package com.team404.trackmyday;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Carl Carter on 12/1/2016.
 */

public class LocationTest extends ActivityInstrumentationTestCase2<Location> {

    Location location;

    public LocationTest(){
        super(Location.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        location = getActivity();

    }

    @SmallTest
    public void testButtonNotNull(){
        Button manualPing = (Button) location.findViewById(R.id.track_btn);
        assertNotNull(manualPing);
    }

    @SmallTest
    public void testTextViewNotNull(){
        TextView locText = (TextView) location.findViewById(R.id.coord_view);
        TextView locText2 = (TextView) location.findViewById(R.id.coord_view2);
        assertNotNull(locText);
        assertNotNull(locText2);
    }

}
