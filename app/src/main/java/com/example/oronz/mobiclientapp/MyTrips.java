package com.example.oronz.mobiclientapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.oronz.mobiclientapp.API.URLs;
import com.example.oronz.mobiclientapp.Adapter.MyTripsArrayAdapter;
import com.example.oronz.mobiclientapp.Adapter.VehicleArrayAdapter;
import com.example.oronz.mobiclientapp.Models.AvailableVehicles;
import com.example.oronz.mobiclientapp.Models.MytripsDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyTrips extends AppCompatActivity {
    MobiClientApplication app;
    ArrayList<MytripsDetails> mytripsDetails;
    ListView mytripslistView ;
    EditText editTextphone;
    Button btnsearch;
    String phoneno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MobiClientApplication) getApplication();

        setContentView(R.layout.activity_my_trips);

        mytripslistView = (ListView) findViewById(R.id.mytripslistView);

        mytripsDetails = new ArrayList<MytripsDetails>();

        editTextphone=findViewById(R.id.editTextphone);
        btnsearch=findViewById(R.id.btnSearch);



         btnsearch.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (editTextphone.length() < 10 && editTextphone.length() > 10) {
                     editTextphone.setError("Please Enter valid phone number");

                 } else {
                     phoneno = editTextphone.getText().toString();
                     mytripsDetails = new ArrayList<MytripsDetails>();

                     getTickets();

                 }

             }
         });


    }


    private void getTickets() {
        RequestQueue requestQueue = Volley.newRequestQueue(MyTrips.this);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "SearchTicket");
        params.put("identifier", phoneno);

        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getInt("response_code") == 0) {
                                JSONArray jsonArray = response.getJSONArray("tickets");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String car_name = jsonObject1.getString("vehicle_name");
                                    String travel_from = jsonObject1.getString("travel_from");
                                    String travel_to = jsonObject1.getString("travel_to");
                                    String travel_date = jsonObject1.getString("travel_date");
                                    String reference_number = jsonObject1.getString("reference_number");
                                    String amount = jsonObject1.getString("amount");
                                    String transport_company = jsonObject1.getString("transport_company");

                                    mytripsDetails.add(new MytripsDetails(car_name,travel_from,travel_to,travel_date,reference_number,amount,transport_company));

                                }

                            } else {
                                Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

                            }

                            MyTripsArrayAdapter tripsArrayAdapter = new MyTripsArrayAdapter(MyTrips.this, mytripsDetails);

                            mytripslistView.setAdapter(tripsArrayAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        })

        {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }


        };

        requestQueue.add(req);

    }

}
