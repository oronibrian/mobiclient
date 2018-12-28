package com.example.oronz.mobiclientapp.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.oronz.mobiclientapp.MobiClientApplication;
import com.example.oronz.mobiclientapp.Models.PassengerManifestDetails;
import com.example.oronz.mobiclientapp.R;

import java.util.ArrayList;

public class PassengerManifestAdapter extends ArrayAdapter<PassengerManifestDetails> {
    private MobiClientApplication app;

    public PassengerManifestAdapter(Activity context, ArrayList<PassengerManifestDetails> packages) {


        super(context, 0, packages);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        app = (MobiClientApplication) getContext().getApplicationContext();

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.vehicle_single_manifest_list, parent, false);
        }

        PassengerManifestDetails mytripsDetails = getItem(position);

        TextView manifest_pass_nameTextView = listItemView.findViewById(R.id.manifest_pass_name);

        manifest_pass_nameTextView.setText(mytripsDetails.getName());


        TextView seatsTextView = listItemView.findViewById(R.id.manifest_seat_no);

        seatsTextView.setText("Seat: "+mytripsDetails.getSeat());

        TextView manifest_ref_noTextView = listItemView.findViewById(R.id.manifest_ref_no);

        manifest_ref_noTextView.setText("Reference: "+mytripsDetails.getRefno());


        TextView regnoTextView = listItemView.findViewById(R.id.manifest_reg_no);

        regnoTextView.setText("Reg No: "+mytripsDetails.getReg_no());


        return listItemView;
    }


}
