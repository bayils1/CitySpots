package com.application.cityspots;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

@SuppressWarnings("CopyConstructorMissesField")
public class Spot {
    public Spot() {
        setDefaultType();
    }

    private String spotID;
    private String spotName;
    private String tag;
    private String spotType;
    private Bitmap photo;
    private String location;

    public Spot(String spotName, String tag, String spotType, Bitmap photo, String location) {
        this.photo = photo;
        this.spotName = spotName;
        this.tag = tag;
        this.spotType = spotType;
        this.location = location;
    }

    public Spot(Spot spot) {
        this.photo = spot.getPhoto();
        this.spotName = spot.getSpotName();
        this.tag = spot.getTag();
        this.spotType = spot.getSpotType();
        this.location = spot.getLocation();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public void setPhoto(byte[] imageBytes) {
        Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        Bitmap mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
        this.setPhoto(mutableBitmap);//cameraImage.setImageBitmap(mutableBitmap);
    }

    public String getSpotID() {
        return spotID;
    }

    public void setSpotID(String spotID) {
        this.spotID = spotID;
    }

    public void setSpotID(int spotID) {
        this.spotID = Integer.toString(spotID);
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }

    public String getSpotType() {
        return spotType;
    }

    public void setSpotType(String spotType) {
        this.spotType = spotType;
    }

    public void setDefaultType() {
        this.setSpotType("General");
    }


    public byte[] getByteArray() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
        return bos.toByteArray();
    }

    public void setSpotValue(String value, String definition) {
        switch (definition) {
            case "spotID":
                this.spotID = value;
                break;
            case "spotType":
                this.spotType = value;
                break;
            case "tag":
                this.tag = value;
                break;
            case "location":
                this.location = value;
                break;
            case "spotName":
                this.spotName = value;
                break;
        }
    }

}
