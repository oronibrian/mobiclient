package com.example.oronz.mobiclientapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.oronz.mobiclientapp.API.URLs;
import com.example.oronz.mobiclientapp.Models.UserDetails;
import com.example.oronz.mobiclientapp.Tickettem.Ticket_Item;
import com.example.oronz.mobiclientapp.Utilities.MySingleton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import fr.ganfra.materialspinner.MaterialSpinner;
import spencerstudios.com.fab_toast.FabToast;

// import org.json.simple.*;

public class Seats_activity extends AppCompatActivity {
    public static String name, phone, id_no, Seat;
    ViewGroup layout;
    ArrayList<String> payment_methods, travel_class_array_list;
    List<String> listofseats = new ArrayList<String>();
    List<TextView> seatViewList = new ArrayList<>();
    MobiClientApplication app;
    Intent intentExtra;

    ArrayList<String> ticketType;

    List<String> seats;
    Button checkbtn;
    TextView info_text;
    String refno, seatno, ticket_mesaage, reserver, reserve_confirmation;
    List<String> LevenSeaterList, fortynineSeaterList, fourteenSeaterlist, sixteeneSeaterList;

    MaterialSpinner payment_type_spinner;

    Spinner travel_class_spinner;
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
            "11", "12", "13"
    };
    String[] sixteenSeater = new String[]{
            "1", "c", "1X", "D",
            "2", "c", "3", "4",
            "5", "c", "6", "7",
            "8", "c", "9", "10",
            "11", "12", "13", "14"

    };
    String[] fortynineSeater = new String[]{

            "1A", "2A", "C", "1B", "2B",
            "3A", "4A", "C", "3B", "4B",
            "5A", "6A", "C", "5B", "6B",
            "7A", "8A", "C", "7B", "8B",
            "9A", "9B", "C", "10A", "10B",
            "11A", "11B", "C", "12A", "12B",
            "13A", "14A", "C", "13B", "14B",
            "15A", "16A", "C", "15B", "16B",
            "17A", "18A", "C", "17B", "18B",
            "19A", "20A", "C", "19B", "20B",
            "21A", "22A", "C", "21B", "22B",
            "23A", "24A", "25", "23B", "24B",

    };
    TextView textView;
    ArrayList<UserDetails> ticketusers;
    UserDetails userDetails;
    Bundle b,bundlebatch;
    String selected_Car, seater;
    private Context mcontext;
    private View btnGo, btnbook, btncancel, textview;
    private GridView gridView;
    private ProgressDialog mProgress;
    private TextView gridtextView;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_seats);
        app = (MobiClientApplication) getApplication();
        payment_methods = new ArrayList<>();
        ticketType = new ArrayList<>();
        mcontext = getApplicationContext();

        travel_class_array_list = new ArrayList<>();


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Select Your Seat or Seats");
        ticketusers = new ArrayList<UserDetails>();


        Intent startingInted = getIntent();
        selected_Car = startingInted.getStringExtra("car_id");
        seater = startingInted.getStringExtra("seater");


        Log.d("Selected car id: ", selected_Car);
//        Log.d("##### Seats", seats.toString());
        Log.d("##### Seater:", seater);


        availableSeats();
        getPaymentMethod();
        getTicketType();

        gridView = findViewById(R.id.grid);
        btnGo = findViewById(R.id.btngo);

        textview = findViewById(R.id.txt_grid);

        btnbook = findViewById(R.id.btnbook);
        btncancel = findViewById(R.id.btncancel);

        LevenSeaterList = new ArrayList<>(Arrays.asList(elevenSeater));
        fortynineSeaterList = new ArrayList<>(Arrays.asList(fortynineSeater));


        fourteenSeaterlist = new ArrayList<>(Arrays.asList(fourteenSeater));
        sixteeneSeaterList = new ArrayList<>(Arrays.asList(sixteenSeater));

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Reserving ..");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

