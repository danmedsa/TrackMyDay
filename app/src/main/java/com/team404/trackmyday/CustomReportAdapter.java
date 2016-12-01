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


        TextView txtLatLong = (TextView) convertView.findViewById(R.id.txtLatLong);
        TextView txtDate = (TextView) convertView.findViewById(R.id.txtDate);
        TextView txtTime = (TextView) convertView.findViewById(R.id.txtTime);

        final UserLocationModel location = (UserLocationModel) this.getItem(position);

        txtLatLong.setText("Latitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude());
        txtDate.setText("Date Visited: " + location.getDateString());
        txtTime.setText("Time Visited: " + location.getTime());

        txtLatLong.setTextColor(c.getResources().getColor(R.color.colorPrimaryDark));
        txtDate.setTextColor(c.getResources().getColor(R.color.colorPrimaryDark));
        txtTime.setTextColor(c.getResources().getColor(R.color.colorPrimaryDark));

        return convertView;
    }
}
