package com.example.oronz.mobiclientapp;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.example.oronz.mobiclientapp.Adapter.PassengerManifestAdapter;
import com.example.oronz.mobiclientapp.Models.PassengerManifestDetails;
import com.example.oronz.mobiclientapp.Utilities.MySingleton;
import com.nbbse.printapi.Printer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SingleManifestUpdate extends AppCompatActivity {
    private MobiClientApplication app;
    ArrayList<PassengerManifestDetails> carmanifestDetails;
    ListView passengerlistView;
    TextView details, name, carreg, ref, seatno;
    EditText reg;
    Button btnupdate;
//    FloatingActionButton fab, fab2, fab3;

    Button prntfull, prntpg1, prntpg2;
    String amount, travel_date;
    int seatsize, manifestsize;
    JSONArray jsonArray;
    InputStream logo;
Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_manifest_update);
        passengerlistView = findViewById(R.id.passewngermanifest);
        carmanifestDetails = new ArrayList<>();

        logo = getResources().openRawResource(R.raw.ena_coach_logo24bit);
        mcontext=getApplicationContext();


        prntfull = findViewById(R.id.btnprntfull);

        prntpg1 = findViewById(R.id.btnprntpg1);
        prntpg2 = findViewById(R.id.btnprntpg2);


        prntpg2.setVisibility(View.GONE);
        prntpg1.setVisibility(View.GONE);
        prntfull.setVisibility(View.GONE);


        app = (MobiClientApplication) getApplication();
        getSupportActionBar().setTitle(app.getManfestSelected());
        details = findViewById(R.id.txtdetails);
        reg = findViewById(R.id.edittextreg_no);
        btnupdate = findViewById(R.id.btnupdate);

        carreg = findViewById(R.id.manifest_pass_name);
        ref = findViewById(R.id.manifest_reg_no);
        seatno = findViewById(R.id.manifest_ref_no);
        loadManifest();


        prntfull.setOnClickListener(v -> printManifest());

        prntpg1.setOnClickListener(v -> printManifestpage1());

        prntpg2.setOnClickListener(v -> printManifestpage2());

    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);

        carmanifestDetails.clear();   //clear the list
        finish();

        super.onBackPressed();
    }


    private void printManifest() {

        Collections.sort(carmanifestDetails, PassengerManifestDetails.Seatno);


        if (Build.MODEL.equals("MobiPrint")) {


            Toast.makeText(getApplicationContext(), " Printing Manifest", Toast.LENGTH_SHORT).show();
            Log.i("Printing manifest:", carmanifestDetails.toString());


            Printer print = Printer.getInstance();
            print.printBitmap(getResources().openRawResource(R.raw.ena_coach_logo24bit));

            print.printText("-----------ENA COACH----------");
            print.printText("--------PO BOX 152-40202-------");
            print.printText("..........KEROKA,KENYA..........");
            print.printText("Date: " + app.getTravel_date());
            print.printText("Cash: " + seatsize * Double.parseDouble(amount));
            print.printText("Vehicle:" + app.getManfestSelected());
            print.printText("Clerk.............sign..........");
            print.printText("Driver............sign..........");
            print.printText("......Passenger Details.........");


            for (int i = 0; i < manifestsize; i++) {
                PassengerManifestDetails passdetails = carmanifestDetails.get(i);
                String phone = passdetails.getPhone().replace("254", "0");
                print.printText(passdetails.getRefno() + "." + phone + " " + passdetails.getSeat() + " " + passdetails.getRoute_from() + " " + passdetails.getReg_no() + ".");

            }
            print.printText("EXPENDITURE******AMOUNT*****");
            print.printText("Fuel        |_______________|");
            print.printText("Meal/Accom  |_______________|");
            print.printText("Washing     |_______________|");
            print.printText("Water       |_______________|");
            print.printText("Repair      |_______________|");
            print.printText("Others      |_______________|");


//        print.printText("Issued on :" + app.getTravel_date());
            print.printText("Issued by :" + app.getLogged_user());
            print.printBitmap(getResources().openRawResource(R.raw.payment_methods_old));
            print.printBitmap(getResources().openRawResource(R.raw.powered_by_mobiticket));
            print.printEndLine();

        } else {
            Toast.makeText(getApplicationContext(), "Device Doesn't Support Printing", Toast.LENGTH_SHORT).show();


        }


    }

    private void printManifestpage1() {

        Collections.sort(carmanifestDetails, PassengerManifestDetails.Seatno);

        if (Build.MODEL.equals("MobiPrint")) {

            Toast.makeText(getApplicationContext(), " Printing Manifest page 1", Toast.LENGTH_SHORT).show();
            Log.i("Printing manifest:", carmanifestDetails.toString());


            Printer print = Printer.getInstance();
            print.printBitmap(getResources().openRawResource(R.raw.ena_coach_logo24bit));
            print.printText("-----------ENA COACH----------");
            print.printText("--------PO BOX 152-40202-------");
            print.printText("..........KEROKA,KENYA..........");
            print.printText("Date: " + app.getTravel_date());
            print.printText("Cash: " + seatsize * Double.parseDouble(amount));
            print.printText("Vehicle:" + app.getManfestSelected());
            print.printText("Clerk.............sign..........");
            print.printText("Driver............sign..........");
            print.printText("......Passenger Details.........");


            for (int i = 0; i < 22; i++) {
                PassengerManifestDetails passdetails = carmanifestDetails.get(i);
                String phone = passdetails.getPhone().replace("254", "0");
                print.printText(passdetails.getRefno() + ". " + phone + " " + passdetails.getSeat() + " " + passdetails.getRoute_from() + " " + passdetails.getReg_no() + "\n");
            }
        } else {

            Toast.makeText(getApplicationContext(), "Device Doesn't Support Printing", Toast.LENGTH_SHORT).show();

        }


    }

    private void printManifestpage2() {

        if (Build.MODEL.equals("MobiPrint")) {

            Collections.sort(carmanifestDetails, PassengerManifestDetails.Seatno);


            Toast.makeText(getApplicationContext(), " Printing Manifest page 2", Toast.LENGTH_SHORT).show();
            Log.i("Printing manifest:", carmanifestDetails.toString());


            Printer print = Printer.getInstance();


            for (int i = 23; i < manifestsize; i++) {
                PassengerManifestDetails passdetails = carmanifestDetails.get(i);
                String phone = passdetails.getPhone().replace("254", "0");
                print.printText(passdetails.getRefno() + " " + phone + " " + passdetails.getSeat() + " " + passdetails.getRoute_from() + " " + passdetails.getReg_no() + "\n");

            }
            print.printText("EXPENDITURE******AMOUNT*****");
            print.printText("Fuel        |_______________|");
            print.printText("Meal/Accom  |_______________|");
            print.printText("Washing     |_______________|");
            print.printText("Water       |_______________|");
            print.printText("Repair      |_______________|");
            print.printText("Others      |_______________|");


            print.printText("Issued on :" + app.getTravel_date());
            print.printText("Issued by :" + app.getLogged_user());
            print.printBitmap(getResources().openRawResource(R.raw.payment_methods_old));
            print.printBitmap(getResources().openRawResource(R.raw.powered_by_mobiticket));
            print.printEndLine();
        } else {

            Toast.makeText(getApplicationContext(), "Device Doesn't Support Printing", Toast.LENGTH_SHORT).show();

        }


    }


    private void loadManifest() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "PassengerManifest");
        params.put("travel_date", app.getManifestDate());
        params.put("hash", app.getHash_key());

        params.put("route", app.getManfestSelected());

        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getInt("response_code") == 0) {
                                jsonArray = response.getJSONArray("tickets");
                                Log.i("Response:", jsonArray.toString());

                                manifestsize = jsonArray.length();
                                seatsize = manifestsize;

                                Log.d("Manifest Size", String.valueOf(manifestsize));


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String name = jsonObject1.getString("name");
                                    String seat = jsonObject1.getString("seat");
                                    String refreference_numbero = jsonObject1.getString("reference_number");
                                    String reg_no = jsonObject1.getString("reg_number");
                                    String phone = jsonObject1.getString("msisdn");
                                    String route_to = jsonObject1.getString("travel_to");
                                    String route_from = jsonObject1.getString("travel_from");


                                    amount = jsonObject1.getString("amount");
                                    travel_date = jsonObject1.getString("travel_date");


                                    carmanifestDetails.add(new PassengerManifestDetails(name, seat, refreference_numbero, reg_no, phone, route_to, route_from));

                                }


                            } else {
                                Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();
                                details.setText(response.getString("response_message"));
                                reg.setVisibility(View.GONE);
                                btnupdate.setVisibility(View.GONE);

                            }

                            PassengerManifestAdapter tripsArrayAdapter = new PassengerManifestAdapter(SingleManifestUpdate.this, carmanifestDetails);

                            passengerlistView.setAdapter(tripsArrayAdapter);


                            if (manifestsize > 0 && manifestsize < 17) {
                                prntpg2.setVisibility(View.GONE);
                                prntpg1.setVisibility(View.GONE);
                                prntfull.setVisibility(View.VISIBLE);


                            } else {
                                prntfull.setVisibility(View.GONE);
                                prntpg2.setVisibility(View.VISIBLE);
                                prntpg1.setVisibility(View.VISIBLE);
                            }
                            tripsArrayAdapter.notifyDataSetChanged();


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


}
