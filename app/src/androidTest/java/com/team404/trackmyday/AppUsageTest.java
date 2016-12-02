package com.team404.trackmyday;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Carl Carter on 12/1/2016.
 */

public class AppUsageTest extends ActivityInstrumentationTestCase2<AppUsage> {

    AppUsage uptime;

    public AppUsageTest(){
        super(AppUsage.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        uptime = getActivity();

    }

    @SmallTest
    public void testButtonNotNull(){
        Button refresh = (Button) uptime.findViewById(R.id.foreground_btn);
        assertNotNull(refresh);
    }

    @SmallTest
    public void testTextViewNotNull() {
        TextView milliup = (TextView) uptime.findViewById(R.id.appname_display);
        assertNotNull(milliup);
    }
}
