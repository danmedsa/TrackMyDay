package com.team404.trackmyday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.team404.trackmyday.R;
import com.team404.trackmyday.UserLocationModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by James on 11/30/2016.
 */

//Custom Adapter needed in order to have custom ListView for Reports
public class CustomReportAdapter extends BaseAdapter {
    Context c;
    ArrayList<UserLocationModel> locations = new ArrayList<UserLocationModel>();

    public CustomReportAdapter(Context c, ArrayList<UserLocationModel> locations){
        this.c = c;
        this.locations = locations;
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public Object getItem(int position) {
        return locations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
            convertView = LayoutInflater.from(this.c).inflate(R.layout.report_model,parent,false);


        TextView txtLocationName = (TextView) convertView.findViewById(R.id.location_name);
        TextView txtLat = (TextView) convertView.findViewById(R.id.txtLat);
        TextView txtLong = (TextView) convertView.findViewById(R.id.txtLong);
        TextView txtDate = (TextView) convertView.findViewById(R.id.txtDate);
        TextView txtTime = (TextView) convertView.findViewById(R.id.txtTime);
        TextView txtDuration = (TextView) convertView.findViewById(R.id.txtDuration);

        final UserLocationModel location = (UserLocationModel) this.getItem(position);

        if(location.getName() == null) {
            txtLocationName.setVisibility(View.GONE);
            txtLat.setPadding(0,10,0,0);
            txtLong.setPadding(0,10,0,0);
        }else{
            txtLocationName.setText(location.getName());
            txtLocationName.setTextColor(c.getResources().getColor(R.color.colorPrimaryDark));

        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(8);

        txtLat.setText("Latitude: " + df.format(location.getLatitude()));
        txtLong.setText("Longitude: "+ df.format(location.getLongitude()));
        txtDate.setText("Date Visited: " + location.getDateString());
        txtTime.setText("Time Visited: " + location.getTime());


        df.setMaximumFractionDigits(2);
        if(location.getDuration() > 5) {
            txtDuration.setText("Duration: " + df.format(location.getDuration()) + " minutes");
            txtDuration.setTextColor(c.getResources().getColor(R.color.colorPrimaryDark));
        }
        else {
            txtDuration.setVisibility(View.GONE);
            txtTime.setPadding(0,0,0,10);

        }
        txtLat.setTextColor(c.getResources().getColor(R.color.colorPrimaryDark));
        txtLong.setTextColor(c.getResources().getColor(R.color.colorPrimaryDark));
        txtDate.setTextColor(c.getResources().getColor(R.color.colorPrimaryDark));
        txtTime.setTextColor(c.getResources().getColor(R.color.colorPrimaryDark));
        txtDuration.setTextColor(c.getResources().getColor(R.color.colorPrimaryDark));

        return convertView;
    }
}
