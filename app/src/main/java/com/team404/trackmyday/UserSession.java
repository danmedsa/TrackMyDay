package com.team404.trackmyday;

import android.util.Log;

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
        //User account = new User(cleanUpEmail(email));
        //final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //final DatabaseReference myRef = database.getReference().child("Users").child(cleanUpEmail(email));
    }

    public String getUser(){
        return this.user;
    }

    private String cleanUpEmail(String email) {
        Log.e("DatabaseKey",email);

        String temp = "";
        int i = 0;
        while(email.charAt(i) != '@'){
            temp += email.charAt(i);
            i++;
        }
        Log.e("DatabaseKey",temp);
        return temp;
    }
}
