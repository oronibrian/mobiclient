package com.example.oronz.mobiclientapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.oronz.mobiclientapp.MobiClientApplication;
import com.example.oronz.mobiclientapp.Models.City;
import com.example.oronz.mobiclientapp.R;

import java.util.ArrayList;
import java.util.List;

public class CityArrayAdapter extends ArrayAdapter<City> {
    private MobiClientApplication app;

        private Context mContext;
        private List<City> cityList = new ArrayList<>();

        public CityArrayAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<City> list) {
            super(context, 0 , list);
            mContext = context;
            cityList = list;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            app = (MobiClientApplication) getContext().getApplicationContext();

            View listItem = convertView;
            if(listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.city_item,parent,false);

            City currentcity = cityList.get(position);

            TextView name = (TextView) listItem.findViewById(R.id.textView_name);
            name.setText(currentcity.getName());

            TextView release = (TextView) listItem.findViewById(R.id.textView_id);
            release.setText(currentcity.getId());
            app.setCity_id(currentcity.getId());

            return listItem;
        }
    }


