package com.example.oronz.mobiclientapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.oronz.mobiclientapp.Adapter.MyRecyclerViewAdapter;



public class Seat_layout_Activity extends AppCompatActivity  {

    int numberOfColumns = 5;
    MyRecyclerViewAdapter adapter;



    String[] fortynineSeater = new String[]{

            "1A", "2A", "C", "1B", "2B",
            "3A", "4A", "C", "3B", "4B",
            "5A", "6A", "C", "5B", "6B",
            "7A", "8A", "C", "7B", "8B",
            "9A", "9B", "C", "10A", "10B",
            "11A", "11B", "C", "12A", "12B",
            "13A", "14A", "C", "13B", "14B",
            "15A", "16A", "C", "15B", "16B",
            "17A", "18A", "C", "17B", "18B",
            "19A", "20A", "C", "19B", "20B",
            "21A", "22A", "C", "21B", "22B",
            "23A", "24A", "25", "23B", "24B",

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_layout_);




        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lst_items);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MyRecyclerViewAdapter(this, fortynineSeater);
        recyclerView.setAdapter(adapter);






    }


}