//        seats = new ArrayList<>();


        payment_type_spinner = findViewById(R.id.payment_type_spinner);
        travel_class_spinner = findViewById(R.id.travel_class);


        travel_class_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String slected_class = String.valueOf(travel_class_spinner.getSelectedItemPosition());

                if (position == -1) {
                    Toast.makeText(mcontext, "Select Travel Class", Toast.LENGTH_SHORT).show();
                }

                app.setPrice_class("10");


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        travel_class_spinner.setVisibility(View.GONE);


        payment_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String Selected_payment_type = String.valueOf(payment_type_spinner.getSelectedItemPosition());
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);


                if (i == -1) {
//                    Toast.makeText(mcontext, "Select Payment Methods", Toast.LENGTH_SHORT).show();

                    FabToast.makeText(mcontext, "Select Payment Method", FabToast.LENGTH_SHORT, FabToast.WARNING, FabToast.POSITION_CENTER).show();

                    travel_class_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            String slected_class = String.valueOf(travel_class_spinner.getSelectedItemPosition());

                            if (position == -1) {
                                Toast.makeText(mcontext, "Select Travel Class", Toast.LENGTH_SHORT).show();
                            }

                            app.setPrice_class("10");


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    btnGo.setVisibility(View.GONE);
                } else {
                    btnGo.setVisibility(View.VISIBLE);


                }

                app.setPayment_type(Selected_payment_type);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here

            }
        });


        btnbook.setVisibility(View.GONE);


