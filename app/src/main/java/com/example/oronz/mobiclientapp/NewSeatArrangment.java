package com.example.oronz.mobiclientapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class NewSeatArrangment extends AppCompatActivity implements View.OnClickListener {
    ViewGroup layout;
    MobiClientApplication app;

    String fortynineseats = "____D/"
                            + "____/"
                            + "UU_AA/"
                            + "UU_UA/"
                            + "___UA/"
                            + "AA_AA/"
                            + "AA_AA/"
                            + "UU_UU/"
                            + "AA_AA/"
                            + "AA_AA/"
                            + "AA_UU/"
                            + "AA_UU/"
                            + "AA_UU/"
                            + "U25AA/";


    String elevenseats = "UUD/"
                        + "UUA/"
                        + "UUU/"
                        + "UUA/";

    List<TextView> seatViewList = new ArrayList<>();
    int seatSize = 69;
    int seatGaping = 5;

    int STATUS_AVAILABLE = 1;
    int STATUS_BOOKED = 2;
    int STATUS_RESERVED = 3;
    String selectedIds = "";

    List<String> seats;
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_seat_arrangment);
        layout = findViewById(R.id.layoutSeat);
        app = (MobiClientApplication) getApplication();

        fortynineseats = "/" + fortynineseats;
        elevenseats = "/" + elevenseats;



        availableSeats();

    }

    @Override
    public void onClick(View view) {
        if ((int) view.getTag() == STATUS_AVAILABLE) {
            if (selectedIds.contains(view.getId() + ",")) {
                selectedIds = selectedIds.replace(+view.getId() + ",", "");
                view.setBackgroundResource(R.drawable.ic_seats_b);
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


        RequestQueue datesrequestQueue = Volley.newRequestQueue(NewSeatArrangment.this);

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


                        if (response.getString("response_code").equals("0")) {

                            JSONArray jsonArray = response.getJSONArray("bus");

                            JSONObject jsonObject1 = jsonArray.getJSONObject(Integer.parseInt(app.getIndex()));
                            JSONArray bus_array = jsonObject1.getJSONArray("seats");


                            Log.d("#####SIZE", String.valueOf(bus_array.length()));


                            for (int x = 0; x < bus_array.length(); x++) {
                                JSONObject obj = bus_array.getJSONObject(x);
                                String gari = obj.getString("name");

                                gari = gari.replace(" ", "");

                                Log.d("##### Seats", gari);

                                seats = new ArrayList<>(Arrays.asList(gari.split(",")));

                            }



                            if (seats.size() <= 11) {

                                LinearLayout layoutSeat = new LinearLayout(this);
                                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                layoutSeat.setOrientation(LinearLayout.VERTICAL);
                                layoutSeat.setLayoutParams(param);
                                layoutSeat.setPadding(8 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping);
                                layout.addView(layoutSeat);

                                LinearLayout layout = null;


                                for (int index = 0; index < elevenseats.length(); index++) {

                                    if (elevenseats.charAt(index) == '/') {
                                        layout = new LinearLayout(this);
                                        layout.setOrientation(LinearLayout.HORIZONTAL);
                                        layoutSeat.addView(layout);
                                    } else if (elevenseats.charAt(index) == 'U') {
                                        count++;
                                        TextView view = new TextView(this);
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                                        layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                                        view.setLayoutParams(layoutParams);
                                        view.setPadding(0, 0, 0, 2 * seatGaping);
                                        view.setId(count);
                                        view.setGravity(Gravity.CENTER);
                                        view.setBackgroundResource(R.drawable.ic_seats_booked);
                                        view.setTextColor(Color.WHITE);
                                        view.setTag(STATUS_BOOKED);
                                        view.setText(count + "");
                                        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                                        layout.addView(view);
                                        seatViewList.add(view);
                                        view.setOnClickListener(this);
                                    } else if (elevenseats.charAt(index) == 'A') {
                                        count++;
                                        TextView view = new TextView(this);
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                                        layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                                        view.setLayoutParams(layoutParams);
                                        view.setPadding(0, 0, 0, 2 * seatGaping);
                                        view.setId(count);
                                        view.setGravity(Gravity.CENTER);
                                        view.setBackgroundResource(R.drawable.ic_seats_b);
                                        view.setText(count + "");
                                        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                                        view.setTextColor(Color.BLACK);
                                        view.setTag(STATUS_AVAILABLE);
                                        layout.addView(view);
                                        seatViewList.add(view);
                                        view.setOnClickListener(this);
                                    } else if (elevenseats.charAt(index) == 'D') {
                                        count++;
                                        TextView view = new TextView(this);
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                                        layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                                        view.setLayoutParams(layoutParams);
                                        view.setPadding(0, 0, 0, 2 * seatGaping);
                                        view.setId(count);
                                        view.setGravity(Gravity.CENTER);
                                        view.setBackgroundResource(R.drawable.ic_seats_reserved);
                                        view.setText(count + "");
                                        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                                        view.setTextColor(Color.WHITE);
                                        view.setTag(STATUS_RESERVED);
                                        layout.addView(view);
                                        seatViewList.add(view);
                                        view.setOnClickListener(this);
                                    } else if (elevenseats.charAt(index) == '_') {
                                        TextView view = new TextView(this);
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                                        layoutParams.setMargins(1, 1, 1, 1);
                                        view.setLayoutParams(layoutParams);
                                        view.setBackgroundColor(Color.TRANSPARENT);
                                        view.setText("");
                                        layout.addView(view);
                                    }
                                }


                            } else if (seats.size() > 14 && seats.size() <= 49) {

                                LinearLayout layoutSeat = new LinearLayout(this);
                                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                layoutSeat.setOrientation(LinearLayout.VERTICAL);
                                layoutSeat.setLayoutParams(param);
                                layoutSeat.setPadding(8 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping);
                                layout.addView(layoutSeat);

                                LinearLayout layout = null;


                                for (int index = 0; index < fortynineseats.length(); index++) {
                                    if (fortynineseats.charAt(index) == '/') {
                                        layout = new LinearLayout(this);
                                        layout.setOrientation(LinearLayout.HORIZONTAL);
                                        layoutSeat.addView(layout);
                                    } else if (fortynineseats.charAt(index) == 'U') {
                                        count++;
                                        TextView view = new TextView(this);
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                                        layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                                        view.setLayoutParams(layoutParams);
                                        view.setPadding(0, 0, 0, 2 * seatGaping);
                                        view.setId(count);
                                        view.setGravity(Gravity.CENTER);
                                        view.setBackgroundResource(R.drawable.ic_seats_booked);
                                        view.setTextColor(Color.WHITE);
                                        view.setTag(STATUS_BOOKED);
                                        view.setText(count + "");
                                        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                                        layout.addView(view);
                                        seatViewList.add(view);
                                        view.setOnClickListener(this);
                                    } else if (fortynineseats.charAt(index) == 'A') {
                                        count++;
                                        TextView view = new TextView(this);
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                                        layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                                        view.setLayoutParams(layoutParams);
                                        view.setPadding(0, 0, 0, 2 * seatGaping);
                                        view.setId(count);
                                        view.setGravity(Gravity.CENTER);
                                        view.setBackgroundResource(R.drawable.ic_seats_b);
                                        view.setText(count + "");
                                        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                                        view.setTextColor(Color.BLACK);
                                        view.setTag(STATUS_AVAILABLE);
                                        layout.addView(view);
                                        seatViewList.add(view);
                                        view.setOnClickListener(this);
                                    } else if (fortynineseats.charAt(index) == 'D') {
                                        count++;
                                        TextView view = new TextView(this);
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                                        layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                                        view.setLayoutParams(layoutParams);
                                        view.setPadding(0, 0, 0, 2 * seatGaping);
                                        view.setId(count);
                                        view.setGravity(Gravity.CENTER);
                                        view.setBackgroundResource(R.drawable.ic_seats_reserved);
                                        view.setText(count + "");
                                        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                                        view.setTextColor(Color.WHITE);
                                        view.setTag(STATUS_RESERVED);
                                        layout.addView(view);
                                        seatViewList.add(view);
                                        view.setOnClickListener(this);
                                    } else if (fortynineseats.charAt(index) == '_') {
                                        TextView view = new TextView(this);
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                                        layoutParams.setMargins(1, 1, 1, 1);
                                        view.setLayoutParams(layoutParams);
                                        view.setBackgroundColor(Color.TRANSPARENT);
                                        view.setText("");
                                        layout.addView(view);
                                    }
                                }



                            }

                            else if (seats.size() > 11 && seats.size() <= 14) {


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

}
