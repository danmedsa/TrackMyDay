package com.team404.trackmyday;

import java.util.ArrayList;

/**
 * Created by DanielMedina on 11/15/16.
 */

public class UserActivity {
    public String activity;
    public ArrayList<LocationEntity> locations;

    UserActivity(String activity){
        this.activity = activity;
    }

    void addLocation(LocationEntity location){
        if (locations == null){
            locations = new ArrayList<LocationEntity>();
            locations.add(location);
        }else{
            locations.add(location);
        }
    }
}
