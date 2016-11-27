package com.team404.trackmyday;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by James on 11/14/2016.
 */

public class EmergencyCoordinator {

    private String contactName;
    private String contactNumber;
    public static final String ACCOUNT_SID = "ACb0148208ff73cc79ce0d344a8623c5e2";
    public static final String AUTH_TOKEN = "007c0dac96738487cd252d27e03906a7";


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


        //Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        //Message message = Message.creator(new PhoneNumber(numb),
         //       new PhoneNumber("+18066865052"),
         //       "TrackMyDay: This is an Emergency").create();

        //System.out.println(message.getSid());

        sendSms(numb,"TrackMyDay: This is an Emergency");

        //If user cancels, don't send message and return to MainActivity
    }

    private void sendSms(String toPhoneNumber, String message){
        final String numb = toPhoneNumber;
        final String msg = message;
        new Thread(new Runnable() {
            public void run() {
                OkHttpClient client = new OkHttpClient();
                String url = "https://api.twilio.com/2010-04-01/Accounts/"+ACCOUNT_SID+"/SMS/Messages";
                String base64EncodedCredentials = "Basic " + Base64.encodeToString((ACCOUNT_SID + ":" + AUTH_TOKEN).getBytes(), Base64.NO_WRAP);

                RequestBody body = new FormBody.Builder()
                        .add("From", "+18066865052")
                        .add("To", numb)
                        .add("Body", msg)
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .header("Authorization", base64EncodedCredentials)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    Log.d("Emergency Coordinator", "sendSms: "+ response.body().string());
                } catch (IOException e) { e.printStackTrace(); }
            }
        }).start();


    }
}
