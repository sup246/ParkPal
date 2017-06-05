package com.psu.sweng500.team4.parkpal;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Before
    public void userIsLoggedIn() {

    }

    @Test
    public void mapAppearsAsDefault() {
        Assert.fail();
    }

    @Test
    public void mapAppearsCenteredAtCurrentLocation() {
        Assert.fail();
    }

    @Test
    public void parksAreVisibleOnMap() {
        Assert.fail();
    }
}
