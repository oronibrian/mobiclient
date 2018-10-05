package com.example.oronz.mobiclientapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.oronz.mobiclientapp.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView textViewName;

    public MyViewHolder(View itemView) {
        super(itemView);

        textViewName = itemView.findViewById(R.id.textViewName);

    }

}