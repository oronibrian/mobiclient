package com.example.oronz.mobiclientapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oronz.mobiclientapp.R;
import com.example.oronz.mobiclientapp.Seats_activity;

import java.util.List;

public class CustomGridAdapter extends BaseAdapter {
    private String[] eleven_seater = {

            "1",
            "1X",
            "0",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "10",
    };

    String[] seat_numbers;

    private Context mContext;
    private LayoutInflater layoutInflater;

    public CustomGridAdapter(Context context, String[] seat_nums) {
        this.mContext = context;
        this.layoutInflater=LayoutInflater.from(mContext);
        this.seat_numbers=seat_nums;
    }

    @Override
    public int getCount() {
        return eleven_seater.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
                convertView=layoutInflater.inflate(R.layout.grid_item,parent,false);
                TextView grid_text=convertView.findViewById(R.id.txt_grid);
                grid_text.setText(eleven_seater[position]);



        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(mContext, "You Clicked "+eleven_seater[position], Toast.LENGTH_LONG).show();

            }
        });


        return convertView;
    }
}