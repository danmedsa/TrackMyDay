package com.team404.trackmyday;

/**
 * Created by DanielMedina on 11/15/16.
 */

public class UserActivity {
    public String activity;
    public Integer times;

    UserActivity(String activity){
        this.activity = activity;
        this.times = 0;
    }

    void addActivity(){
        times++;
    }
}
