package com.application.cityspots;

import android.app.Application;

import java.util.Arrays;
import java.util.List;

public class Global extends Application {

    public static String[] getUserDefinition() {
        return new String[]{"userID", "fullName", "username", "password", "city", "spotFilter"};
    }

    public static String[] getSpotDefinition() {
        return new String[]{"spotID", "spotName", "spotType", "tag", "image", "location"};
    }

    public static List<String> defaultSpotTypes() {
        return Arrays.asList("Nature Spot", "Food Spot", "Urban Spot");
    }

    public static List<String> defaultLocations() {
        return Arrays.asList("Wellington", "Auckland", "Christchurch");
    }
}
