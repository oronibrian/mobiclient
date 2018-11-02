package com.example.oronz.mobiclientapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class VehiclesActivity extends AppCompatActivity {
    public static String name, phone, id_no = "";
    ArrayList<String> vehicles, payment_methods;
    RecyclerView recyclerView;
    MyAdapter adapter;
    String buses;
    JSONArray jsonArray;
    SearchView sv;
    String car_id="";

    private MobiClientApplication app;

    ArrayList<AvailableVehicles> availableVehicles;
    ListView listView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);
        app = (MobiClientApplication) getApplication();
        vehicles = new ArrayList<>();
        payment_methods = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listview);

        availableVehicles = new ArrayList<AvailableVehicles>();




        sv = findViewById(R.id.mSearch);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                adapter.getFilter().filter(query);
                return false;
            }
        });



                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                          app.set_selected_vehicle(String.valueOf(listView.indexOfChild(view)));

                        app.set_car_name("Select Seats\n");

                        Intent intent = new Intent(VehiclesActivity.this, Seats_activity.class);
                        startActivity(intent);

                    }});

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

        params.put("clerk_username", app.get_Clerk_username());
        params.put("clerk_password", app.get_Clerk_password());

        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                response -> {
                    try {

                        if (response.getInt("response_code") == 0) {
                            jsonArray = response.getJSONArray("bus");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                buses = jsonObject1.getString("route");
                                car_id = jsonObject1.getString("id");
                                String total_seats = jsonObject1.getString("total_seats");
                                String seats_available = jsonObject1.getString("seats_available");
                                String departure_time = jsonObject1.getString("departure_time");
                                String from = jsonObject1.getString("from");
                                String too = jsonObject1.getString("to");







                                if(jsonObject1==null){
                                    Toast.makeText(getApplicationContext(), ("There are no vehicles Remaining"), Toast.LENGTH_LONG).show();

                                }else {

                                    Log.d("Buses: ", buses);

//                                        vehicles.add(buses);

                                    availableVehicles.add(new AvailableVehicles(buses,total_seats,seats_available,departure_time,car_id));

                                }


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
                Log.d("Error: ", error.getMessage());
            }
        })

        {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }


        };
        busrequestQueue.getCache().clear();
        busrequestQueue.add(req);





    }
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


}


