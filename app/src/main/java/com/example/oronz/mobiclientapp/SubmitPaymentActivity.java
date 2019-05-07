package com.example.oronz.mobiclientapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.oronz.mobiclientapp.API.URLs;
import com.example.oronz.mobiclientapp.Utilities.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SubmitPaymentActivity extends AppCompatActivity {
    TextView txtreceived_today,txtbanked_today;
    TextView txtdue_today,txtshort_today;

    TextView txtreceived_yesterday,txtbanked_yesterday;
    TextView txtdue_yesterday,txtshort_yesterday;

    String today,yesterday;
    private Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_payment);

        mcontext = getApplicationContext();

        this.setTitle("Account Status");
        Spinner spinner = findViewById(R.id.spinner);

        // Spinner click listener
        List<String> categories = new ArrayList<String>();
        categories.add("PayBill");
        categories.add("STK Push");

        txtreceived_today=findViewById(R.id.txtreceived_today);
        txtbanked_today=findViewById(R.id.txtbanked_today);
        txtdue_today=findViewById(R.id.txtdue_today);
        txtshort_today=findViewById(R.id.txtshort_today);

        txtreceived_yesterday=findViewById(R.id.txtreceived_yesterday);
        txtbanked_yesterday=findViewById(R.id.txtbanked_yesterday);
        txtdue_yesterday=findViewById(R.id.txtdue_yesterday);
        txtshort_yesterday=findViewById(R.id.txtshort_yesterday);




        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();

        today=dateFormat.format(date);



        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        dateFormat.format(cal.getTime()); //your formatted date here

        yesterday=dateFormat.format(cal.getTime());



        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        ticketingReport();
        yesterdayticketingReport();

    }


    private void ticketingReport() {


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", "emuswailit");
        params.put("api_key", "c8e254c0adbe4b2623ff85567027d78d4cc066357627e284d4b4a01b159d97a7");
        params.put("action", "TicketingReport");
        params.put("from", today);
        params.put("to", today);


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URLs.URL, new JSONObject(params),
                response -> {
                    try {
                        String code = response.getString("response_code");

                        if (code.equals("0")) {


                            Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();


                            JSONArray message = response.getJSONArray("agents");


                            for (int i = 0; i < message.length(); i++) {
                                JSONObject jsonObject1 = message.getJSONObject(i);
                                String name = jsonObject1.getString("name");

                                String tickets = jsonObject1.getString("tickets");
                                String received = jsonObject1.getString("received");
                                String banked = jsonObject1.getString("banked");

                                String due = jsonObject1.getString("due");
                                String deficit = jsonObject1.getString("deficit");

                                String reference_number = jsonObject1.getString("reference_number");



                                txtreceived_today.setText(received);
                                txtbanked_today.setText(banked);
                                txtdue_today.setText(due);
                                txtshort_today.setText(deficit);


                            }


                        } else {


                            Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

                            Log.d("Response", response.getString("response_message"));


                        }


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
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }


        };
        MySingleton.getInstance(mcontext).addToRequestQueue(req);


    }


    private void yesterdayticketingReport() {


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", "emuswailit");
        params.put("api_key", "c8e254c0adbe4b2623ff85567027d78d4cc066357627e284d4b4a01b159d97a7");
        params.put("action", "TicketingReport");
        params.put("from", yesterday);
        params.put("to", yesterday);


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URLs.URL, new JSONObject(params),
                response -> {
                    try {
                        String code = response.getString("response_code");

                        if (code.equals("0")) {


                            Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();


                            JSONArray message = response.getJSONArray("agents");


                            for (int i = 0; i < message.length(); i++) {
                                JSONObject jsonObject1 = message.getJSONObject(i);
                                String name = jsonObject1.getString("name");

                                String tickets = jsonObject1.getString("tickets");
                                String received = jsonObject1.getString("received");
                                String banked = jsonObject1.getString("banked");

                                String due = jsonObject1.getString("due");
                                String deficit = jsonObject1.getString("deficit");

                                txtreceived_yesterday.setText(received);
                                txtbanked_yesterday.setText(banked);
                                txtdue_yesterday.setText(due);
                                txtshort_yesterday.setText(deficit);


                            }


                        } else {


                            Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

                            Log.d("Response", response.getString("response_message"));


                        }


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
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }


        };
        MySingleton.getInstance(mcontext).addToRequestQueue(req);


    }



}

