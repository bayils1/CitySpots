package com.application.cityspots;

import android.graphics.Bitmap;

public class NatureSpot extends Spot {
    @SuppressWarnings("unused")
    private String activity;

    public NatureSpot(String spotName, String tag, @SuppressWarnings("unused") String spotType, Bitmap photo, String location) {
        this.setPhoto(photo);
        this.setSpotName(spotName);
        this.setDefaultType();
        this.setTag(tag);
        this.setLocation(location);
    }

    public String getActivity() {
        return activity;
    }

// --Commented out by Inspection START (22/10/2020 9:53 PM):
//    public void setActivity(String activity) {
//        this.activity = activity;
//    }
// --Commented out by Inspection STOP (22/10/2020 9:53 PM)

    @Override
    public void setDefaultType() {
        this.setSpotType("Nature Spot");
    }
}
