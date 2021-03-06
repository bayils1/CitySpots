package com.application.cityspots;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SuppressWarnings("EmptyMethod")
@RunWith(AndroidJUnit4.class)
public class Unit_DBHandlerTest {

    @Mock
    DBHandler databaseMock;
    User testUser;
    Spot testSpot;
    List<Location> testLocations;
    List<Type> testTypes;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    public String username;
    public String password;
    public String spotID;


    /*
    public String testFullName;
    public String testCity;
    public String testPassword;
    public String testUserID;
    public String userDefinition[];
    public String testUserArrayValue[];
    */

    @Before
    public void setUp() {
    }
    @Test
    public void testValidPassword()  {
        databaseMock = mock(DBHandler.class);
        when(databaseMock.validPassword(username, password)).thenReturn(true);
        assertTrue(databaseMock.validPassword(username, password));
   }

    @Test
    public void testValidUser()  {
        databaseMock = mock(DBHandler.class);
        when(databaseMock.validUser(username)).thenReturn(true);
        assertTrue(databaseMock.validUser(username));
    }

    @Test
    public void getUserTest()  {
        databaseMock = mock(DBHandler.class);
        testUser = mock(User.class);
        when(databaseMock.getUser(username, password)).thenReturn(testUser);
        assertEquals(testUser,databaseMock.getUser(username, password));
    }

    @Test
    public void getSpotTest()  {
        databaseMock = mock(DBHandler.class);
        testSpot = mock(Spot.class);
        when(databaseMock.getSpot(spotID)).thenReturn(testSpot);
        assertEquals(testSpot,databaseMock.getSpot(spotID));
    }
    @Test
    public void getSpotCountTest()  {

        databaseMock = mock(DBHandler.class);
        when(databaseMock.spotCount(username)).thenReturn(2);
        assertEquals(2,databaseMock.spotCount(username));
    }

    @Test
    public void testGetSpotLocations()  {
        databaseMock = mock(DBHandler.class);
        testLocations = new ArrayList<>();
        testLocations.add(mock(Location.class));
        when(databaseMock.getSpotLocations("1")).thenReturn(testLocations);
        assertEquals(testLocations,databaseMock.getSpotLocations("1"));
    }

    @Test
    public void testGetSpotTypes()  {
        databaseMock = mock(DBHandler.class);
        testTypes = new ArrayList<>();
        testTypes.add(mock(Type.class));
        when(databaseMock.getSpotTypes("1")).thenReturn(testTypes);
        assertEquals(testTypes,databaseMock.getSpotTypes("1"));
    }

    @After
    public void tearDown() {
    }
}

