package com.team404.trackmyday;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.test.ActivityInstrumentationTestCase2;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import android.test.mock.MockContext;

import org.junit.Test;

/**
 * Created by cc_ac on 12/2/2016.
 */

public class AndroidVerTest{

    @Test
    public void androidVer(){
        assertTrue(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }
}
