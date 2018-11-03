package com.example.oronz.mobiclientapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReceiptActivity extends AppCompatActivity {
TextView txt_name,txt_seat_no,date,journey,txt_phone;
Button btnnew;
    MobiClientApplication app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        app = (MobiClientApplication) getApplication();

        txt_name=findViewById(R.id.txt_name);
        txt_seat_no=findViewById(R.id.seat_no);
        date=findViewById(R.id.date);
        journey=findViewById(R.id.journey);
        btnnew=findViewById(R.id.btnnew);



//        journey.setText(String.format("Travelling from %s To %s", app.getTravel_from(), app.getTravel_too()));
////        txt_name.setText(String.format("Name \n%s", app.getName()));
//        txt_seat_no.setText(String.format("Seat No \n%s", app.getSeatNo()));
//        date.setText(String.format("Date \n%s", app.getTravel_date()));

        String value = getIntent().getStringExtra("data");

        txt_name.setText(String.format("Message \n%s", value));

        btnnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

    }
}
