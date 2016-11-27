package com.team404.trackmyday;

/**
 * Created by DanielMedina on 11/15/16.
 */

public class UserSession {
    private static UserSession instance = null;
    private String user;

    protected UserSession() {
        // Exists only to defeat instantiation.
    }
    public static UserSession getInstance() {
        if(instance == null) {
            instance = new UserSession();
        }
        return instance;
    }


    public void setUser(String email){
        this.user = email;
    }

    public String getUser(){
        return this.user;
    }
}
