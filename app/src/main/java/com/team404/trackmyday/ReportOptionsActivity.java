package com.team404.trackmyday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by James on 11/27/2016.
 */

//Activity for choosing between Daily, Weekly, or Monthly Report
public class ReportOptionsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnDaily, btnWeekly, btnMonthly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_options);

        btnDaily = (Button) findViewById(R.id.btn_daily_report);
        btnWeekly = (Button) findViewById(R.id.btn_weekly_report);
        btnMonthly = (Button) findViewById(R.id.btn_monthly_report);

        btnDaily.setOnClickListener(this);
        btnWeekly.setOnClickListener(this);
        btnMonthly.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)  {
        int id = view.getId();

        //Request a report based on the user's selection
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

    //Request to Display Daily Report
    private void displayDailyReport() {
        Intent i = new Intent(ReportOptionsActivity.this, GeneratedReport.class);
        i.putExtra("Email", "James - Testing");
        i.putExtra("Period", "DAILY");
        startActivity(i);

    }

    //Request to display Weekly Report
    private void displayWeeklyReport(){
        Intent i = new Intent(ReportOptionsActivity.this, GeneratedReport.class);
        i.putExtra("Email", "James - Testing");
        i.putExtra("Period", "WEEKLY");
        startActivity(i);

    }

    //Request to display Monthly Report
    private void displayMonthlyReport(){
        Intent i = new Intent(ReportOptionsActivity.this, GeneratedReport.class);
        i.putExtra("Email", "James - Testing");
        i.putExtra("Period", "MONTHLY");
        startActivity(i);
    }

}
