package com.example.oronz.mobiclientapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        gridView.setLayoutManager(layoutManager);




        gridView.addOnItemTouchListener(
                new RecyclerItemClickListener(mcontext, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
//                        Toast.makeText(mcontext, "Clicked", Toast.LENGTH_SHORT).show();

                        TextView seater = (TextView)view.findViewById(R.id.grid_vehicle_seater);
                        TextView selected_car_id = (TextView)view.findViewById(R.id.selected_car_id);

                        Chip availableseats=view.findViewById(R.id.chip_available);


                        Toast.makeText(mcontext, "Car ID " +selected_car_id.getText(), Toast.LENGTH_SHORT).show();



                        Log.d("Selected seater: ",seater.getText().toString());

                        Log.d("Available seats: ",availableseats.getText().toString());

                        app.setSeater(seater.getText().toString());



                        Intent intent = new Intent(VehicleGridActivity.this, Seats_activity.class);
                        intent.putExtra("selected_car",selected_car_id.getText().toString());
                        intent.putExtra("seater",seater.getText().toString());
                        intent.putExtra("availableseats",availableseats.getText().toString());

                        intent.putExtra("car_id",selected_car_id.getText());

                        startActivity(intent);
                        finish();

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );


    }



    private void checkAvailableVehicle() {


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

                                final Dialog dialog = new Dialog(VehicleGridActivity.this);
                                dialog.setContentView(R.layout.indo_dialog);
                                dialog.setTitle("Info...");

                                // set the custom dialog components - text, image and button
                                TextView text = (TextView) dialog.findViewById(R.id.content);
                                text.setText("There are no vehicles remaining!");


                                Button dialogButton = (Button) dialog.findViewById(R.id.bt_close);
                                // if button is clicked, close the custom dialog
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        finish();
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    }
                                });

                                dialog.show();

                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                buses = jsonObject1.getString("route");
                                String  car_id = jsonObject1.getString("id");
                                total_seats = jsonObject1.getString("total_seats");
                                String seats_available = jsonObject1.getString("seats_available");


                                Log.e("Buses: ", buses);
                                Log.e("ids: ", car_id);


//                                        vehicles.add(buses);


                                availableVehiclelist.add(new RecyclerViewItem(R.drawable.bus, buses,total_seats,seats_available,car_id));

                            }

                        } else {
                            Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

                        }


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

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }





}
