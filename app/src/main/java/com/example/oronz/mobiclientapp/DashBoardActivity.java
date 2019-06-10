package com.example.oronz.mobiclientapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DashBoardActivity extends AppCompatActivity {

    FloatingActionButton btn_dash_new,btn_dash_manifest;
    FloatingActionButton btn_dash_searchtckt,btn_dash_srchpayment;
    FloatingActionButton btn_dash_update,btn_dash_abtus;
    FloatingActionButton btn_dash_logout,btn_dash_submit_cash;
    TextView txtusername;
    private MobiClientApplication app;
    String today,yesterday;
    private Context mcontext;
    TextView summery,yestersummery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MobiClientApplication) getApplication();

        setContentView(R.layout.activity_dash_board);


        btn_dash_new = findViewById(R.id.btn_dash_new);
        btn_dash_manifest=findViewById(R.id.btn_dash_manifest);
        btn_dash_searchtckt=findViewById(R.id.btn_dash_searchtckt);
        btn_dash_srchpayment=findViewById(R.id.btn_dash_srchpayment);
        btn_dash_update=findViewById(R.id.btn_dash_update);
        btn_dash_abtus=findViewById(R.id.btn_dash_abtus);
        btn_dash_logout=findViewById(R.id.btn_dash_logout);
        summery=findViewById(R.id.summery);
        yestersummery=findViewById(R.id.yestersummery);
        btn_dash_submit_cash=findViewById(R.id.btn_dash_submit_cash);





        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();

        today=dateFormat.format(date);



        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        dateFormat.format(cal.getTime()); //your formatted date here

        yesterday=dateFormat.format(cal.getTime());

        ticketingReport();
        yesterdayticketingReport();


        txtusername = findViewById(R.id.txtusername);

        txtusername.setText(String.format("%s\n%s\n%s", app.getLogged_user(), app.getPhone_num(), app.getEmail_address()));

        btn_dash_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardActivity.this, MainActivity.class));
                finish();
            }
        });

        btn_dash_manifest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardActivity.this, ManifestActivity.class));
                finish();
            }
        });


        btn_dash_searchtckt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardActivity.this, SearchTicketActivity.class));
                finish();
            }
        });


        btn_dash_srchpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DashBoardActivity.this, SearchPaymentActivity.class));
                finish();

            }
        });


        btn_dash_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DashBoardActivity.this, UpdateApp.class));
                finish();

            }
        });


        btn_dash_abtus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardActivity.this, AboutUsActivity.class));
                finish();
            }
        });


        btn_dash_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        btn_dash_submit_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SubmitPaymentActivity.class));
                finish();

            }
        });

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



                                summery.setText(received);



                            }


                        } else {


//                            Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

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

                                yestersummery.setText(received);



                            }


                        } else {


//                            Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

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
