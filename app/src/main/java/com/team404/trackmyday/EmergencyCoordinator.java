package com.team404.trackmyday;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

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
            Twilio.init(String.valueOf(R.string.twilio_SID), String.valueOf(R.string.twilio_Auth));

            Message message = Message.creator(new PhoneNumber("+18063005006"),
                    new PhoneNumber("+18066865052"),
                    "TrackMyDay: This is an Emergency").create();

            System.out.println(message.getSid());

        //If user cancels, don't send message and return to MainActivity
    }
}
