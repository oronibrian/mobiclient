package com.example.oronz.mobiclientapp.Fragemnts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dd.CircularProgressButton;
import com.example.oronz.mobiclientapp.R;

public class OneWayTripFragement extends Fragment {

    Button search1;

    public OneWayTripFragement() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View V =inflater.inflate(R.layout.fragemnet_oneway_trip, container, false);


        return V;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        search1 =view.findViewById(R.id.searchbtn1);
        search1.setOnClickListener(v -> {

        });
    }

}
