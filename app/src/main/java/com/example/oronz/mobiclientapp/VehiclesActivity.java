package com.example.oronz.mobiclientapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.oronz.mobiclientapp.Adapter.MyAdapter;

import java.util.ArrayList;

public class VehiclesActivity extends AppCompatActivity {
    ArrayList<String> buses =new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);
        fillList();

        recyclerView = findViewById(R.id.vertical_recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        MyAdapter adapter=new MyAdapter(this,buses);
        recyclerView.setAdapter(adapter);


    }

    private void fillList() {
//String x =getIntent(). getStringExtra("Buses");
        ArrayList<String> myArray;
        myArray =getIntent().getStringArrayListExtra("Buses");

//        buses.addAll(args.getStringArrayList("buses"));

        Log.v("vehicles","vehicles no"+ myArray);

    }
}
