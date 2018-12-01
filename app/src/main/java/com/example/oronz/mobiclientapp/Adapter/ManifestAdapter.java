package com.example.oronz.mobiclientapp.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.oronz.mobiclientapp.MobiClientApplication;
import com.example.oronz.mobiclientapp.Models.ManifestDetails;
import com.example.oronz.mobiclientapp.R;

import java.util.ArrayList;

public class ManifestAdapter extends ArrayAdapter<ManifestDetails> {
private MobiClientApplication app;

    public ManifestAdapter(Activity context, ArrayList<ManifestDetails> packages) {


        super(context, 0, packages);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        app = (MobiClientApplication) getContext().getApplicationContext();

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.manifest_list, parent, false);
        }

        ManifestDetails mytripsDetails = getItem(position);

        TextView routeTextView = listItemView.findViewById(R.id.routeTextView);

        routeTextView.setText(mytripsDetails.getRoute());


        TextView seatsTextView = listItemView.findViewById(R.id.manifestseats);

        seatsTextView.setText("Total Seats: "+mytripsDetails.getTotal_seats());

        TextView availabeseatsTextView = listItemView.findViewById(R.id.manifestavilableseats);

        availabeseatsTextView.setText("Available Seats: "+mytripsDetails.getSeats_available());

        return listItemView;
    }


}
