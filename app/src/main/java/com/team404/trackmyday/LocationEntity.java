package com.team404.trackmyday;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by DanielMedina on 11/15/16.
 */

public class LocationEntity {
    public double latitude, longitude;
    public String name;
    public Date date;

    //Count diff activities at same location to get most probable activity
    public ArrayList<String> Activities = new ArrayList<String>();
    private HashMap<String, Integer> guessing = new HashMap<String, Integer>();

    LocationEntity(double lat, double lon, String name, Date date){
        this.latitude = lat;
        this.longitude = lon;
        this.name = name;
        this.date = date;
    }

    //Count diff activities at same location to get most probable activity
    public void addActivity(String activity){
        if(!guessing.containsKey(activity)){
            guessing.put(activity,1);
        }else{
            Integer temp = guessing.get(activity);
            temp += 1;
            guessing.put(activity,temp);
        }
    }
}
