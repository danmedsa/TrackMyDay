package com.team404.trackmyday;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Carl Carter on 11/26/2016.
 */
@IgnoreExtraProperties
public class User {
    public String Ping;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email) {
        Ping = email;

    }

}
