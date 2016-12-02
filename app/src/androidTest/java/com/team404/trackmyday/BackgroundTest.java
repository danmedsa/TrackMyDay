package com.team404.trackmyday;

import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityUnitTestCase;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import static org.hamcrest.core.IsInstanceOf.any;

@RunWith(AndroidJUnit4.class)
/**
 * Created by Carl Carter on 12/2/2016.
 */

public class BackgroundTest {
    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    @Test
    public void testWithBoundService() throws TimeoutException {
        // Create the service Intent.
        Intent serviceIntent =
                new Intent(InstrumentationRegistry.getTargetContext(),
                        Background.class);
        // Pass Test Data to the Background Service
        serviceIntent.putExtra("Test", "Data");
    }

}
