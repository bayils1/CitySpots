package com.application.cityspots;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    /*Variables*/
    private String fullName;
    private String username;
    private String password;
    private String city;
    private String userID;
    private List<Spot> spotList;


    /*Constructors*/
    public User() {
    }

    public User(String fn, String un, String p, String c) {
        this.fullName = fn;
        this.username = un;
        this.password = p;
        this.city = c;
    }

    /*Getters/Setters*/
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /*Methods*/

    public void setUserValue(String value, String definition) {
        switch (definition) {
            case "userID":
                this.userID = value;
                break;
            case "username":
                this.username = value;
                break;
            case "password":
                this.password = value;
                break;
            case "fullName":
                this.fullName = value;
                break;
            case "city":
                this.city = value;
                break;
        }
    }
}
