package com.moringaschool.myrestaurant;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)

public class MainActivityTest {

    private MainActivity mainActivity;

    @Before
    public void setUp(){
        mainActivity = Robolectric.setupActivity(MainActivity.class);
    }
}
