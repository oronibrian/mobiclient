package com.example.oronz.mobiclientapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.oronz.mobiclientapp.MobiClientApplication;
import com.example.oronz.mobiclientapp.Models.AvailableVehicles;
import com.example.oronz.mobiclientapp.Models.ReceiptItems;
import com.example.oronz.mobiclientapp.R;

import java.util.ArrayList;

public class ReceiptItemsAdapter extends ArrayAdapter<ReceiptItems> {

    private MobiClientApplication app;

    public ReceiptItemsAdapter(Activity context, ArrayList<ReceiptItems> packages) {

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

            listItemView = li.inflate(R.layout.receipt_items, null);

        }
        else {
// View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (RecyclerView.ViewHolder) convertView.getTag();
        }


        ReceiptItems currentpackage = getItem(position);


        TextView availableTextView = listItemView.findViewById(R.id.passengerIdtxt);
        availableTextView.setText(currentpackage.getName());


        TextView txtpriceTextView = listItemView.findViewById(R.id.txtprice);
        txtpriceTextView.setText("seat: "+currentpackage.getSeat());


        TextView txtisnumberTextView = listItemView.findViewById(R.id.txtisnumber);
        txtisnumberTextView.setText(currentpackage.getPhone());



        TextView txviewdateTextView = listItemView.findViewById(R.id.txviewdate);
        txviewdateTextView.setText(currentpackage.getTravel_date());




        TextView txttimeTextView = listItemView.findViewById(R.id.txttime);
        txttimeTextView.setText(currentpackage.getTravel_time());



        return listItemView;
    }



}
