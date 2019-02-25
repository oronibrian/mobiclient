package com.example.oronz.mobiclientapp.Fragemnts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
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
import com.example.oronz.mobiclientapp.MobiClientApplication;
import com.example.oronz.mobiclientapp.R;
import com.example.oronz.mobiclientapp.VehiclesActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import fr.ganfra.materialspinner.MaterialSpinner;


public class OneWayTripFragement extends Fragment {

    Button search1;

    ArrayList<String> city,dates,vehicles;

    Spinner too;
    Spinner from;
    MaterialSpinner travel_date;

    private MobiClientApplication app;
    String _too,_from,date;
    String buses;
    NumberPicker np;
    ProgressDialog progressBar;

    CountDownTimer CDT;
    int i =7;

    public OneWayTripFragement() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MobiClientApplication) getActivity().getApplicationContext();
        getDestination();
        getDates();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View V =inflater.inflate(R.layout.fragemnet_oneway_trip, container, false);
        city = new ArrayList<>();
        dates=new ArrayList<>();
        vehicles=new ArrayList<>();
        return V;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        search1 =view.findViewById(R.id.searchbtn1);
        search1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (too.getSelectedItemId()== 0 && too.getSelectedItemId()==-1 && travel_date.getSelectedItemId()==-1 ){

                     Toast.makeText(getContext(), "Select Correct Choices", Toast.LENGTH_SHORT).show();

                 }

                else if (_too.equals(_from)) {
                    Toast.makeText(getContext(), "Journey cannot be on the same City", Toast.LENGTH_SHORT).show();
                }
                else {

                    progressBar = new ProgressDialog(v.getContext());
                    progressBar.setCancelable(true);

                    progressBar.show();

                    Intent intent = new Intent(getActivity(), VehiclesActivity.class);


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

                            startActivity(intent);                        }
                    }.start();







                }
            }
        });



        from=view.findViewById(R.id.spinner_from_one);
        too=view.findViewById(R.id.spinner_to_one);
        travel_date=view.findViewById(R.id.spinner_travel_date_one);


        travel_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 date = travel_date.getItemAtPosition(travel_date.getSelectedItemPosition()).toString();
                Log.d("Date :%n %s", date);


                    app.setTravel_date(date);



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });


        too.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String _too = too.getItemAtPosition(too.getSelectedItemPosition()).toString();

                 _too = String.valueOf(too.getSelectedItemId());

                Log.d("To City:%n %s", _too);


                    app.setTravel_too(_too);



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String _from = from.getItemAtPosition(from.getSelectedItemPosition()).toString();
                 _from = String.valueOf(from.getSelectedItemId());
                Log.d("From City:%n %s", _from);

                    app.setTravel_from(_from);



            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

    }



    private void getDestination() {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        HashMap<String, String> params;
        params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "AvailableCities");


        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getInt("response_code") == 0) {
                                JSONArray jsonArray = response.getJSONArray("cities");


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String City = jsonObject1.getString("name");
                                    city.add(City);


                                }

                            } else {
                                Toast.makeText(getContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

                            }

                            from.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, city));
                            from.setSelection(0, false);


                            too.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, city));
                            too.setSelection(0, false);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void getDates() {
        RequestQueue datesrequestQueue = Volley.newRequestQueue(getContext());

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
                                Toast.makeText(getContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

                            }


                            travel_date.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, dates));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }            }
        })

        {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }


        };

        datesrequestQueue.add(req);
    }

}
