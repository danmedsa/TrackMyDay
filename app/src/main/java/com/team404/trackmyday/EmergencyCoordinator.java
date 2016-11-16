package com.team404.trackmyday;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by James on 11/14/2016.
 */

public class EmergencyCoordinator {

    private String contactName;
    private String contactNumber;


    public void getContactName(){
        //get contact name from database
        //store into variable contactName
    }

    public void getContactNumber(){
        //get contact's phone number from database
        //store into variable contactNumber
    }

    public void setContactName(String name){
        this.contactName = name;
    }

    public void setContactNumber(String number){
        this.contactNumber = number;
        //storeContact();
    }

    public void storeContact(Context context, String name, String numb){
        //store new contact into database
        SharedPreferences settings = context.getSharedPreferences("ContactInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Name", name);
        editor.putString("Number", numb);
        editor.commit();// commit is important here.
        Toast.makeText(context, name+": " + numb + " is now Emergency Contact", Toast.LENGTH_SHORT).show();

    }

    public void deleteContact(){
        //User selects which contact to delete
        //Delete contact from database
    }

    public void activateEmergency(Context context){
        //Ask user to confirm the sending of the emergency message

        //If user confirms, send emergency message
        SharedPreferences settings = context.getSharedPreferences("ContactInfo", 0);
        String name = settings.getString("Name", "");
        String numb = settings.getString("Number", "");

        Toast.makeText(context, name+": " + numb + " Has been Alerted", Toast.LENGTH_LONG).show();

        //If user cancels, don't send message and return to MainActivity
    }
}
