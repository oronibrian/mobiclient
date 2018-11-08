package com.example.oronz.mobiclientapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReceiptActivity extends AppCompatActivity {
    TextView txt_name, txt_status;
    Button btnnew, btncomplete;
    MobiClientApplication app;
    List<String> myList;
    EditText walletpassword,wallet_username, mpesanumber,agency_username, Agencywaletpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        app = (MobiClientApplication) getApplication();

        txt_name = findViewById(R.id.txt_name);
        txt_status = findViewById(R.id.txt_status);
        btnnew = findViewById(R.id.btnnew);
        btncomplete = findViewById(R.id.btncomplete);


//        journey.setText(String.format("Travelling from %s To %s", app.getTravel_from(), app.getTravel_too()));
////        txt_name.setText(String.format("Name \n%s", app.getName()));
//        txt_seat_no.setText(String.format("Seat No \n%s", app.getSeatNo()));
//        date.setText(String.format("Date \n%s", app.getTravel_date()));


        String value = getIntent().getStringExtra("data");
        String status = getIntent().getStringExtra("txt_status");

        txt_name.setText(value);
        txt_status.setText(status);


        if (status.equals("Failed")) {
            btncomplete.setVisibility(View.GONE);
        } else {
            btncomplete.setVisibility(View.VISIBLE);
            btnnew.setVisibility(View.GONE);

        }

        btncomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceed();
            }
        });


        btnnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


    }

    private void proceed() {

        if (String.valueOf(app.getPayment_type()).equals("3")) {
            MpesaDialog();

        } else if (String.valueOf(app.getPayment_type()).equals("2")) {
            JPWalletDialog();

        } else if (String.valueOf(app.getPayment_type()).equals("1")) {
            JPAgencyWalletDialog();

        }

    }

    public void MpesaDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.mpesa_layout, null);
        dialogBuilder.setView(dialogView);

        mpesanumber = dialogView.findViewById(R.id.mpesanumber);
        Button btncomplete = dialogView.findViewById(R.id.mpsabtn);

        btncomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpesaPayment();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }


    public void JPWalletDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.wallet_confirmation_input_dialog, null);
        dialogBuilder.setView(dialogView);

        wallet_username = dialogView.findViewById(R.id.wallet_username);

        wallet_username.setText(app.getUser_name());

        walletpassword = dialogView.findViewById(R.id.waletpassword);
        Button btncomplete = dialogView.findViewById(R.id.btnconfirm);

        btncomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jamboPayWalet();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }


    public void JPAgencyWalletDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.agency_wallet, null);
        dialogBuilder.setView(dialogView);
        agency_username = dialogView.findViewById(R.id.agency_username);

        agency_username.setText(app.getUser_name());

        Agencywaletpassword = dialogView.findViewById(R.id.Agencywaletpassword);
        Button btncomplete = dialogView.findViewById(R.id.btnconfirm);

        btncomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jamboPayAgencyWalet();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void mpesaPayment() {

        RequestQueue reserverequestQueue = Volley.newRequestQueue(ReceiptActivity.this);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "AuthorizePayment");
        params.put("payment_method", "3");

        params.put("reference_number", app.getRefno());
        params.put("mpesa_phone_number", mpesanumber.getText().toString());

        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getInt("response_code") == 0) {

                                JSONArray jsonArray = response.getJSONArray("tickets");


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String reserve_confirmation = jsonObject1.getString("description");

                                    Log.d("Reservation Status: ", reserve_confirmation);
                                    Log.d("Reserve:%n %s", jsonArray.toString(4));


                                }


                            } else {
                                Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        reserverequestQueue.getCache().clear();

        reserverequestQueue.add(req);

    }

    private void jamboPayWalet() {


        //Commond here......"p/IK4:"


        RequestQueue reserverequestQueue = Volley.newRequestQueue(ReceiptActivity.this);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "AuthorizePayment");
        params.put("payment_method", "2");
        params.put("reference_number", app.getRefno());
        params.put("jambopay_wallet_username", app.getPhone());
        params.put("jambopay_wallet_password", walletpassword.getText().toString());


        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getInt("response_code") == 0) {

                                JSONArray jsonArray = response.getJSONArray("ticket");


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String reserver = jsonObject1.getString("trx_status");

                                    Log.d("Reservation Status: ", reserver);
                                    Log.d("Reserve:%n %s", jsonArray.toString(4));


                                }


                            } else {
                                Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        reserverequestQueue.getCache().clear();

        reserverequestQueue.add(req);


    }


    private void jamboPayAgencyWalet() {
        RequestQueue reserverequestQueue = Volley.newRequestQueue(ReceiptActivity.this);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "AuthorizePayment");
        params.put("payment_method", "2");
        params.put("reference_number", app.getRefno());
        params.put("jambopay_agency_username", app.getPhone());
        params.put("jambopay_agency_password", Agencywaletpassword.toString());


        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getInt("response_code") == 0) {

                                JSONArray jsonArray = response.getJSONArray("ticket");


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String reserver = jsonObject1.getString("trx_status");

                                    Log.d("Reservation Status: ", reserver);
                                    Log.d("Reserve:%n %s", jsonArray.toString(4));


                                }

                            } else {
                                Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        reserverequestQueue.getCache().clear();

        reserverequestQueue.add(req);

    }

}
