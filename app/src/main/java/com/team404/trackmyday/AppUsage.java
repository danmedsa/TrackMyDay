package com.team404.trackmyday;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Carl Carter on 11/27/2016.
 */

public class AppUsage extends AppCompatActivity {
    private Button foreground_btn;
    private TextView appname_display;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.appusage);

        foreground_btn = (Button) findViewById(R.id.foreground_btn);
        appname_display = (TextView) findViewById(R.id.appname_display);

        //AppPackages();
        configureButton();
    }

    private void configureButton() {
        foreground_btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                long milliUptime = SystemClock.uptimeMillis();
                long secUptime = milliUptime/1000;
                long minUptime = secUptime/60;
                long hourUptime = minUptime/60;
                long dayUptime = hourUptime/24;
                appname_display.setText(Long.toString(secUptime)+" Active Phone Usage In Seconds\n"+
                        Long.toString(minUptime)+ " Active Phone Usage in Minutes\n"+
                        Long.toString(hourUptime)+ " Active Phone Usage in Hours\n"+
                        Long.toString(dayUptime)+ " Active Phone Usage in Days\n");

            }
        });
    }

    private void AppPackages(){
        final PackageManager pm = getPackageManager();
        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
                appname_display.append(packageInfo.packageName + "\n");
                Log.d("Message1", "Installed package :" + packageInfo.packageName);

            // Log.d("Message2", "Source dir : " + packageInfo.sourceDir);
            //Log.d("Message3", "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
        }

    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                : false;
    }

}