package com.application.cityspots;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@SuppressWarnings({"unused", "deprecation"})
@RunWith(AndroidJUnit4.class)
public class Act_EditSpotTest {

    @Rule
    public final ActivityTestRule<Act_EditSpot> editActivity = new ActivityTestRule<>(Act_EditSpot.class);
    private String spotNameTest;
    private String spotLocationTest;
    private String spotTypeTest;
    private String spotTagTest;

    @Before
    public void setUp() {
        Act_EditSpot editSpot = editActivity.getActivity();
        spotNameTest = "SpotName";
        spotTypeTest = "Nature Spot";
        spotTagTest = "SpotTag";
        spotLocationTest = "Wellington";
    }

    @Test
    public void testUpdateFunction() {
        onView(withId(R.id.txtSpotName)).perform(typeText(spotNameTest));
        closeSoftKeyboard();
        onView(withId(R.id.spnType)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        onView(withId(R.id.spnType)).check(matches(withSpinnerText(spotTypeTest)));
        onView(withId(R.id.txtSpotTag)).perform(typeText(spotTagTest));
        closeSoftKeyboard();
        onView(withId(R.id.txtSpotName)).check(matches(withText("SpotName")));
        onView(withId(R.id.spnLocation)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        onView(withId(R.id.spnLocation)).check(matches(withSpinnerText(spotLocationTest)));
        onView(withId(R.id.txtSpotTag)).check(matches(withText("SpotTag")));
    }

    @After
    public void tearDown() {
    }

    @Test
    public void onCreate() {
    }
}