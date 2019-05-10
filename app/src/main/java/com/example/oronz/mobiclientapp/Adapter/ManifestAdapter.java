package com.example.oronz.mobiclientapp.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.oronz.mobiclientapp.MobiClientApplication;
import com.example.oronz.mobiclientapp.Models.ManifestDetails;
import com.example.oronz.mobiclientapp.R;

import java.util.ArrayList;
import java.util.List;



public class ManifestAdapter extends ArrayAdapter<ManifestDetails>   {
    private MobiClientApplication app;

    List<ManifestDetails> mStringFilterList;
    private List<ManifestDetails> beanList;

    ValueFilter valueFilter;


    public ManifestAdapter(Activity context, ArrayList<ManifestDetails> packages) {


        super(context, 0, packages);
        mStringFilterList = beanList;

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


        TextView seatsTextView = listItemView.findViewById(R.id.manifestavilableseats);

        seatsTextView.setText("Seater" +
                ": "+mytripsDetails.getTotal_seats());

        TextView availabeseatsTextView = listItemView.findViewById(R.id.manifestavilableseats);

        availabeseatsTextView.setText("Available: "+mytripsDetails.getSeats_available());

        return listItemView;
    }



    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<ManifestDetails> filterList = new ArrayList<ManifestDetails>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getRoute().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {

                        ManifestDetails bean = new ManifestDetails(mStringFilterList.get(i)
                                .getRoute(), mStringFilterList.get(i)
                                .getTotal_seats(),mStringFilterList.get(i).getTotal_seats());
                        filterList.add(bean);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            beanList = (ArrayList<ManifestDetails>) results.values;
            notifyDataSetChanged();
        }

        }


    }
