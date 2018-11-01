package com.example.oronz.mobiclientapp.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.oronz.mobiclientapp.AvailableVehicles;
import com.example.oronz.mobiclientapp.MobiClientApplication;
import com.example.oronz.mobiclientapp.R;

import java.util.ArrayList;

public class VehicleArrayAdapter  extends ArrayAdapter<AvailableVehicles> {

    private MobiClientApplication app;


    public VehicleArrayAdapter(Activity context, ArrayList<AvailableVehicles> packages) {

        super(context, 0, packages);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        app = (MobiClientApplication) getContext().getApplicationContext();

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.car_list, parent, false);
        }

        AvailableVehicles currentpackage = getItem(position);

        TextView nameTextView = listItemView.findViewById(R.id.name);

        nameTextView.setText(String.format("Total Seats : %s", currentpackage.getName()));

        TextView numberTextView = listItemView.findViewById(R.id.seat_number);

        numberTextView.setText(currentpackage.getSeater());

        TextView availableTextView = listItemView.findViewById(R.id.available_seats);

        availableTextView.setText(String.format("Available : %s", currentpackage.getSeats_available()));

        TextView departureTextView = listItemView.findViewById(R.id.departure_time);

        departureTextView.setText(currentpackage.getDeparture_time());




        return listItemView;
    }



}
