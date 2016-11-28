package com.team404.trackmyday;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by James on 11/27/2016.
 */

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnDaily, btnWeekly, btnMonthly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);

        btnDaily = (Button) findViewById(R.id.btn_daily_report);
        btnWeekly = (Button) findViewById(R.id.btn_weekly_report);
        btnMonthly = (Button) findViewById(R.id.btn_monthly_report);

        btnDaily.setOnClickListener(this);
        btnWeekly.setOnClickListener(this);
        btnMonthly.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_daily_report:
                displayDailyReport();
                break;
            case R.id.btn_weekly_report:
                displayWeeklyReport();
                break;
            case R.id.btn_monthly_report:
                displayMonthlyReport();
                break;
        }
    }

    private void displayToast(String s)
    {
        Toast toast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM,0,0);
        toast.show();
    }

    private void displayDailyReport(){
        displayToast("Display Daily Report");
        GeneratedReport temp = new GeneratedReport();
        temp.getLocations("James - Testing", "DAILY");
    }

    private void displayWeeklyReport(){
        displayToast("Display Weekly Report");
        GeneratedReport temp = new GeneratedReport();
        temp.getLocations("James - Testing", "WEEKLY");
    }

    private void displayMonthlyReport(){
        displayToast("Display Monthly Report");
        GeneratedReport temp = new GeneratedReport();
        temp.getLocations("James - Testing", "MONTHLY");
    }
}
