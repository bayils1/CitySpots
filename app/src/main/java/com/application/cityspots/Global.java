package com.application.cityspots;

import android.app.Application;

public class Global extends Application {

    public static String[] getUserDefinition() {
        return new String[]{"userID", "fullName", "username", "password", "city"};
    }

    public static String[] getSpotDefinition() {
        return new String[]{"spotID", "spotName", "spotType", "tag", "image", "location"};
    }
}
