package com.example.oronz.mobiclientapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Seats_activity extends AppCompatActivity {
    public static String name, phone, id_no = "";
    ViewGroup layout;
    ArrayList<String> payment_methods;
    List<String> listofseats = new ArrayList<String>();
    List<TextView> seatViewList = new ArrayList<>();
    MobiClientApplication app;
    String payment_type;
    Intent intentExtra;

    ArrayList<String> dates, buses, ticketType;

    List<String> seats;
    Button checkbtn, btnreserve;
    TextView receipt_txt;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seats_activity);
        app = (MobiClientApplication) getApplication();
        payment_methods = new ArrayList<>();
        ticketType = new ArrayList<>();


        btnreserve = findViewById(R.id.btnreserve);
        btnreserve.setVisibility(View.GONE);
        availableSeats();
        getPaymentMethod();


        btnreserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i <= listofseats.size(); i++) {
                    reserve();

                }

            }
        });

    }


    private void getPaymentMethod() {
        RequestQueue requestQueue = Volley.newRequestQueue(Seats_activity.this);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "PaymentMethods");
        params.put("clerk_username", app.get_Clerk_username());
        params.put("clerk_password", app.get_Clerk_password());

        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getInt("response_code") == 0) {
                                JSONArray jsonArray = response.getJSONArray("payment_methods");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String methods = jsonObject1.getString("name");
                                    payment_methods.add(methods);

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

        requestQueue.add(req);

    }


    private void availableSeats() {

        RequestQueue datesrequestQueue = Volley.newRequestQueue(Seats_activity.this);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "AvailableSeats");

        params.put("from_city", app.getTravel_from());
        params.put("to_city", app.getTravel_too());
        params.put("travel_date", app.getTravel_date());
        params.put("hash", app.getHash_key());
        params.put("selected_vehicle", app.get_selected_vehicle());

        params.put("clerk_username", app.get_Clerk_username());
        params.put("clerk_password", app.get_Clerk_password());


        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                            if (response.getInt("response_code") == 0) {

                                JSONArray jsonArray = response.getJSONArray("seats");


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String bus = jsonObject1.getString("name");
                                    seats = new ArrayList<>(Arrays.asList(bus.split(",")));


                                    ticketType();

//                                    Intent I = new Intent(getApplicationContext(),Seats_layout.class);
//                                    startActivity(I);
//
                                    final GridView gridView = new GridView(Seats_activity.this);


                                    gridView.setAdapter(new ArrayAdapter<>(Seats_activity.this, R.layout.simple_list_item_1, seats));
                                    gridView.setNumColumns(3);


                                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Toast.makeText(Seats_activity.this,
                                                    getString(R.string.you_booked, seats.get(position)),
                                                    Toast.LENGTH_SHORT).show();

                                            app.setSeatNo(seats.get(position));


                                            listofseats.add(parent.getItemAtPosition(position).toString());

                                            gridView.getChildAt(position).setBackgroundColor(Color.RED);

//                                            view.setBackgroundColor(R.drawable.ic_seats_b);


                                        }


                                    });


                                    AlertDialog.Builder builder = new AlertDialog.Builder(Seats_activity.this);
                                    builder.setView(gridView);
                                    builder.setTitle("Select seat");
                                    builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
                                        public void onClick(android.content.DialogInterface dialog, int id) {
                                            btnreserve.setVisibility(View.VISIBLE);
                                            Log.d("List of seats:%n %s", String.valueOf(listofseats));
                                            payment();

                                        }
                                    })


                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    startActivity(new Intent(getApplicationContext(), VehiclesActivity.class));
                                                }
                                            });


                                    builder.show();


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
        datesrequestQueue.getCache().clear();

        datesrequestQueue.add(req);

    }

    private void payment() {


        LayoutInflater inflater = (Seats_activity.this).getLayoutInflater();


        for (int i = 0; i < listofseats.size(); i++) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(Seats_activity.this);

            final View v = inflater.inflate(R.layout.payment, null);

            final Spinner spinner_payment_type = v.findViewById(R.id.spinner_payment_method);
            spinner_payment_type.setAdapter(new ArrayAdapter<String>(Seats_activity.this, android.R.layout.simple_spinner_dropdown_item, payment_methods));
            spinner_payment_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

//                    payment_type = spinner_payment_type.getItemAtPosition(i).toString();

                    payment_type = String.valueOf(spinner_payment_type.getSelectedItemId() + 1);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // DO Nothing here
                }
            });


            builder.setView(v);


            builder.setTitle("Payment Details For Seat: " + listofseats.get(i));


            builder.setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {
                public void onClick(android.content.DialogInterface dialog, int id) {

                    final EditText passenger_name = v.findViewById(R.id.passenger_name);
                    final EditText passenger_phone = v.findViewById(R.id.passenger_phone);
                    final EditText passenger_id = v.findViewById(R.id.passenger_id);


                    name = passenger_name.getText().toString().trim();
                    phone = passenger_phone.getText().toString().trim();
                    id_no = passenger_id.getText().toString().trim();

                    app.setName(name);
                    app.setPhone(phone);
                    app.setID(id_no);
                    app.setPayment_type(payment_type);


                    if ( listofseats.size() >1) {

                        Toast.makeText(getApplicationContext(), "Details for Next Seat ", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getApplicationContext(), "Click Reserve To complete....", Toast.LENGTH_SHORT).show();


                    }


                }
            })

                    .setNegativeButton("Cancel", null);


            builder.show();


        }