//        set listener for Button event
        btnGo.setOnClickListener(v -> {
            payment();
            payment_type_spinner.setVisibility(View.GONE);
            btnbook.setVisibility(View.VISIBLE);

        });

        btnbook.setOnClickListener((View v) -> {


                    if (ticketusers.size() == 1) {

                        for (int x = 0; x < ticketusers.size(); x++) {
                            userDetails = ticketusers.get(x);
                            name = userDetails.getName();
                            phone = userDetails.getPhone();
                            id_no = userDetails.getIs();
                            Seat = userDetails.getSeat();

                            reserve();
                        }


                    } else {
                        batch_reserve();

                    }


                }
        );

        btncancel.setOnClickListener(v -> back());


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }



    private void getRefferenceNumber() {

        RequestQueue requestQueue = Volley.newRequestQueue(Seats_activity.this);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("developer_username", app.getUser_name());
        params.put("developer_api_key", app.getApi_key());
        params.put("action", "generatereferencenumber");

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, URLs.REF_URL, new JSONObject(params),
                response -> {
                    try {

                        if (response.getInt("response_code") == 0) {

                            refno = response.getString("reference_number");
                            app.setRefno(refno);

                            Log.d("Ref Number: ", refno);


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

        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }


        };

        MySingleton.getInstance(mcontext).addToRequestQueue(req);

    }


    private void getTicketType() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "TicketTypes");
        params.put("from_city", app.getTravel_from());
        params.put("to_city", app.getTravel_too());
        params.put("travel_date", app.getTravel_date());
        params.put("hash", app.getHash_key());
        params.put("selected_vehicle", "13");
        params.put("selected_seat", "2");
        params.put("seater", selected_Car);


        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                response -> {
                    try {

                        if (response.getInt("response_code") == 0) {
                            JSONArray jsonArray = response.getJSONArray("ticket_type");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String class_price = jsonObject1.getString("fare_per_ticket");
                                String name = jsonObject1.getString("name");

                                travel_class_array_list.add(name);
                                Log.d("Ticket_class", name);

                                Log.d("price", class_price);

                            }


                        } else {
                            Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

                        }

                        travel_class_spinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, travel_class_array_list));


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

        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }


        };

        MySingleton.getInstance(mcontext).addToRequestQueue(req);


    }

    private void getPaymentMethod() {
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

                        payment_type_spinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, payment_methods));


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

        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }


        };

        MySingleton.getInstance(mcontext).addToRequestQueue(req);

    }


    private void availableSeats() {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "AvailableSeats");
        params.put("from_city", app.getTravel_from());
        params.put("to_city", app.getTravel_too());
        params.put("travel_date", app.getTravel_date());
        params.put("hash", app.getHash_key());
        params.put("selected_vehicle", selected_Car);


        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                response -> {
                    try {


                        if (response.getInt("response_code") == 0) {

                            JSONArray bus_array = response.getJSONArray("seats");


                            Log.d("#####SIZE", String.valueOf(bus_array.length()));


                            for (int x = 0; x < bus_array.length(); x++) {
                                JSONObject obj = bus_array.getJSONObject(x);
                                String gari = obj.getString("name");
//                                vehicleseater = obj.getString("seater");

                                gari = gari.replace(" ", "");


                                seats = new ArrayList<>(Arrays.asList(gari.split(",")));

                                Log.d("Seats", seats.toString());


                            }


                            if (Integer.parseInt(seater) <= 11) {


                                final ElevenSeaterAdapter adapter = new ElevenSeaterAdapter(elevenSeater, this);

                                gridView.setAdapter(adapter);
                                gridView.setNumColumns(3);


                                gridView.setOnItemClickListener((AdapterView<?> parent, View v, int position, long id) -> {

                                    View viewItem = gridView.getChildAt(position);

                                    int selectedIndex = adapter.selectedPositions.indexOf(position);

                                    if (selectedIndex > -1) {
                                        adapter.selectedPositions.remove(selectedIndex);

                                        Toast.makeText(Seats_activity.this,
                                                "Seat " + LevenSeaterList.get(position) + " unselected",
                                                Toast.LENGTH_SHORT).show();
                                        seatno = "";

                                        listofseats.remove(parent.getItemAtPosition(position).toString());
                                        TextView text = (TextView) v.findViewById(R.id.txt_grid);
                                        text.setBackgroundResource(R.drawable.seat_normal);

                                    } else {
                                        adapter.selectedPositions.add(position);
//                                        ((GridItemView) v).display(true);

                                        Toast.makeText(Seats_activity.this,
                                                getString(R.string.you_booked, LevenSeaterList.get(position)),
                                                Toast.LENGTH_SHORT).show();

                                        seatno = String.valueOf(LevenSeaterList.get(position));

                                        listofseats.add(parent.getItemAtPosition(position).toString());
                                        TextView text = (TextView) v.findViewById(R.id.txt_grid);
                                        text.setBackgroundResource(R.drawable.seat_normal_booked);


                                    }


                                });
                            } else if (Integer.parseInt(seater) > 14 && Integer.parseInt(seater) <= 16) {


                                final SixteenCustomAdapter adapter16 = new SixteenCustomAdapter(sixteenSeater, this);
                                gridView.setAdapter(adapter16);
                                gridView.setNumColumns(4);

//
                                gridView.setOnItemClickListener((parent, v, position, id) -> {

                                    View viewItem = gridView.getChildAt(position);

                                    int selectedIndex = adapter16.selectedPositions.indexOf(position);


                                    if (selectedIndex > -1) {
                                        adapter16.selectedPositions.remove(selectedIndex);
//                                        ((GridItemView) v).display(false);
                                        Toast.makeText(Seats_activity.this,
                                                "Seat " + sixteeneSeaterList.get(position) + " unselected",
                                                Toast.LENGTH_SHORT).show();
                                        seatno = "";

                                        listofseats.remove(parent.getItemAtPosition(position).toString());
                                        TextView text = (TextView) v.findViewById(R.id.txt_grid);
                                        text.setBackgroundResource(R.drawable.seat_normal);


                                    } else {
                                        adapter16.selectedPositions.add(position);

                                        Toast.makeText(Seats_activity.this,
                                                getString(R.string.you_booked, sixteeneSeaterList.get(position)),
                                                Toast.LENGTH_SHORT).show();


                                        seatno = String.valueOf(sixteeneSeaterList.get(position));

                                        listofseats.add(parent.getItemAtPosition(position).toString());
                                        TextView text = (TextView) v.findViewById(R.id.txt_grid);
                                        text.setBackgroundResource(R.drawable.seat_normal_booked);

                                    }


                                });
                            } else if (Integer.parseInt(seater) > 16 && Integer.parseInt(seater) <= 49) {

                                final FoutynineCustomAdapter adapter = new FoutynineCustomAdapter(fortynineSeater, this);
                                gridView.setAdapter(adapter);
                                gridView.setNumColumns(5);

//
                                gridView.setOnItemClickListener((parent, v, position, id) -> {

                                    View viewItem = gridView.getChildAt(position);

                                    int selectedIndex = adapter.selectedPositions.indexOf(position);


                                    TextView text = (TextView) v.findViewById(R.id.txt_grid);


                                    if (selectedIndex > -1) {
                                        adapter.selectedPositions.remove(selectedIndex);
//                                        ((GridItemView) v).display(false);
                                        Toast.makeText(Seats_activity.this,
                                                "Seat " + fortynineSeaterList.get(position) + " unselected",
                                                Toast.LENGTH_SHORT).show();
                                        seatno = "";

                                        text.setBackgroundResource(R.drawable.seat_normal);

                                        listofseats.remove(parent.getItemAtPosition(position).toString());


                                    } else {
                                        adapter.selectedPositions.add(position);


                                        Toast.makeText(Seats_activity.this,
                                                getString(R.string.you_booked, fortynineSeaterList.get(position)),
                                                Toast.LENGTH_SHORT).show();


                                        seatno = String.valueOf(fortynineSeaterList.get(position));

//                                        listofseats.add(parent.getItemAtPosition(position).toString());
                                        listofseats.add(fortynineSeaterList.get(position));

                                        if (text.getText().equals(seatno)) {
                                            text.setBackgroundResource(R.drawable.seat_normal_booked);

                                        }

//                                        TextView text = (TextView) v.findViewById(R.id.txt_grid);


                                    }


                                });


                            } else if (Integer.parseInt(seater) > 11 && Integer.parseInt(seater) <= 14) {
//
                                final FouteenSeaterAdapter adapter = new FouteenSeaterAdapter(fourteenSeater, this);
                                gridView.setAdapter(adapter);
                                gridView.setNumColumns(3);

//
                                gridView.setOnItemClickListener((parent, v, position, id) -> {

                                    View viewItem = gridView.getChildAt(position);

                                    int selectedIndex = adapter.selectedPositions.indexOf(position);


                                    if (selectedIndex > -1) {
                                        adapter.selectedPositions.remove(selectedIndex);
//                                        ((GridItemView) v).display(false);
                                        Toast.makeText(Seats_activity.this,
                                                "Seat " + fourteenSeaterlist.get(position) + " unselected",
                                                Toast.LENGTH_SHORT).show();
                                        seatno = "";

                                        listofseats.remove(parent.getItemAtPosition(position).toString());
                                        TextView text = (TextView) v.findViewById(R.id.txt_grid);
                                        text.setBackgroundResource(R.drawable.seat_normal);


                                    } else {
                                        adapter.selectedPositions.add(position);

                                        Toast.makeText(Seats_activity.this,
                                                getString(R.string.you_booked, fourteenSeaterlist.get(position)),
                                                Toast.LENGTH_SHORT).show();

                                        seatno = String.valueOf(fourteenSeaterlist.get(position));

                                        listofseats.add(parent.getItemAtPosition(position).toString());
                                        TextView text = (TextView) v.findViewById(R.id.txt_grid);

                                        text.setBackgroundResource(R.drawable.seat_normal_booked);
                                        listofseats.add(parent.getItemAtPosition(position).toString());


                                    }

                                    adapter.notifyDataSetChanged();


                                });


//
//
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
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }


        };
        MySingleton.getInstance(mcontext).addToRequestQueue(req);


    }

    private void payment() {


        LayoutInflater inflater = (Seats_activity.this).getLayoutInflater();
        //Generate Reff Number

        getRefferenceNumber();

        Log.d("List Of Seats :%n %s", String.valueOf(listofseats));

        for (int i = 0; i < listofseats.size(); i++) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(Seats_activity.this);

            final View v = inflater.inflate(R.layout.payment, null);

            final String seat_no = listofseats.get(i);
            Log.d("seat_no:%n %s", String.valueOf(seat_no));


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


                    ticketusers.add(new UserDetails(name, phone, id_no, seat_no));


//                    btnbook.setVisibility(View.VISIBLE);
                    btnGo.setVisibility(View.GONE);


                    for (int i = 0; i < listofseats.size(); i++) {

                        Toast.makeText(getApplicationContext(), "Details  Seat ", Toast.LENGTH_SHORT).show();

                    }


                }
            })

                    .setNegativeButton("Cancel", null);


            builder.show();


        }

