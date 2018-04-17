package com.psi.index.singapore.sp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.psi.index.singapore.sp.Activity.MapsActivity;
import com.psi.index.singapore.sp.Utils.MainController;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    ActivityTestRule<MapsActivity> mapsActivityActivityTestRule= new ActivityTestRule<MapsActivity>(MapsActivity.class);
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
       MapsActivity mMapactivity = mapsActivityActivityTestRule.getActivity();

//        mMapactivity.findViewById(R.id.refresh).performClick();
        MainController.getHrMinute("2018-04-14T20:00:00+08:00");
        assertEquals("com.psi.index.singapore.sp", appContext.getPackageName());

        useAppContext();
    }
}
