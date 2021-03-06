package com.example.oronz.mobiclientapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.oronz.mobiclientapp.MobiClientApplication;
import com.example.oronz.mobiclientapp.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> implements Filterable {

    Context c;
    ArrayList<String> buses,filterList;
    CustomFilter filter;
    private MobiClientApplication app;

    public MyAdapter(Context c, ArrayList<String> buses) {
        this.c = c;
        this.buses = buses;
        this.filterList=buses;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.cards_layout, parent, false);
        app = (MobiClientApplication) v.getContext();

        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //BIND DATA
        holder.textViewName.setText(buses.get(position));



    }

    @Override
    public int getItemCount() {
        return buses.size();
    }




    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new CustomFilter(filterList,this);
        }

        return filter;
    }
}