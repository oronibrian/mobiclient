package com.example.oronz.mobiclientapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.oronz.mobiclientapp.API.URLs;
import com.example.oronz.mobiclientapp.Adapter.CityArrayAdapter;
import com.example.oronz.mobiclientapp.Models.City;
import com.example.oronz.mobiclientapp.Utilities.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Main2Activity extends AppCompatActivity {


    Button search1;

    ArrayList<String> dates,vehicles;

//    ArrayList<String> city;


    Spinner too;
    Spinner from;
    MaterialSpinner travel_date;
    Calendar calendar;


    private MobiClientApplication app;
    String _too,_from,date;
    String buses;
    NumberPicker np;
    ProgressDialog progressBar;

    CountDownTimer CDT;
    int i =7;

    private ArrayList<City> cities;
    CityArrayAdapter citycutomAdapter;
    private Context mContext;
    TextView selectDate;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        app = (MobiClientApplication)this.getApplicationContext();
        getDestination();
        getDates();
        mContext=this.getApplicationContext();



        dates=new ArrayList<>();
        vehicles=new ArrayList<>();
        cities = new ArrayList<>();



        from=findViewById(R.id.spinner_from_one);
        too=findViewById(R.id.spinner_to_one);
        selectDate=findViewById(R.id.btn_select_date);


        search1 =findViewById(R.id.searchbtn1);
        search1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (too.getSelectedItemId()== 0 && from.getSelectedItemId()==0 && travel_date.getSelectedItemId()==0 ){

                    Toast.makeText(mContext, "Select Correct Choices", Toast.LENGTH_SHORT).show();

                }

                else if (_too.equals(_from)) {
                    Toast.makeText(mContext, "Journey cannot be on the same City", Toast.LENGTH_SHORT).show();
                }
                else {

                    progressBar = new ProgressDialog(v.getContext());
                    progressBar.setCancelable(true);

                    progressBar.show();

                    Intent intent = new Intent(mContext, VehicleGridActivity.class);


                    CDT = new CountDownTimer(7000, 1000)
                    {
                        public void onTick(long millisUntilFinished)
                        {
                            progressBar.setMessage("Loading Available Vehicles...." + i + "");
                            i--;
                        }

                        public void onFinish()
                        {
                            progressBar.dismiss();

                            intent.putExtra("Buses", vehicles);

                            startActivity(intent);

                        }
                    }.start();









                }
            }
        });




        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();

                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(Main2Activity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {



                                selectDate.setText(day + "-" + (month + 1) + "-" + year);
                                String dbDate = selectDate.getText().toString();

                                app.setTravel_date(dbDate);
                                Log.d("Date :", dbDate);

                            }
                        }, year, month, dayOfMonth);
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);



                datePickerDialog.show();
            }
        });






        too.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l) {
//                String _too = too.getItemAtPosition(too.getSelectedItemPosition()).toString();

                String name    = ((TextView) v.findViewById(R.id.textView_name)).getText().toString();
                String id = ((TextView) v.findViewById(R.id.textView_id)).getText().toString();

                _too =id;



                Log.d("To City id:%n %s", _too);

                Log.d("To City name:%n %s", name);

                app.setTravel_too(_too);



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l) {
//                String _from = from.getItemAtPosition(from.getSelectedItemPosition()).toString();

                String name    = ((TextView) v.findViewById(R.id.textView_name)).getText().toString();
                String id = ((TextView) v.findViewById(R.id.textView_id)).getText().toString();


                _from = id;

                Log.d("From id:%n %s", _from);
                Log.d("From City:%n %s",name);


                app.setTravel_from(_from);



            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });



    }




    private void getDestination() {

        HashMap<String, String> params;
        params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "AvailableCities");


        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                response -> {
                    try {

                        if (response.getInt("response_code") == 0) {
                            JSONArray jsonArray = response.getJSONArray("cities");
                            Log.d("Cities",jsonArray.toString(4));
//                                city.add("<select>"

//                            citymodel.setName("select");


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String name = jsonObject1.getString("name");
                                String id = jsonObject1.getString("id");

                                cities.add(new City(name,id));


                            }

                        } else {
                            Toast.makeText(mContext, response.getString("response_message"), Toast.LENGTH_SHORT).show();

                        }

                        citycutomAdapter =new CityArrayAdapter(mContext, cities);

                        from.setAdapter(citycutomAdapter);

//                            from.setSelection(0, false);


                        too.setAdapter(citycutomAdapter);

//                            too.setSelection(0, false);


                    } catch (JSONException e) {
                        e.printStackTrace();
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


        MySingleton.getInstance(mContext).addToRequestQueue(req);

    }

    private void getDates() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "AvailableDates");
//        params.put("clerk_username", app.get_Clerk_username());
//        params.put("clerk_password", app.get_Clerk_password());

        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getInt("response_code") == 0) {
                                JSONArray jsonArray = response.getJSONArray("dates");


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String date = jsonObject1.getString("name");
                                    dates.add(date);
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

                            }


//                            travel_date.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, dates));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                }            }
        })

        {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }


        };

        MySingleton.getInstance(mContext).addToRequestQueue(req);
    }
}