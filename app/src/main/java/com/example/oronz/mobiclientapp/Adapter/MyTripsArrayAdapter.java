package com.example.oronz.mobiclientapp.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.oronz.mobiclientapp.MobiClientApplication;
import com.example.oronz.mobiclientapp.Models.AvailableVehicles;
import com.example.oronz.mobiclientapp.Models.MytripsDetails;
import com.example.oronz.mobiclientapp.R;

import java.util.ArrayList;

public class MyTripsArrayAdapter extends ArrayAdapter<MytripsDetails> {
    private MobiClientApplication app;

    public MyTripsArrayAdapter(Activity context, ArrayList<MytripsDetails> packages) {


        super(context, 0, packages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        app = (MobiClientApplication) getContext().getApplicationContext();

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.my_trip_list, parent, false);
        }

        MytripsDetails mytripsDetails = getItem(position);

        TextView nameTextView = listItemView.findViewById(R.id.car_name);

        nameTextView.setText(mytripsDetails.getTravel_from());

        TextView numberTextView = listItemView.findViewById(R.id.travel_date);

        numberTextView.setText(mytripsDetails.getReference_number());


        TextView fromTextView = listItemView.findViewById(R.id.from);

        fromTextView.setText(mytripsDetails.getTravel_date());


        TextView tooTextView = listItemView.findViewById(R.id.too);

        tooTextView.setText(mytripsDetails.getTravel_to());


        TextView amountTextView = listItemView.findViewById(R.id.textView2);

        amountTextView.setText(mytripsDetails.getTransport_company());


        TextView seateTextView = listItemView.findViewById(R.id.seatsbooked);

        seateTextView.setText(mytripsDetails.getAmount());



        return listItemView;
    }





}
