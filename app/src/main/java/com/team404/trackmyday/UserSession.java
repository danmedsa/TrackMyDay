package com.team404.trackmyday;

/**
 * Created by DanielMedina on 11/15/16.
 */

public class UserSession {

    private String email;
    private UserSession session;

    private UserSession(String email){
        this.email = email;

    }

    public UserSession getInstance(String email){
        if (session == null){
            session = new UserSession(email);
        }
        return session;
    }

}
