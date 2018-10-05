package com.example.oronz.mobiclientapp.Fragemnts;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
import java.util.Calendar;
import java.util.HashMap;

import fr.ganfra.materialspinner.MaterialSpinner;


public class TwowayFragemnt extends Fragment {
    Button search2;
    static EditText txttrvel_retun,txttrvel_date;
    EditText no_of_pass;
    DatePickerDialog picker;
    String _too,_from,date;

    ArrayList<String> city,dates,vehicles;
    String buses,date1,date2;

    MaterialSpinner from,too,travel_date,return_date;

    private MobiClientApplication app;

    public TwowayFragemnt() {
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
        View V =inflater.inflate(R.layout.fragment_twoway_fragemnt, container, false);
        city = new ArrayList<>();
        dates=new ArrayList<>();
        vehicles=new ArrayList<>();

        return V;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        search2 =view.findViewById(R.id.search2);
        search2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_too.equals(_from)) {
                    Toast.makeText(getContext(), "Journey cannot be on the same City", Toast.LENGTH_LONG).show();
                } else {
                    checkAvailableVehicle();

                    Intent intent = new Intent(getActivity(), VehiclesActivity.class);
                    startActivity(intent);
                }
            }
        });



        no_of_pass = view.findViewById(R.id.num_of_passengers2);

        from=view.findViewById(R.id.spinner_from);
        too=view.findViewById(R.id.spinner_to);

        travel_date=view.findViewById(R.id.spinner_travel_date);
        return_date=view.findViewById(R.id.spinner_return_date);



        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String _from = from.getItemAtPosition(from.getSelectedItemPosition()).toString();
                _from = String.valueOf(from.getSelectedItemId());
                Log.d("From City:%n %s", _from);

                app.set_Twoway_Travel_from(_from);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });


        too.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String _from = from.getItemAtPosition(from.getSelectedItemPosition()).toString();
                _too = String.valueOf(too.getSelectedItemId());
                Log.d("From City:%n %s", _from);

                app.set_Twoway_Travel_too(_too);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });


        travel_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               date1 = travel_date.getItemAtPosition(travel_date.getSelectedItemPosition()).toString();
                Log.d("From Date :%n %s", date1);

                app.set_Twoway_Travel_date(date1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });


        return_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                date2 = return_date.getItemAtPosition(return_date.getSelectedItemPosition()).toString();
                Log.d("From Date :%n %s", date2);

                app.set_Twoway_return_date(date2);
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
        params.put("clerk_username", app.get_Clerk_username());
        params.put("clerk_password", app.get_Clerk_password());

        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getInt("response_code") == 0) {
                                JSONArray jsonArray = response.getJSONArray("cities");

//                                Log.d("Destinations:%n %s", jsonArray.toString(4));

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String City = jsonObject1.getString("name");
                                    city.add(City);


                                }

                            } else {
                                Toast.makeText(getContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

                            }

                            from.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item, city));

                            too.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, city));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

        requestQueue.add(req);

    }

    private void getDates() {
        RequestQueue datesrequestQueue = Volley.newRequestQueue(getContext());

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "AvailableDates");
        params.put("clerk_username", app.get_Clerk_username());
        params.put("clerk_password", app.get_Clerk_password());

        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getInt("response_code") == 0) {
                                JSONArray jsonArray = response.getJSONArray("dates");

                                Log.d("Dates:%n %s", jsonArray.toString(4));

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String date = jsonObject1.getString("name");
                                    dates.add(date);
                                }

                            } else {
                                Toast.makeText(getContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

                            }


                            travel_date.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, dates));
                            return_date.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, dates));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

        datesrequestQueue.add(req);
    }


    private void checkAvailableVehicle() {

        RequestQueue busrequestQueue = Volley.newRequestQueue(getContext());

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "AvailableBuses");

        params.put("from_city", app.get_Twoway_Travel_too());
        params.put("to_city", app.get_Twoway_Travel_too());
        params.put("travel_date", app.get_Twoway_Travel_date());
        params.put("hash", app.getHash_key());

        params.put("clerk_username", app.get_Clerk_username());
        params.put("clerk_password", app.get_Clerk_password());

        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getInt("response_code") == 0) {
                                JSONArray jsonArray = response.getJSONArray("buses");

                                for (int i = 1; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    buses = jsonObject1.getString("name");
                                    Log.d("Buses: ", buses);
                                    vehicles.add(buses);




                                }

                            } else {
                                Toast.makeText(getContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

                            }

                            app.setBuses(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, vehicles));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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



}
