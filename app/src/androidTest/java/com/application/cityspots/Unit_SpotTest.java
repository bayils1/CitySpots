package com.application.cityspots;

import androidx.test.ext.junit.runners.AndroidJUnit4;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;


@SuppressWarnings("EmptyMethod")
@RunWith(AndroidJUnit4.class)
public class Unit_SpotTest {

    public String testSpotID;
    public String expectedSpotID;
    public String testSpotName;
    public String expectedSpotName;
    public String testTag;
    public String expectedTag;
    public String testSpotType;
    public String expectedSpotType;

    public String expectedLocation;
    public String testLocation;
    public String[] spotDefinition;
    public String[] spotArrayValue;
    public Spot testSpot;

    @Before
    public void setUp() {
        expectedLocation ="sl";
        testLocation = "sl";

        expectedSpotName = "sn";
        testSpotName = "sn";

        expectedSpotType = "st";
        testSpotType = "st";

        expectedTag = "st";
        testTag = "st";

        expectedSpotID = "si";
        testSpotID = "si";

        spotDefinition = Global.getSpotDefinition();
        spotArrayValue = new String[]{testSpotID, testSpotName, testSpotType, testTag, "null",testLocation};

        testSpot = new Spot();
    }
    @Test
    public void setSpotValuesTest(){

        for(int i = 0 ; i < spotDefinition.length;i++)
        {
            testSpot.setSpotValue(spotArrayValue[i], spotDefinition[i]);
        }

        assertEquals(expectedLocation,testSpot.getLocation());
        assertEquals(expectedSpotID, testSpot.getSpotID());
        assertEquals(expectedSpotName, testSpot.getSpotName());
        assertEquals(expectedSpotType, testSpot.getSpotType());
        assertEquals(expectedTag, testSpot.getTag());
        //assertEquals(null, testSpot.getPhoto());

    }

    @After
    public void tearDown() {
    }
}
