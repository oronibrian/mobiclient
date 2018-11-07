package com.example.oronz.mobiclientapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.oronz.mobiclientapp.API.URLs;
import com.example.oronz.mobiclientapp.Adapter.GridItemView;
import com.example.oronz.mobiclientapp.Adapter.GridViewBaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Seats_activity extends AppCompatActivity {
    public static String name, phone, id_no = "";
    ViewGroup layout;
    ArrayList<String> payment_methods;
    List<String> listofseats = new ArrayList<String>();
    List<TextView> seatViewList = new ArrayList<>();
    MobiClientApplication app;
    String payment_type;
    Intent intentExtra;

    ArrayList<String> ticketType;
    List<String> listofRefferences;

    List<String> seats;
    Button checkbtn, btnreserve;
    TextView info_text;
    String refno,seatno,ticket_mesaage, reserver,reserve_confirmation;
    List<String> LevenSeaterList,fortynineSeaterList;

    String[] elevenSeater = new String[]{
            "1", "1X", "D",
            "2", "3", "4",
            "5", "6", "7",
            "8", "9", "10",
    };

    String[] fourteenSeater = new String[]{
            "1", "1X", "D",
            "2", "3", "4",
            "5", "6", "7",
            "8", "9", "10",
            "11", "12"
    };

    String[] fortynineSeater = new String[]{
            "1A", "2A", "1B", "2B",
            "3A", "4A", "3B", "4B",
            "5A", "6A", "5B", "6B",
            "7A", "8A", "7B", "8B",
            "9A", "10A", "9B","10B",
            "11A","12A", "11B", "12B",
            "13A", "14A", "13B", "14B",
            "15A", "16A", "15B", "16B",
            "17A", "18A", "17B", "18B",
            "19A", "20A", "19B", "20B",
            "21A", "22A", "21B", "22B",
            "23A", "24A", "23B", "24B",
            "25",


    };
    private View btnGo,btnbook,btncancel;

    private GridView gridView;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_seats);
        app = (MobiClientApplication) getApplication();
        payment_methods = new ArrayList<>();
        ticketType = new ArrayList<>();

        info_text=findViewById(R.id.info_text);

        btnreserve = findViewById(R.id.btnreserve);
        btnreserve.setVisibility(View.GONE);

        listofRefferences = new ArrayList<>();

        availableSeats();
        getPaymentMethod();

        gridView = findViewById(R.id.grid);
        btnGo = findViewById(R.id.btngo);

        btnbook = findViewById(R.id.btnbook);
        btncancel = findViewById(R.id.btncancel);

        LevenSeaterList = new ArrayList<>(Arrays.asList(elevenSeater));
        fortynineSeaterList = new ArrayList<>(Arrays.asList(fortynineSeater));


