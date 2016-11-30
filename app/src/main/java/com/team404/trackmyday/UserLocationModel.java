package com.team404.trackmyday;

import java.util.ArrayList;

/**
 * Created by Feyi Oyewole on 11/26/16.
 */


public class UserLocationModel {

    private double latitude;
    private double longitude;
    private String dateString;
    private ArrayList<UserActivity> activityList;
    private String time;

    public UserLocationModel(double latitude, double longitude, String dateString, String time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateString = dateString;
        this.time = time;
        this.activityList = new ArrayList<UserActivity>();
    }


    public UserLocationModel() {

    }

    //Retrieve data and store for map ping
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void addActivities(UserActivity activityList){
        this.activityList.add(activityList);
    }

    public ArrayList<UserActivity> getActivityList(){
        return this.activityList;
    }
}
