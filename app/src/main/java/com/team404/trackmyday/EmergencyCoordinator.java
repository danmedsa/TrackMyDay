package com.team404.trackmyday;

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
        storeContact();
    }

    public void storeContact(){
        //store new contact into database
    }

    public void deleteContact(){
        //User selects which contact to delete
        //Delete contact from database
    }

    public void activateEmergency(){
        //Ask user to confirm the sending of the emergency message

        //If user confirms, send emergency message
            
        //If user cancels, don't send message and return to MainActivity
    }
}
