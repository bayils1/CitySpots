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

@SuppressWarnings("EmptyMethod")
@RunWith(AndroidJUnit4.class)
public class Act_RegisterTest {

    @Rule
    public final ActivityTestRule<Act_Register> regActivity = new ActivityTestRule<>(Act_Register.class);
    private String username;
    private String fullname;
    private String city;
    private String password;

    @SuppressWarnings("unused")
    @Before
    public void setUp() {
        Act_Register registerActivity = regActivity.getActivity();
        username ="testUser001";
        fullname = "test User";
        city = "CityA";
        password = "password";
    }

    @Test
    public void testRegister(){
        onView(withId(R.id.txtUserName)).perform(typeText(username));
        closeSoftKeyboard();
        onView(withId(R.id.txtFullName)).perform(typeText(fullname));
        closeSoftKeyboard();
        onView(withId(R.id.txtCity)).perform(typeText(city));
        closeSoftKeyboard();
        onView(withId(R.id.txtLoginPassword)).perform(clearText(),typeText(password));
        closeSoftKeyboard();
        onView(withId(R.id.txtUserName)).check(matches(withText("testUser001")));
        onView(withId(R.id.txtFullName)).check(matches(withText("test User")));
        onView(withId(R.id.txtCity)).check(matches(withText("CityA")));
        onView(withId(R.id.txtLoginPassword)).check(matches(withText("password")));
    }
    @After
    public void tearDown() {
    }

    @Test
    public void onCreate() {
    }
}
