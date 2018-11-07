package com.example.oronz.mobiclientapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReceiptActivity extends AppCompatActivity {
TextView txt_name,txt_status;
Button btnnew;
    MobiClientApplication app;
    List<String> myList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        app = (MobiClientApplication) getApplication();

        txt_name=findViewById(R.id.txt_name);
        txt_status=findViewById(R.id.txt_status);
        btnnew=findViewById(R.id.btnnew);



//        journey.setText(String.format("Travelling from %s To %s", app.getTravel_from(), app.getTravel_too()));
////        txt_name.setText(String.format("Name \n%s", app.getName()));
//        txt_seat_no.setText(String.format("Seat No \n%s", app.getSeatNo()));
//        date.setText(String.format("Date \n%s", app.getTravel_date()));


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            myList = bundle.getStringArrayList("Success"); // declare temp as ArrayList


                String value = getIntent().getStringExtra("data");
                String status = getIntent().getStringExtra("txt_status");

                txt_name.setText(value);
                txt_status.setText(status);
            

        }





        btnnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

    }
}