//        // This clears the list
//        listofseats = new ArrayList<>();
        Log.d("Ticket Details :%n %s", String.valueOf(ticketusers));


    }


    private void reserve() {

        mProgress.show();
        RequestQueue reserverequestQueue = Volley.newRequestQueue(Seats_activity.this);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "ReserveSeats");
        params.put("from_city", app.getTravel_from());
        params.put("to_city", app.getTravel_too());
        params.put("travel_date", app.getTravel_date());
        params.put("hash", app.getHash_key());
        params.put("selected_vehicle", selected_Car);
        params.put("seater", seater);
        params.put("selected_ticket_type", "13");
        params.put("payment_method", app.getPayment_type());
        params.put("selected_seat", Seat);
        params.put("phone_number", phone);
        params.put("id_number", id_no);
        params.put("passenger_name", name);
        params.put("email_address", "brianoroni6@gmail.com");
        params.put("insurance_charge", "");
        params.put("served_by", app.getLogged_user());
        params.put("amount_charged", app.getPrice_class());
        params.put("reference_number", refno);


        Log.e("Params", params.toString());



        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                response -> {
                    try {

                        Log.d("Response: ", response.toString(4));


                        if (response.getInt("response_code") == 0) {
                            JSONArray message = response.getJSONArray("ticket_message");
                            mProgress.dismiss();


                            for (int i = 0; i < message.length(); i++) {
                                JSONObject jsonObject1 = message.getJSONObject(i);
                                ticket_mesaage = jsonObject1.getString("name");


                            }

                            JSONArray jsonArray = response.getJSONArray("ticket");


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                reserver = jsonObject1.getString("trx_status");


                            }

                            b = new Bundle();
                            b.putString("TicketArray", jsonArray.toString());


                            Log.d("Status", reserver);
                            Log.d("Mesaage", ticket_mesaage);

                            app.setServerRespose(reserver);
                            app.setServerMessage(ticket_mesaage);


                        } else {
                            Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("Exception", e.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.dismiss();

                Log.e("Volley Error:", error.toString());


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
        Log.e("Request body: ", params.toString());


        req.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
        reserverequestQueue.add(req);

        reserverequestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                reserverequestQueue.getCache().clear();
                mProgress.dismiss();

                Gson gson = new Gson();

                String jsonTicketusers = gson.toJson(ticketusers);

                intentExtra = new Intent(Seats_activity.this, ReceiptActivity.class);
                intentExtra.putStringArrayListExtra("listofseats", (ArrayList<String>) listofseats);

                intentExtra.putExtra("data", ticket_mesaage);
                intentExtra.putExtra("txt_status", reserver);
                intentExtra.putExtras(b);
                intentExtra.putExtra("list_as_string", jsonTicketusers);

                startActivity(intentExtra);

            }
        });


    }

    private void batch_reserve() {

        mProgress.show();

        JSONObject obj;
        JSONArray ticket_items = new JSONArray();

        for (int i=0; i < ticketusers.size(); i++) {
            obj = new JSONObject();
                        userDetails = ticketusers.get(i);

            try {
                obj.put("passenger_name", userDetails.getName());
                obj.put("phone_number", userDetails.getPhone());
                obj.put("id_number", userDetails.getIs());
                obj.put("from_city", app.getTravel_from());
                obj.put("to_city", app.getTravel_too());
                obj.put("travel_date", app.getTravel_date());
                obj.put("selected_vehicle", selected_Car);
                obj.put("seater", seater);
                obj.put("selected_seat", userDetails.getSeat());
                obj.put("selected_ticket_type", "13");
                obj.put("payment_method", app.getPayment_type());
                obj.put("email_address", "brianoroni6@gmail.com");
                obj.put("insurance_charge", "");
                obj.put("served_by", app.getLogged_user());
                obj.put("amount_charged", app.getPrice_class());
                obj.put("reference_number", refno);
                ticket_items.put(obj);
            }catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        Log.e("Body", ticket_items.toString());


        RequestQueue batchreserve = Volley.newRequestQueue(Seats_activity.this);


        JSONObject postparams = new JSONObject();

        try {
            postparams.put("username", app.getUser_name());
            postparams.put("api_key",  app.getApi_key());
            postparams.put("action", "BatchReserveSeats");
            postparams.put("hash", "1FBEAD9B-D9CD-400D-ADF3-F4D0E639CEE0");
            postparams.put("ticket_items", ticket_items);

        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, postparams,
                response -> {
                    try {

                        Log.d("Response: ", response.toString(4));


                        if (response.getInt("response_code") == 0) {
                            JSONArray message = response.getJSONArray("ticket_message");
                            mProgress.dismiss();


                            for (int i = 0; i < message.length(); i++) {
                                JSONObject jsonObject1 = message.getJSONObject(i);
                                ticket_mesaage = jsonObject1.getString("name");


                            }

                            JSONArray jsonArray = response.getJSONArray("ticket");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                reserver = jsonObject1.getString("name");


                            }

                            bundlebatch = new Bundle();

                            bundlebatch.putString("TicketArray", jsonArray.toString());


                        } else {
                            Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("Exception", e.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.dismiss();

                Log.e("Volley Error:", error.toString());


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
                return "application/json; charset=utf-8";
            }

        };
        Log.d("Request body: ", postparams.toString());


        req.setRetryPolicy(new DefaultRetryPolicy(0, -1, 0));
        batchreserve.add(req);

        batchreserve.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                batchreserve.getCache().clear();
                mProgress.dismiss();

                Gson gson = new Gson();

                String jsonTicketusers = gson.toJson(ticketusers);

                intentExtra = new Intent(Seats_activity.this, ReceiptActivity.class);
                intentExtra.putStringArrayListExtra("listofseats", (ArrayList<String>) listofseats);

                intentExtra.putExtra("data", ticket_mesaage);
                intentExtra.putExtra("txt_status", reserver);
//                intentExtra.putExtras(bundlebatch);
                intentExtra.putExtra("list_as_string", jsonTicketusers);

                startActivity(intentExtra);

            }
        });




    }



    private void back() {
        finish();
        startActivity(new Intent(getApplicationContext(), VehiclesActivity.class));

    }

    @Override
    public void onBackPressed() {
        finish();
        availableSeats();
        startActivity(new Intent(getApplicationContext(), VehiclesActivity.class));
    }

    static class ViewHolder {

        TextView gridtextView;

        public ViewHolder(TextView textView) {
            this.gridtextView = textView;
        }

        public TextView getGridtextView() {
            return gridtextView;
        }

        public void setGridtextView(TextView gridtextView) {
            this.gridtextView = gridtextView;
        }
    }

    public class ElevenSeaterAdapter extends BaseAdapter {
        public List<Object> selectedPositions;
        List<String> Booked_11 = new ArrayList<>(LevenSeaterList);
        private Context context;
        private String[] strings;

        public ElevenSeaterAdapter(String[] strings, Context context) {
            this.strings = strings;
            this.context = context;
            selectedPositions = new ArrayList<>();

        }


        @Override
        public int getCount() {
            return strings.length;

        }

        @Override
        public Object getItem(int position) {
            return strings[position];
        }

        @Override
        public long getItemId(int position) {
            return position;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            View gridView;


            HashSet<String> common = new HashSet<>(LevenSeaterList);
            common.retainAll(seats);

            Log.d("similiar ", common.toString());
            Log.d("seats available ", seats.toString());
            Log.d("Leven Seater List ", LevenSeaterList.toString());

            Booked_11.removeAll(common);
            Log.d("Booked  Seats ", Booked_11.toString());


            if (convertView == null) {

                gridView = new View(context);

                gridView = inflater.inflate(R.layout.grid_item, null);

                // set value into textview
                gridtextView = gridView
                        .findViewById(R.id.txt_grid);
                gridtextView.setText(strings[position]);

                String seatsitem = strings[position];

                gridtextView.setBackgroundResource(R.drawable.seat_normal_booked);

                if (seatsitem.equals("D")) {
                    gridtextView.setBackgroundResource(R.drawable.steering);
                    gridtextView.setText("");

                }

                for (int i = 0; i < common.size(); i++) {

                    if (seats.size() <= 11) {

                        if (seatsitem.equals(seats.get(i))) {
                            gridtextView.setBackgroundResource(R.drawable.seat_normal);

                        }


                    }

                }


            } else {
                gridView = convertView;
            }


            return gridView;

        }

        @Override
        public boolean areAllItemsEnabled() {

            return true;

        }

        @Override
        public boolean isEnabled(int position) {

            String seats = strings[position];

            for (int i = 0; i < Booked_11.size(); i++) {
                if ((seats.equals(Booked_11.get(i)))) {

                    return false;

                }

            }


            return true;
        }


    }

    public class FouteenSeaterAdapter extends BaseAdapter {
        public List selectedPositions;
        List<String> Booked_14 = new ArrayList<>(fourteenSeaterlist);
        private Context context;
        private String[] strings;

        public FouteenSeaterAdapter(String[] strings, Context context) {
            this.strings = strings;
            this.context = context;
            selectedPositions = new ArrayList<>();

        }


        @Override
        public int getCount() {
            return strings.length;

        }

        @Override
        public Object getItem(int position) {
            return strings[position];
        }

        @Override
        public long getItemId(int position) {
            return position;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            View gridView;


            HashSet<String> fourteencommon = new HashSet<>(fourteenSeaterlist);
            fourteencommon.retainAll(seats);
            System.out.println("14 seater similiar " + fourteencommon);
            System.out.println("seats available " + seats);
            System.out.println("14 Seater List " + fourteenSeaterlist);


            Booked_14.removeAll(fourteencommon);
            System.out.println("14 seater Booked  Seats " + Booked_14);


            if (convertView == null) {

                gridView = new View(context);

                gridView = inflater.inflate(R.layout.grid_item, null);

                // set value into textview
                gridtextView = gridView
                        .findViewById(R.id.txt_grid);
                gridtextView.setText(strings[position]);

                String seatsitem = strings[position];

                gridtextView.setBackgroundResource(R.drawable.seat_normal_booked);

                if (seatsitem.equals("D")) {
                    gridtextView.setBackgroundResource(R.drawable.steering);
                    gridtextView.setText("");

                }


                for (int i = 0; i < fourteencommon.size(); i++) {

                    if (seatsitem.equals("D")) {

                        gridtextView.setBackgroundResource(R.drawable.steering);
                        gridtextView.setText("");

                    } else if (seats.size() > 11 && seats.size() <= 14) {

                        if (seatsitem.equals(seats.get(i))) {
                            gridtextView.setBackgroundResource(R.drawable.seat_normal);

                        }

                    }
                }


            } else {
                gridView = convertView;
            }


            return gridView;

        }

        @Override
        public boolean areAllItemsEnabled() {

            return true;

        }

        @Override
        public boolean isEnabled(int position) {

            String seats = strings[position];


            for (int i = 0; i < Booked_14.size(); i++) {
                if ((seats.equals(Booked_14.get(i)))) {

                    return false;

                }

            }

            return true;
        }


    }

    public class SixteenCustomAdapter extends BaseAdapter {
        public List selectedPositions;
        List<String> Booked = new ArrayList<>(sixteeneSeaterList);
        private Context context;
        private String[] strings;

        public SixteenCustomAdapter(String[] strings, Context context) {
            this.strings = strings;
            this.context = context;
            selectedPositions = new ArrayList<>();

        }


        @Override
        public int getCount() {
            return strings.length;

        }

        @Override
        public Object getItem(int position) {
            return strings[position];
        }

        @Override
        public long getItemId(int position) {
            return position;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            View gridView;


            HashSet<String> sixteencommon = new HashSet<>(sixteeneSeaterList);
            sixteencommon.retainAll(seats);
            System.out.println("16 seater similiar " + sixteencommon);
            System.out.println("seats available " + seats);
            System.out.println("16 Seater List " + sixteeneSeaterList);
            int size = seats.size();
            System.out.println("16 Size " + size);


            Booked.removeAll(sixteencommon);


            if (convertView == null) {


                gridView = inflater.inflate(R.layout.grid_item, null);


            } else {
                gridView = convertView;
            }


            // set value into textview
            gridtextView = gridView
                    .findViewById(R.id.txt_grid);
            gridtextView.setText(strings[position]);

            String seatsitem = strings[position];

            gridtextView.setBackgroundResource(R.drawable.seat_normal);

            if (seatsitem.equals("D")) {
                gridtextView.setBackgroundResource(R.drawable.steering);
                gridtextView.setText("");

            }


            int color = 0x00FFFFFF; // Transparent

            if (seatsitem.equals("c")) {
                gridtextView.setBackgroundColor(color);
                gridtextView.setText("");

            }

            for (int i = 0; i < sixteencommon.size(); i++) {
                if (seats.size() > 14 && seats.size() <= 16) {

                    if (seatsitem.equals(seats.get(i))) {
                        gridtextView.setBackgroundResource(R.drawable.seat_normal);

                    }

                }
            }

            return gridView;

        }

        @Override
        public boolean areAllItemsEnabled() {

            return true;

        }

        @Override
        public boolean isEnabled(int position) {

            String seats = strings[position];

            for (int i = 0; i < Booked.size(); i++) {
                if ((seats.equals(Booked.get(i)))) {

                    return false;

                }

            }


            return true;
        }


    }

    public class FoutynineCustomAdapter extends BaseAdapter {
        public List selectedPositions;
        ArrayList<String> Booked_49 = new ArrayList<>(fortynineSeaterList);
        LayoutInflater inflater = null;
        private Context context;
        private String[] strings;


        public FoutynineCustomAdapter(String[] strings, Context context) {
            this.strings = strings;
            this.context = context;
            selectedPositions = new ArrayList<>();


            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        }


        @Override
        public int getViewTypeCount() {
            return getCount();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }


        @Override
        public int getCount() {
            return fortynineSeaterList.size();

        }

        @Override
        public Object getItem(int position) {


            return fortynineSeaterList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder vh;
            HashSet<String> fortyninecommon = new HashSet<>(fortynineSeaterList);
            fortyninecommon.retainAll(seats);

            System.out.println("49 seater similiar " + fortyninecommon);
            System.out.println("seats available " + seats);
            System.out.println("49 Seater List " + fortynineSeaterList);
            int size = Integer.valueOf(seater);
            System.out.println("49 Size " + size);

            Booked_49.removeAll(fortyninecommon);

            String seatsitem = strings[position];


            if (convertView == null) {

                convertView = inflater.inflate(R.layout.grid_item, parent, false);

                vh = new ViewHolder(textView);

                convertView.setTag(vh);


            } else {


                vh = (ViewHolder) convertView.getTag();


            }


            vh.gridtextView = convertView.findViewById(R.id.txt_grid);
            vh.gridtextView.setText(strings[position]);

            vh.gridtextView.setBackgroundResource(R.drawable.seat_normal_booked);

            if (seatsitem.equals("D")) {
                vh.gridtextView.setBackgroundResource(R.drawable.steering);
                vh.gridtextView.setText("");

            }


            int color = 0x00FFFFFF; // Transparent

            if (seatsitem.equals("C")) {
                vh.gridtextView.setBackgroundColor(color);
                vh.gridtextView.setText("");

            }

            for (int i = 0; i < fortyninecommon.size(); i++) {
                if (seats.size() > 16 && seats.size() <= 49) {

                    if (seatsitem.equals(seats.get(i))) {
                        vh.gridtextView.setBackgroundResource(R.drawable.seat_normal);

                    }

                }
            }

            return convertView;

        }

        @Override
        public boolean areAllItemsEnabled() {

            return true;

        }

        @Override
        public boolean isEnabled(int position) {

            String seats = strings[position];

            for (int i = 0; i < Booked_49.size(); i++) {
                if ((seats.equals(Booked_49.get(i)))) {

                    return false;

                }

            }


            return true;
        }


    }


}
