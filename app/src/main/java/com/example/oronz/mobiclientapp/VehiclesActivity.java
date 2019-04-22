package com.example.oronz.mobiclientapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.oronz.mobiclientapp.API.URLs;
import com.example.oronz.mobiclientapp.Adapter.MyAdapter;
import com.example.oronz.mobiclientapp.Adapter.VehicleArrayAdapter;
import com.example.oronz.mobiclientapp.Models.AvailableVehicles;
import com.example.oronz.mobiclientapp.Utilities.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class VehiclesActivity extends AppCompatActivity {
    public static String name, phone, id_no = "";
    ArrayList<String> vehicles, payment_methods;
    RecyclerView recyclerView;
    MyAdapter adapter;
    String buses;
    JSONArray jsonArray;
    SearchView sv;
    String car_id="";
    String total_seats="";

    private MobiClientApplication app;

    ArrayList<AvailableVehicles> availableVehicles;
    ListView listView ;
    Context mcontext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);
        app = (MobiClientApplication) getApplication();
        vehicles = new ArrayList<>();
        payment_methods = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listview);

        availableVehicles = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String today = dateFormat.format(new Date());

        app.setTravel_date(today);
        mcontext=getApplicationContext();


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);



                listView.setOnItemClickListener((parent, view, position, id) -> {

                    TextView seatssize = view.findViewById(R.id.seater);
                    String number=seatssize.getText().toString();


                    TextView available = view.findViewById(R.id.available);
                    String selected_car_id=available.getText().toString();

                      app.setIndex(String.valueOf(listView.indexOfChild(view)));


//                    app.set_selected_vehicle(car_id);
//
//                    app.set_car_name(total_seats);


                    Log.d("Car Index: ", String.valueOf(listView.indexOfChild(view)));


                    Log.d("Car Id: ",car_id);

                    Log.d("Selected seater: ",number);

                    Log.d("Selected Car Id: ",selected_car_id);

                    app.setSeater(number);


                    Intent intent = new Intent(VehiclesActivity.this, Seats_activity.class);
                    intent.putExtra("selected_car",car_id);
                    intent.putExtra("seater",number);
                    intent.putExtra("car_id",selected_car_id);


                    startActivity(intent);

                });


        checkAvailableVehicle();

    }

    private void checkAvailableVehicle() {

        RequestQueue busrequestQueue = Volley.newRequestQueue(getApplicationContext());

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "SearchSchedule");

        params.put("travel_from", app.getTravel_from());
        params.put("travel_to", app.getTravel_too());

        params.put("travel_date", app.getTravel_date());
        params.put("hash", app.getHash_key());


        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                response -> {
                    try {

                        if (response.getInt("response_code") == 0) {
                            jsonArray = response.getJSONArray("bus");

                            Log.d("Vehicles:",jsonArray.toString(4));

                            if(jsonArray.length()==0){
                                Toast.makeText(getApplicationContext(), ("There are no vehicles Remaining"), Toast.LENGTH_LONG).show();

                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                buses = jsonObject1.getString("route");
                                car_id = jsonObject1.getString("id");
                                total_seats = jsonObject1.getString("total_seats");
                                String seats_available = jsonObject1.getString("seats_available");
                                String departure_time = jsonObject1.getString("departure_time");
                                String from = jsonObject1.getString("from");
                                String too = jsonObject1.getString("to");

                                    Log.d("Buses: ", buses);

//                                        vehicles.add(buses);

                                    availableVehicles.add(new AvailableVehicles(buses,total_seats,seats_available,departure_time,car_id));

                            }

                        } else {
                            Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

                        }

//
                        VehicleArrayAdapter vehicleAdapter = new VehicleArrayAdapter(VehiclesActivity.this, availableVehicles);

                        listView.setAdapter(vehicleAdapter);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.d("Error: ", error.getMessage().toString());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        })

        {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }


        };
        MySingleton.getInstance(mcontext).addToRequestQueue(req);





    }
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }



}


