package com.team404.trackmyday;

import java.util.Date;

/**
 * Created by DanielMedina on 11/15/16.
 */

public class LocationEntity {
    public double latitude, longitude;
    public String name;
    public Date date;
    LocationEntity(double lat, double lon, String name, Date date){
        this.latitude = lat;
        this.longitude = lon;
        this.name = name;
        this.date = date;
    }
}