//        // This clears the list
//        listofseats = new ArrayList<>();
//        Log.d("After Payment :%n %s", String.valueOf(listofseats));


    }

    private void reserve() {


        RequestQueue reserverequestQueue = Volley.newRequestQueue(Seats_activity.this);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "ReserveSeats");

        params.put("from_city", app.getTravel_from());


        params.put("to_city", app.getTravel_too());
        params.put("travel_date", app.getTravel_date());
        params.put("hash", app.getHash_key());
        params.put("selected_vehicle", app.get_selected_vehicle());

        params.put("selected_seat", app.getSeatNo());
        params.put("selected_ticket_type", "1");
        params.put("payment_method", app.getPayment_type());

        params.put("phone_number", app.getPhone());
        params.put("id_number", app.getID());

        params.put("passenger_name", app.getName());
        params.put("email_address", "brianoroni6@gmail.com");

        params.put("insurance_charge", "");
        params.put("served_by", "Test User");

        params.put("amount_charged", "10");
        params.put("reference_number", "");

        params.put("clerk_username", app.get_Clerk_username());
        params.put("clerk_password", app.get_Clerk_password());


        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getInt("response_code") == 0) {

                                JSONArray jsonArray = response.getJSONArray("ticket");

                                Log.d("Reserve:%n %s", jsonArray.toString(4));

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String reserver = jsonObject1.getString("trx_status");


                                }


                                intentExtra = new Intent(Seats_activity.this, ReceiptActivity.class);

//                                intentExtra.putExtra("data", jsonArray.toString());

                                startActivity(intentExtra);


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

    private void ticketType() {

        RequestQueue tickettyperequestQueue = Volley.newRequestQueue(Seats_activity.this);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "TicketTypes");

        params.put("from_city", app.getTravel_from());
        params.put("to_city", app.getTravel_too());
        params.put("travel_date", app.getTravel_date());
        params.put("hash", app.getHash_key());

        params.put("selected_vehicle", app.get_selected_vehicle());
        params.put("selected_seat", app.getSeatNo());
        params.put("seater", "11");

        params.put("clerk_username", app.get_Clerk_username());
        params.put("clerk_password", app.get_Clerk_password());

        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getInt("response_code") == 0) {
                                JSONArray jsonArray = response.getJSONArray("ticket_type");


                                Log.d("ticket_type:%n %s", jsonArray.toString(4));

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String ticket_type = jsonObject1.getString("name");
                                    ticketType.add(ticket_type);
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

                            }

//                            spinner_tickettype.setAdapter(new ArrayAdapter<String>(Seats_activity.this, android.R.layout.simple_spinner_dropdown_item, ticketType));

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
        tickettyperequestQueue.getCache().clear();
        tickettyperequestQueue.add(req);


    }


    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), VehiclesActivity.class));
    }
}
