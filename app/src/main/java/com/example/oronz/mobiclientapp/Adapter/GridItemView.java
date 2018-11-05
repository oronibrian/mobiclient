package com.example.oronz.mobiclientapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.oronz.mobiclientapp.R;

public class GridItemView extends FrameLayout {

    private TextView textView;

    public GridItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.grid_item, this);
        textView = (TextView) getRootView().findViewById(R.id.txt_grid);
    }

    public void display(String text, boolean isSelected) {
        textView.setText(text);
        display(isSelected);
    }

    public void display(boolean isSelected) {
        textView.setBackgroundResource(isSelected ? R.drawable.ic_seats_b : R.drawable.ic_seats_empty_seats);
    }
}