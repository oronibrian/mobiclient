package com.example.oronz.mobiclientapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.oronz.mobiclientapp.API.URLs;
import com.example.oronz.mobiclientapp.Adapter.ManifestRecyclerViewAdapter;
import com.example.oronz.mobiclientapp.Models.ManifestDetails;
import com.example.oronz.mobiclientapp.RecyclerClickCustom.RecyclerItemClickListener;
import com.example.oronz.mobiclientapp.Utilities.MySingleton;
import com.google.android.material.chip.Chip;
import com.mikepenz.itemanimators.ScaleUpAnimator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ManifestActivity extends AppCompatActivity {
    EditText date;
    ArrayList<ManifestDetails> mytripsDetails;
    RecyclerView mytripslistView;
    Button selectDate;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    String dbDate, today;
    ProgressDialog progressDialog;
    int textlength = 0;
    ManifestRecyclerViewAdapter tripsArrayAdapter;
    private MobiClientApplication app;
    private EditText sv;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manifest);
        app = (MobiClientApplication) getApplication();
        date = (EditText) findViewById(R.id.date);


        mytripslistView = (RecyclerView) findViewById(R.id.manifestvehiclelist);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        mytripslistView.setLayoutManager(layoutManager);

        selectDate = findViewById(R.id.btnDate);


        mytripsDetails = new ArrayList<ManifestDetails>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        today = dateFormat.format(new Date());
        date.setText("Today");
        app.setManifestDate(today);


        sv = findViewById(R.id.etsearch);
        sv.setVisibility(View.GONE);
        mContext = getApplicationContext();



        getManifest();

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(ManifestActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String formattedDay = (String.valueOf(day));
                                String formattedMonth = (String.valueOf(month));




                                if (day < 10) {
                                    formattedDay = "0" + day;
                                }

                                if (month < 10) {
                                    formattedMonth = "0" + month;
                                }


                                date.setText(day + "-" + (month + 1) + "-" + year);

                                dbDate = date.getText().toString();

                                app.setManifestDate(dbDate);

                                getManifestClick();
                            }
                        }, year, month, dayOfMonth);
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });




        mytripslistView.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, mytripslistView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever

                        TextView seater = (TextView)view.findViewById(R.id.routeTextView);

                        Toast.makeText(mContext, "clicked on " +seater.getText(), Toast.LENGTH_SHORT).show();


                        String selected;

                        selected = String.valueOf(mytripslistView.indexOfChild(view));

                        Log.d("Manifest Selected Car", selected);

                        String route = mytripsDetails.get(position).getRoute();

                        Toast.makeText(ManifestActivity.this,
                                route, Toast.LENGTH_SHORT).show();

                        app.setManfestSelected(route);

                        Intent intent = new Intent(ManifestActivity.this, SingleManifestUpdate.class);
                        startActivity(intent);
                        finish();

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );



    }

    private void getManifest() {
        progressDialog = ProgressDialog.show(this, "Loading Manifest", "Please Wait...", true);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "Schedule");
        params.put("travel_date", today);
        params.put("hash", app.getHash_key());

        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getInt("response_code") == 0) {
                                JSONArray jsonArray = response.getJSONArray("bus");
                                Log.i("Response:", jsonArray.toString());
                                progressDialog.dismiss();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String car_name = jsonObject1.getString("route");
                                    String travel_from = jsonObject1.getString("total_seats");
                                    String travel_to = jsonObject1.getString("seats_available");


                                    mytripsDetails.add(new ManifestDetails(car_name, travel_from, travel_to));

                                }


                            } else {
                                Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }

                            tripsArrayAdapter = new ManifestRecyclerViewAdapter(ManifestActivity.this, mytripsDetails);

                            mytripslistView.setAdapter(tripsArrayAdapter);
                            mytripslistView.setItemAnimator(new ScaleUpAnimator());



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }


        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(mContext).addToRequestQueue(req);

    }

    private void getManifestClick() {

        progressDialog = ProgressDialog.show(this, "Loading Manifest", "Please Wait...", true);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "Schedule");
        params.put("travel_date", dbDate);
        params.put("hash", app.getHash_key());

        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getInt("response_code") == 0) {
                                JSONArray jsonArray = response.getJSONArray("bus");
                                Log.i("Response:", jsonArray.toString());
                                progressDialog.dismiss();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String car_name = jsonObject1.getString("route");
                                    String travel_from = jsonObject1.getString("total_seats");
                                    String travel_to = jsonObject1.getString("seats_available");


                                    mytripsDetails.add(new ManifestDetails(car_name, travel_from, travel_to));

                                }


                            } else {

                                Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();


                            }


                            tripsArrayAdapter = new ManifestRecyclerViewAdapter(ManifestActivity.this, mytripsDetails);
                            mytripslistView.setItemAnimator(new ScaleUpAnimator());

                            mytripslistView.setAdapter(tripsArrayAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

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
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }


        };

        MySingleton.getInstance(mContext).addToRequestQueue(req);

    }

}
