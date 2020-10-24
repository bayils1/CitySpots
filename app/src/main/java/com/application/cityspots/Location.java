package com.application.cityspots;

public class Location {

    private String LocationID;
    private String LocationName;
    private String UserID;

    public Location() {
    }

    public Location(String LocationName, String userID) {
        this.LocationName = LocationName;
        this.UserID = userID;
    }

    public String getLocationID() {
        return LocationID;
    }

    public void setLocationID(String locationID) {
        LocationID = locationID;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}
