package com.example.oronz.mobiclientapp.Adapter;

import android.content.ClipData;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oronz.mobiclientapp.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView textViewName;
    public View view;

    public MyViewHolder(View itemView) {
        super(itemView);
        view = itemView;

        textViewName = itemView.findViewById(R.id.textViewName);

    }
}