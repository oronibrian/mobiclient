package com.example.oronz.mobiclientapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
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

public class Seats_activity extends AppCompatActivity implements View.OnClickListener {
    ViewGroup layout;
    List<String> viti;
    GridView gridView;
    List<String> listofseats = new ArrayList<String>();

    String seats = "A__XR/"
            + "U_AA/"
            + "U_UU/"
            + "A_AA/"
            + "A_AU/"
            + "UXUU/";

    List<TextView> seatViewList = new ArrayList<>();
    int seatSize = 100;
    int seatGaping = 10;

    int STATUS_AVAILABLE = 1;
    int STATUS_BOOKED = 2;
    int STATUS_RESERVED = 3;
    String selectedIds = "";
    MobiClientApplication app;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seats_activity);
        app = (MobiClientApplication) getApplication();


        availableSeats();


        layout = findViewById(R.id.activity_linear_layout);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.activity_linear_layout);

        //added LayoutParams
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);


        Button ok = new Button(this);
        ok.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ok.setBackgroundColor(R.color.colorPrimary);
        ok.setText("Book");
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //added the textView and the Button to LinearLayout


        seats = "/" + seats;

        LinearLayout layoutSeat = new LinearLayout(this);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutSeat.setOrientation(LinearLayout.VERTICAL);
        layoutSeat.setLayoutParams(params);
        layoutSeat.setPadding(8 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping);



        Button btncancel = new Button(this);
        btncancel.setText("Cancel");
        btncancel.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "welcome_message Booked", Toast.LENGTH_LONG).show();
            }
        });
        TextView txt = new TextView(this);
        txt.setText("Boook A seat");
        txt.setTextSize(30);
        txt.setGravity(4);
        txt.setTextColor(Color.BLACK);

        linearLayout.addView(txt);
        layout.addView(layoutSeat);

        linearLayout.addView(ok);
        linearLayout.addView(btncancel);



        LinearLayout layout = null;

        int count = 0;

        for (int index = 0; index < seats.length(); index++) {
            if (seats.charAt(index) == '/') {
                layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layoutSeat.addView(layout);
            } else if (seats.charAt(index) == 'U') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.ic_seats_b);
                view.setTextColor(Color.WHITE);
                view.setTag(STATUS_BOOKED);
                view.setText(count + "");
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            } else if (seats.charAt(index) == 'A') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.ic_seats_empty_seats);
                view.setText(count + "");
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                view.setTextColor(Color.BLACK);
                view.setTag(STATUS_AVAILABLE);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            } else if (seats.charAt(index) == 'R') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.steering);
                view.setText(count + "");
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                view.setTextColor(Color.WHITE);
                view.setTag(STATUS_RESERVED);
                layout.addView(view);
                seatViewList.add(view);
//                view.setOnClickListener(this);
            } else if (seats.charAt(index) == '_') {
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setText("");
                layout.addView(view);
            } else if (seats.charAt(index) == 'X') {
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setBackgroundColor(Color.TRANSPARENT);
                view.setText("");
                view.setVisibility(View.GONE);

                layout.addView(view);
            }
        }
    }



    @Override
    public void onClick(View view) {
        if ((int) view.getTag() == STATUS_AVAILABLE) {

            if (selectedIds.contains(view.getId() + ",")) {

                selectedIds = selectedIds.replace(+view.getId() + ",", "");
                view.setBackgroundResource(R.drawable.ic_seats_book);
            } else {
                selectedIds = selectedIds + view.getId() + ",";
                view.setBackgroundResource(R.drawable.ic_seats_selected);
            }


        } else if ((int) view.getTag() == STATUS_BOOKED) {
            Toast.makeText(this, "Seat " + view.getId() + " is Booked", Toast.LENGTH_SHORT).show();
        } else if ((int) view.getTag() == STATUS_RESERVED) {
            Toast.makeText(this, "Seat " + view.getId() + " is Reserved", Toast.LENGTH_SHORT).show();
        }
    }


    private void availableSeats() {

        RequestQueue datesrequestQueue = Volley.newRequestQueue(getApplicationContext());

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "AvailableSeats");

        params.put("from_city", app.getTravel_from());
        params.put("to_city", app.getTravel_too());
        params.put("travel_date", "12-09-2018");
        params.put("hash", app.getHash_key());
        params.put("selected_vehicle", "13");

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
                                    viti = new ArrayList<>(Arrays.asList(bus.split(",")));


//                                    ticketType();

//
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Seats_activity.this);
                                    builder.setView(gridView);
                                    builder.setTitle("Select seat");
                                    builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
                                        public void onClick(android.content.DialogInterface dialog, int id) {
                                            Log.d("List of seats:%n %s", String.valueOf(listofseats));
//                                            payment();

                                        }
                                    })


                                            .setNegativeButton("Cancel", null);


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

}
