package com.example.oronz.mobiclientapp.Fragemnts;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class TwowayFragemnt extends Fragment {
    Button search2;
    static EditText txttrvel_retun,txttrvel_date;
    EditText no_of_pass;
    DatePickerDialog picker;

    ArrayList<String> city;

    Spinner from,too;

    private MobiClientApplication app;

    public TwowayFragemnt() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MobiClientApplication) getActivity().getApplicationContext();

        getDestination();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V =inflater.inflate(R.layout.fragment_twoway_fragemnt, container, false);
        city = new ArrayList<>();

        return V;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        search2 =view.findViewById(R.id.search2);
        search2.setOnClickListener(v -> {

        });


        txttrvel_retun=view.findViewById(R.id.txttrvel_retun);
        txttrvel_retun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new DatePickerFragment2();
                newFragment.show(getActivity().getFragmentManager(), "datePicker");
            }
        });


        txttrvel_date=view.findViewById(R.id.txttrvel_date);
        txttrvel_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new DatePickerFragment1();
                newFragment.show(getActivity().getFragmentManager(), "datePicker");
            }
        });

        no_of_pass = view.findViewById(R.id.num_of_passengers2);

        from=view.findViewById(R.id.spinner_from);
        too=view.findViewById(R.id.spinner_to);




    }


    public static class DatePickerFragment1 extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String date_selected = (day ) + "/" + month + "/"
                    + year;

            txttrvel_date.setText(date_selected);
        }
    }

    public static class DatePickerFragment2 extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String date_selected = (day ) + "/" + month + "/"
                    + year;

            txttrvel_retun.setText(date_selected);
        }
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


}