//        set listener for Button event
        btnGo.setOnClickListener(v -> payment());

        btnbook.setOnClickListener(v -> reserve());
        btncancel.setOnClickListener(v -> back());



    }


    private void   getRefferenceNumber(){

        RequestQueue requestQueue = Volley.newRequestQueue(Seats_activity.this);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("developer_username", app.getUser_name());
        params.put("developer_api_key", app.getApi_key());
        params.put("action", "generatereferencenumber");

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT,URLs.REF_URL, new JSONObject(params),
                response -> {
                    try {

                        if (response.getInt("response_code") == 0) {

                             refno = response.getString("reference_number");

                            Log.d("Ref Number: ",refno);



                        } else {
                            Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

                        }

                        listofRefferences.add(refno);

                        Log.d("Ref Number list: ", String.valueOf(listofRefferences));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
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

                })

        {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }


        };

        requestQueue.add(req);

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
                response -> {
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
                }, error -> {
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
        params.put("action", "SearchSchedule");

        params.put("travel_from", app.getTravel_from());
        params.put("travel_to", app.getTravel_too());
        params.put("travel_date", app.getTravel_date());
        params.put("hash", app.getHash_key());
        params.put("selected_vehicle", app.get_selected_vehicle());

        params.put("clerk_username", app.get_Clerk_username());
        params.put("clerk_password", app.get_Clerk_password());


        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                response -> {
                    try {


                        if (response.getInt("response_code") == 0) {

                            JSONArray jsonArray = response.getJSONArray("bus");

                                JSONObject jsonObject1 = jsonArray.getJSONObject(Integer.parseInt(app.getIndex()));
                                JSONArray bus_array = jsonObject1.getJSONArray("seats");



                                    Log.d("########### SIZE", String.valueOf(bus_array.length()));


                            for(int x=0; x < bus_array.length();x++) {
                                    JSONObject obj = bus_array.getJSONObject(x);
                                    String gari = obj.getString("name");
                                    Log.d("########### GARI",gari);

                                    seats = new ArrayList<>(Arrays.asList(gari.split(",")));

                                }

                                if(seats.size()==11){

                                    final GridViewBaseAdapter adapter = new GridViewBaseAdapter(elevenSeater, this);
                                    gridView.setAdapter(adapter);
                                    gridView.setNumColumns(3);

                                    gridView.setOnItemClickListener((parent, v, position, id) -> {
                                        int selectedIndex = adapter.selectedPositions.indexOf(position);
                                        if (selectedIndex > -1) {
                                            adapter.selectedPositions.remove(selectedIndex);
                                            ((GridItemView) v).display(false);
                                            Toast.makeText(Seats_activity.this,
                                                 "Seat "+ LevenSeaterList.get(position)+ " unselected",
                                                    Toast.LENGTH_SHORT).show();
                                            seatno = String.valueOf("");

                                            listofseats.remove(parent.getItemAtPosition(position).toString());
                                        } else if(selectedIndex ==3){

                                            Toast.makeText(Seats_activity.this,
                                                    "You Cant book drivers seat",
                                                    Toast.LENGTH_SHORT).show();

                                        }

                                        else {
                                            adapter.selectedPositions.add(position);
                                            ((GridItemView) v).display(true);

                                            Toast.makeText(Seats_activity.this,
                                                    getString(R.string.you_booked, LevenSeaterList.get(position)),
                                                    Toast.LENGTH_SHORT).show();

                                            seatno = String.valueOf(LevenSeaterList.get(position));

                                            listofseats.add(parent.getItemAtPosition(position).toString());



                                        }
                                    });


                                }  else if (seats.size()==49){

                                    final GridViewBaseAdapter adapter = new GridViewBaseAdapter(fortynineSeater, this);
                                    gridView.setAdapter(adapter);
                                    gridView.setNumColumns(4);
//
                                    gridView.setOnItemClickListener((parent, v, position, id) -> {
                                        int selectedIndex = adapter.selectedPositions.indexOf(position);
                                        if (selectedIndex > -1) {
                                            adapter.selectedPositions.remove(selectedIndex);
                                            ((GridItemView) v).display(false);
                                            Toast.makeText(Seats_activity.this,
                                                    "Seat "+ fortynineSeaterList.get(position)+ " unselected",
                                                    Toast.LENGTH_SHORT).show();
                                            seatno = String.valueOf("");

                                            listofseats.remove(parent.getItemAtPosition(position).toString());
                                        }
                                        else {
                                            adapter.selectedPositions.add(position);
                                            ((GridItemView) v).display(true);

                                            Toast.makeText(Seats_activity.this,
                                                    getString(R.string.you_booked, fortynineSeaterList.get(position)),
                                                    Toast.LENGTH_SHORT).show();


                                            seatno = String.valueOf(fortynineSeaterList.get(position));


                                            listofseats.add(parent.getItemAtPosition(position).toString());



                                        }
                                    });


                                }



                        } else {
                            Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

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
        //Generate Reff Number
        getRefferenceNumber();

        for (int i = 0; i < listofseats.size(); i++) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(Seats_activity.this);

            final View v = inflater.inflate(R.layout.payment, null);

            final Spinner spinner_payment_type = v.findViewById(R.id.spinner_payment_method);
            spinner_payment_type.setAdapter(new ArrayAdapter<String>(Seats_activity.this, android.R.layout.simple_spinner_dropdown_item, payment_methods));
            spinner_payment_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


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


//                    btnbook.setVisibility(View.VISIBLE);
                    btnGo.setVisibility(View.GONE);


                    if (listofseats.size() > 1 ) {

                        Toast.makeText(getApplicationContext(), "Details for Next Seat ", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(getApplicationContext(), "Click book To complete....", Toast.LENGTH_SHORT).show();


                    }




                }
            })

                    .setNegativeButton("Cancel", null);


            builder.show();


        }

//        // This clears the list
        listofseats = new ArrayList<>();
//        Log.d("After Payment :%n %s", String.valueOf(listofseats));


    }

    private void reserve() {


        if(String.valueOf(app.getPayment_type()).equals("3")){
//                                mpesaPayment();
            MpesaDialog();

        }

        else if(String.valueOf(app.getPayment_type()).equals("2")){
//            jamboPayWalet();
            JPWalletDialog();

        }


//        RequestQueue reserverequestQueue = Volley.newRequestQueue(Seats_activity.this);
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("username", app.getUser_name());
//        params.put("api_key", app.getApi_key());
//        params.put("action", "ReserveSeats");
//        params.put("from_city", app.getTravel_from());
//        params.put("to_city", app.getTravel_too());
//        params.put("travel_date", app.getTravel_date());
//        params.put("hash", app.getHash_key());
//        params.put("selected_vehicle", app.get_selected_vehicle());
//        params.put("seater", "11");
//        params.put("selected_seat", seatno);
//        params.put("selected_ticket_type", "13");
//        params.put("payment_method", app.getPayment_type());
//
//        params.put("phone_number", app.getPhone());
//        params.put("id_number", app.getID());
//        params.put("passenger_name", app.getName());
//        params.put("email_address", "brianoroni6@gmail.com");
//        params.put("insurance_charge", "");
//        params.put("served_by", "Oroni");
//        params.put("amount_charged", "10");
//        params.put("reference_number", refno);
//
//
//
//        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
//                response -> {
//                    try {
//
//                        if (response.getInt("response_code") == 0) {
//                            JSONArray message = response.getJSONArray("ticket_message");
//
//                            for (int i = 0; i < message.length(); i++) {
//                                JSONObject jsonObject1 = message.getJSONObject(i);
//                                 ticket_mesaage = jsonObject1.getString("name");
//
//
//                            }
//
//                            JSONArray jsonArray = response.getJSONArray("ticket");
//
//
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                 reserver = jsonObject1.getString("trx_status");
//
//                                Log.d("Reservation Status: ",reserver);
//                                Log.d("Reserve:%n %s", jsonArray.toString(4));
//
//
//                            }
//
//
//
//                            Log.d("Selected Vehicle: ",app.get_selected_vehicle());
//
//                            Log.d("Selected Seat: ",seatno);
//                            Log.d("from_city", app.getTravel_from());
//                            Log.d("to_city", app.getTravel_too());
//                            Log.d("travel_date", app.getTravel_date());
//                            Log.d("reference_number", refno);
//                            Log.d("phone_number", app.getPhone());
//
//                            Log.d("payment_method", app.getPayment_type());
//
//
//
//
//                            intentExtra = new Intent(Seats_activity.this, ReceiptActivity.class);
//
//                            for(int x=0;x<listofRefferences.size();x++) {
//
//                                intentExtra.putExtra("data", ticket_mesaage);
//                                intentExtra.putExtra("txt_status", reserver);
//
//                            }
//
//                            startActivity(intentExtra);
//
//
//                        } else {
//                            Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();
//
//                        }
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//
//                } else if (error instanceof AuthFailureError) {
//                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                } else if (error instanceof ServerError) {
//                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                } else if (error instanceof NetworkError) {
//                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                } else if (error instanceof ParseError) {
//                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        })
//
//        {
//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded; charset=utf-8";
//            }
//
//
//        };
//        reserverequestQueue.getCache().clear();
//
//        reserverequestQueue.add(req);
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

    public void MpesaDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.mpesa_layout, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }


    public void JPWalletDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.wallet_confirmation_input_dialog, null);
        dialogBuilder.setView(dialogView);

        EditText editText = (EditText) dialogView.findViewById(R.id.waletpassword);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
    private void mpesaPayment(){

        RequestQueue reserverequestQueue = Volley.newRequestQueue(Seats_activity.this);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "AuthorizePayment");
        params.put("payment_method", "3");

        params.put("reference_number", refno);
        params.put("mpesa_phone_number", app.getPhone());

        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getInt("response_code") == 0) {

                                JSONArray jsonArray = response.getJSONArray("tickets");


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                     reserve_confirmation = jsonObject1.getString("description");

                                    Log.d("Reservation Status: ",reserve_confirmation);
                                    Log.d("Reserve:%n %s", jsonArray.toString(4));



                                }


                                intentExtra = new Intent(Seats_activity.this, ReceiptActivity.class);

                                    intentExtra.putExtra("data", reserve_confirmation);

                                Bundle b=new Bundle();
                                b.putStringArrayList("Success",(ArrayList<String>)listofRefferences);

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

    private void jamboPayWalet(){


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView= inflater.inflate(R.layout.wallet_confirmation_input_dialog, null);
        dialogBuilder.setTitle("Wallet Payment Confirmation");

        dialogBuilder.setView(dialogView);

        Button button = dialogView.findViewById(R.id.btnconfirm);

        EditText password = dialogView.findViewById(R.id.waletpassword);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Commond here......"p/IK4:"


                RequestQueue reserverequestQueue = Volley.newRequestQueue(Seats_activity.this);

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", app.getUser_name());
                params.put("api_key", app.getApi_key());
                params.put("action", "AuthorizePayment");
                params.put("payment_method", "2");
                params.put("reference_number", refno);
                params.put("jambopay_wallet_username", app.getPhone());
                params.put("jambopay_wallet_password",  password.getText().toString());



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

                                            Log.d("Reservation Status: ",reserver);
                                            Log.d("Reserve:%n %s", jsonArray.toString(4));



                                        }



                                        intentExtra = new Intent(Seats_activity.this, ReceiptActivity.class);

                                        intentExtra.putExtra("data", jsonArray.toString());

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
        });

        EditText username = dialogView.findViewById(R.id.username);

        username.setText(app.getPhone());

        dialogBuilder.create().show();



    }


    private void jamboPayAgencyWalet(){
        RequestQueue reserverequestQueue = Volley.newRequestQueue(Seats_activity.this);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "AuthorizePayment");
        params.put("payment_method", "2");
        params.put("reference_number", refno);
        params.put("jambopay_agency_username", "0702357053");
        params.put("jambopay_agency_password", "p/IK4:");



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

                                    Log.d("Reservation Status: ",reserver);
                                    Log.d("Reserve:%n %s", jsonArray.toString(4));



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


private void back(){
        finish();
    startActivity(new Intent(getApplicationContext(),MainActivity.class));

}

    @Override
    public void onBackPressed() {
        finish();
        availableSeats();
        startActivity(new Intent(getApplicationContext(), VehiclesActivity.class));
    }
}
