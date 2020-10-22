package com.application.cityspots;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class Act_NewSpotTest {

    @Rule
    public final ActivityTestRule<Act_NewSpot> newSpotActivity = new ActivityTestRule<>(Act_NewSpot.class);
    private String spotNameTest;
    private String spotLocationTest;
    private String spotTypeTest;
    private String spotTagTest;
    // --Commented out by Inspection (22/10/2020 9:52 PM):public ImageView cameraImage;

    @SuppressWarnings("unused")
    @Before
    public void setUp() {
        Act_NewSpot newSpot = newSpotActivity.getActivity();
        spotNameTest = "SpotName";
        spotLocationTest = "SpotLocation";
        spotTagTest = "SpotTag";
        spotTypeTest = "SpotType";
    }

    @Test
    public void testNewSpot() {
        onView(withId(R.id.txtSpotName)).perform(typeText(spotNameTest));
        closeSoftKeyboard();
        onView(withId(R.id.txtSpotLocation)).perform(typeText(spotLocationTest));
        closeSoftKeyboard();
        onView(withId(R.id.txtSpotTag)).perform(typeText(spotTagTest));
        closeSoftKeyboard();
        onView(withId(R.id.txtSpotType)).perform(clearText(), typeText(spotTypeTest));
        closeSoftKeyboard();
        onView(withId(R.id.txtSpotName)).check(matches(withText("SpotName")));
        onView(withId(R.id.txtSpotLocation)).check(matches(withText("SpotLocation")));
        onView(withId(R.id.txtSpotTag)).check(matches(withText("SpotTag")));
        onView(withId(R.id.txtSpotType)).check(matches(withText("SpotType")));
    }

    @After
    public void tearDown() {
    }

    @Test
    public void onCreate() {
    }
}