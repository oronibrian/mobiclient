package com.example.oronz.mobiclientapp.Adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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