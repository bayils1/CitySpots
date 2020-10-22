package com.application.cityspots;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("EmptyMethod")
@RunWith(AndroidJUnit4.class)
public class Unit_UserTest {


    public String testUsername;
    public String expectedUsername;
    public String testFullName;
    public String expectedFullName;
    public String testCity;
    public String expectedCity;
    public String testPassword;
    public String expectedPassword;
    public String testUserID;
    public String expectedTestUserID;
    public String[] userDefinition;
    public String[] testUserArrayValue;
    public User testUser;

    @Before
    public void setUp() {
        testUsername = "tu";
        expectedUsername = "tu";
        testFullName = "tfn";
        expectedFullName = "tfn";
        testCity = "tc";
        expectedCity = "tc";
        testPassword = "tp";
        expectedPassword = "tp";
        testUserID = "tuid";
        expectedTestUserID = "tuid";
        userDefinition = Global.getUserDefinition();
        testUserArrayValue = new String[]{testUserID, testFullName, testUsername, testPassword, testCity};
        testUser = new User();
    }

    @Test
    public void setUserValuesTest() {


        for (int i = 0; i < userDefinition.length; i++) {
            testUser.setUserValue(testUserArrayValue[i], userDefinition[i]);
        }

        assertEquals(expectedFullName, testUser.getFullName());
        assertEquals(expectedCity, testUser.getCity());
        assertEquals(expectedPassword, testUser.getPassword());
        assertEquals(expectedUsername, testUser.getUsername());
        assertEquals(expectedUsername, testUser.getUsername());
        assertEquals(expectedTestUserID, testUser.getUserID());
    }

    @After
    public void tearDown() {
    }
}
