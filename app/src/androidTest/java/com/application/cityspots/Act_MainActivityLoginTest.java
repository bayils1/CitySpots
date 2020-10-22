package com.application.cityspots;


import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@SuppressWarnings("unused")
@LargeTest
@RunWith(AndroidJUnit4.class)
public class Act_MainActivityLoginTest {

    @Rule
    public final ActivityTestRule<Act_MainActivityLogin> mActivityTestRule = new ActivityTestRule<>(Act_MainActivityLogin.class);
    public Act_MainActivityLogin loginActivity = null;
    private String username;
    private String password;


    @Before
    public void setUp() {
        loginActivity = mActivityTestRule.getActivity();
        username = "test";
        password = "test";
    }

    @Test
    public void act_MainActivityLoginTest() {
        onView(withId(R.id.txtUserName)).perform(typeText(username));
        onView(withId(R.id.txtLoginPassword)).perform(typeText(password));
        closeSoftKeyboard();
        onView(withId(R.id.btnLogin)).perform(click());
    }
}
