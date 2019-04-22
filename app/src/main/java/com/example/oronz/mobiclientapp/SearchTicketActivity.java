package com.example.oronz.mobiclientapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.example.oronz.mobiclientapp.Adapter.MyTripsArrayAdapter;
import com.example.oronz.mobiclientapp.Models.MytripsDetails;
import com.example.oronz.mobiclientapp.Utilities.MySingleton;
import com.nbbse.printapi.Printer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SearchTicketActivity extends AppCompatActivity {
    MobiClientApplication app;
    ArrayList<MytripsDetails> mytripsDetails;
    ListView mytripslistView;
    EditText editTextphone;
    Button btnsearch;
    String phoneno;
    private ProgressDialog mProgress;
    private Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MobiClientApplication) getApplication();

        setContentView(R.layout.activity_my_trips);

        mytripslistView = (ListView) findViewById(R.id.mytripslistView);

        mytripsDetails = new ArrayList<MytripsDetails>();

        editTextphone = findViewById(R.id.editTextphone);
        btnsearch = findViewById(R.id.btnSearch);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Ticket");

        phoneno = app.getAgency_phone();

        mProgress = new ProgressDialog(this);

        mProgress.setTitle("Searching Ticket...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(true);
        mProgress.setIndeterminate(true);
        mcontext=getApplicationContext();


        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextphone.length() < 10 && editTextphone.length() > 10) {
                    editTextphone.setError("Please Enter valid phone number Or Ref No");

                } else {
                    phoneno = editTextphone.getText().toString();
                    mytripsDetails = new ArrayList<MytripsDetails>();


                    getTickets();

                }

            }
        });


        mytripslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String Selected_payment_type = String.valueOf(mytripslistView.getSelectedItemPosition());

                TextView textView = (TextView) view.findViewById(R.id.seatsbooked);
                textView.setVisibility(View.GONE);
                String ref = textView.getText().toString();

                TextView amounttxt = (TextView) view.findViewById(R.id.textView2);
                String amoumt = amounttxt.getText().toString();

                amounttxt.setVisibility(View.GONE);

                TextView vehicletxt = (TextView) view.findViewById(R.id.routeTextView);
                String vehicle = vehicletxt.getText().toString();
                textView.setVisibility(View.GONE);


                TextView tootxt = (TextView) view.findViewById(R.id.too);
                String too = tootxt.getText().toString();
                tootxt.setVisibility(View.GONE);


                TextView fromtxt = (TextView) view.findViewById(R.id.manifestavilableseats);
                String from = fromtxt.getText().toString();
                fromtxt.setVisibility(View.GONE);

                TextView departuretxt = (TextView) view.findViewById(R.id.travel_date);
                String departure = departuretxt.getText().toString();


                TextView passengernametxt = (TextView) view.findViewById(R.id.passengername);
                String passengername = passengernametxt.getText().toString();


                TextView phonetxt = (TextView) view.findViewById(R.id.phonenum);
                String phonenum = phonetxt.getText().toString();

                TextView issuedtxt = (TextView) view.findViewById(R.id.issuedon);
                String issuedon = issuedtxt.getText().toString();

                TextView seattxt = (TextView) view.findViewById(R.id.seat);
                String seat = seattxt.getText().toString();


                if (Build.MODEL.equals("MobiPrint")) {
                    Printer print = Printer.getInstance();
                    print.printFormattedText();
                    print.printBitmap(getResources().openRawResource(R.raw.ena_coach_logo24bit));
                    print.printText("-----------ENA COACH----------");
                    print.printText("--------PO BOX 152-40202-------");
                    print.printText("..........KEROKA,KENYA..........");
                    print.printText("......Passenger Details.........");

                    print.printText("Name: " + passengername);
                    print.printText("Ref No:" + ref);
                    print.printText("Phone No:" + phonenum);
                    print.printText("Seat:" + seat);
                    print.printText("Fare: Ksh." + amoumt);

                    print.printText("................................");
                    print.printText("......Vehicle Details.........");
                    print.printText("Vehicle:" + vehicle);
                    print.printText("Route:" + too + " " + from);
                    print.printText("Travel Date: " + departure);
                    print.printText("................................");
                    print.printText("Issued On :" + issuedon);
                    print.printText("Issued by :" + app.getLogged_user());

                    print.printBitmap(getResources().openRawResource(R.raw.payment_methods_old));
                    print.printBitmap(getResources().openRawResource(R.raw.powered_by_mobiticket));
                    print.printEndLine();
                }
            }
        });


    }


    private void getTickets() {

        mProgress.show();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "SearchTicket");
        params.put("identifier", phoneno);

        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getInt("response_code") == 0) {
                                JSONArray jsonArray = response.getJSONArray("tickets");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    Log.d("Ticket details ", jsonObject1.toString());

                                    String car_name = jsonObject1.getString("vehicle_name");
                                    String travel_from = jsonObject1.getString("travel_from");
                                    String travel_to = jsonObject1.getString("travel_to");
                                    String travel_date = jsonObject1.getString("travel_date");
                                    String reference_number = jsonObject1.getString("reference_number");
                                    String amount = jsonObject1.getString("amount");
                                    String seat = jsonObject1.getString("seat");

                                    String name = jsonObject1.getString("name");
                                    String phone = jsonObject1.getString("msisdn");
                                    String date_issued = jsonObject1.getString("payment_date");

                                    mytripsDetails.add(new MytripsDetails(car_name, travel_from, travel_to, travel_date, reference_number, amount, seat, name, phone, date_issued));
                                    mProgress.dismiss();

                                }

                            } else {
                                Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();

                            }

                            MyTripsArrayAdapter tripsArrayAdapter = new MyTripsArrayAdapter(SearchTicketActivity.this, mytripsDetails);

                            mytripslistView.setAdapter(tripsArrayAdapter);


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
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }


        };

        MySingleton.getInstance(mcontext).addToRequestQueue(req);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

}
