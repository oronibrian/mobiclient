package com.example.oronz.mobiclientapp.Adapter;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.oronz.mobiclientapp.Models.AvailableVehicles;
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
        RecyclerView.ViewHolder viewHolder; // view lookup cache stored in tag

        app = (MobiClientApplication) getContext().getApplicationContext();

        if (listItemView == null) {
            LayoutInflater li = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//            listItemView = LayoutInflater.from(getContext()).inflate(
//                    R.layout.car_list, parent, false);

            listItemView = li.inflate(R.layout.car_list, null);

        }
        else {
// View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (RecyclerView.ViewHolder) convertView.getTag();
        }


        AvailableVehicles currentpackage = getItem(position);
//
//        TextView numberTextView = listItemView.findViewById(R.id.seat_number);
//
//        numberTextView.setText(currentpackage.getSeater());

        TextView availableTextView = listItemView.findViewById(R.id.available_seats);

        availableTextView.setText(String.format(String.format("Seater: %s Available : %%s", currentpackage.getName()), currentpackage.getSeats_available()));

//        TextView departureTextView = listItemView.findViewById(R.id.departure_time);
//
//        departureTextView.setText(currentpackage.getDeparture_time());


        TextView seater= listItemView.findViewById(R.id.seater);

        seater.setText(currentpackage.getName());

        TextView id = listItemView.findViewById(R.id.available);
        id.setText(currentpackage.get_Id());




        return listItemView;
    }



}
