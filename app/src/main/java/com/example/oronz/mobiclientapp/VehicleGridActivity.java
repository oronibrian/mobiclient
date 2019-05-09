package com.example.oronz.mobiclientapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.oronz.mobiclientapp.API.URLs;
import com.example.oronz.mobiclientapp.Adapter.GridViewAdapterVehicles;
import com.example.oronz.mobiclientapp.Adapter.MyAdapter;
import com.example.oronz.mobiclientapp.Models.RecyclerViewItem;
import com.example.oronz.mobiclientapp.RecyclerClickCustom.RecyclerItemClickListener;
import com.example.oronz.mobiclientapp.Utilities.MySingleton;
import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class VehicleGridActivity extends AppCompatActivity {

    private RecyclerView gridView;
    private GridViewAdapterVehicles gridViewAdapter;
    private ArrayList<RecyclerViewItem> corporations;
    private ArrayList<RecyclerViewItem> availableVehiclelist;

    private MobiClientApplication app;

    public static String name, phone, id_no = "";
    ArrayList<String> vehicles, payment_methods;
    RecyclerView recyclerView;
    MyAdapter adapter;
    String buses;
    JSONArray jsonArray;
    SearchView sv;
    String car_id="";
    String total_seats="";
    Context mcontext;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_grid);

        app = (MobiClientApplication) getApplication();

        gridView = (RecyclerView) findViewById(R.id.grid);
        mcontext=getApplicationContext();



        checkAvailableVehicle();

//        setDummyData();
        availableVehiclelist = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        gridView.setLayoutManager(layoutManager);




        gridView.addOnItemTouchListener(
                new RecyclerItemClickListener(mcontext, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
//                        Toast.makeText(mcontext, "Clicked", Toast.LENGTH_SHORT).show();

                        TextView seater = (TextView)view.findViewById(R.id.grid_vehicle_seater);
                        TextView selected_car_id = (TextView)view.findViewById(R.id.selected_car_id);

                        Chip availableseats=view.findViewById(R.id.chip_available);


                        Toast.makeText(mcontext, "clicked on " +seater.getText(), Toast.LENGTH_SHORT).show();


                        Log.d("Car Id: ",car_id);

                        Log.d("Selected seater: ",seater.getText().toString());

                        Log.d("Available seats: ",availableseats.getText().toString());

                        app.setSeater(seater.getText().toString());



                        Intent intent = new Intent(VehicleGridActivity.this, Seats_activity.class);
                        intent.putExtra("selected_car",selected_car_id.getText().toString());
                        intent.putExtra("seater",seater.getText().toString());
                        intent.putExtra("availableseats",availableseats.getText().toString());

                        intent.putExtra("car_id",car_id);

                        startActivity(intent);

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );


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

//                                availableVehicles.add(new AvailableVehicles(buses,total_seats,seats_available,departure_time,car_id));

                                availableVehiclelist.add(new RecyclerViewItem(R.drawable.bus, buses,total_seats,seats_available,car_id));

                            }

                        } else {
                            Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

                        }

////
//                        VehicleArrayAdapter vehicleAdapter = new VehicleArrayAdapter(VehicleGridActivity.this, availableVehicles);
//
//                        listView.setAdapter(vehicleAdapter);

                        gridViewAdapter = new GridViewAdapterVehicles(this, availableVehiclelist);
                        gridView.setAdapter(gridViewAdapter);

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




}