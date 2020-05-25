package com.moringaschool.myrestaurant;

import android.content.Intent;
import android.widget.TextView;

import com.moringaschool.myrestaurant.ui.MainActivity;
import com.moringaschool.myrestaurant.ui.RestaurantsActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;

import static junit.framework.TestCase.*;

@RunWith(RobolectricTestRunner.class)

public class MainActivityTest {

    private MainActivity mainActivity;

    @Before
    public void setUp(){
        mainActivity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void validateTheNameOfTheApp() {
        TextView appName = mainActivity.findViewById(R.id.appNameTextView);
        assertTrue("My Restaurants".equals(appName.getText().toString()));
    }

    @Test
    public void assertsSecondActivityIsStarted() {
        mainActivity.findViewById(R.id.findRestaurantsButton).performClick();
        Intent expected = new Intent(mainActivity, RestaurantsActivity.class);

        ShadowActivity shadowActivity = org.robolectric.Shadows.shadowOf(mainActivity);
        Intent actual = shadowActivity.getNextStartedActivity();
        assertTrue(actual.filterEquals(expected));
    }

}
