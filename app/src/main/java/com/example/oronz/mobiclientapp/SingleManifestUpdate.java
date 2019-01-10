package com.example.oronz.mobiclientapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.nbbse.printapi.Printer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    FloatingActionButton fab;
    String amount,travel_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_manifest_update);
        passengerlistView = findViewById(R.id.passewngermanifest);
        carmanifestDetails = new ArrayList<>();
        fab = findViewById(R.id.fabprint);

        app = (MobiClientApplication) getApplication();
        getSupportActionBar().setTitle("Manifest:" + app.getManfestSelected());
        details = findViewById(R.id.txtdetails);
        reg = findViewById(R.id.edittextreg_no);
        btnupdate = findViewById(R.id.btnupdate);

        carreg = findViewById(R.id.manifest_pass_name);
        ref = findViewById(R.id.manifest_reg_no);
        seatno = findViewById(R.id.manifest_ref_no);
        loadManifest();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printManifest();
            }
        });
    }

    private void printManifest() {

        Collections.sort(carmanifestDetails, PassengerManifestDetails.Seatno);


        Toast.makeText(getApplicationContext(), " Printing Manifest", Toast.LENGTH_LONG).show();
        Log.i("Printing manifest:", carmanifestDetails.toString());

        int seatsize=carmanifestDetails.size();

        Printer print = Printer.getInstance();
        print.printFormattedText();
        print.printBitmap(getResources().openRawResource(R.raw.ena_coach_logo24bit));
        print.printText("-----------ENA COACH----------");
        print.printText("--------PO BOX 152-40202-------");
        print.printText("..........KEROKA,KENYA..........\n");

        print.printText("Date: "+travel_date);
        print.printText("Cash: "+seatsize*Integer.valueOf(amount));

        print.printText("Vehicle:" + app.getManfestSelected() +"\n");

        print.printText("Clerk.............sign..........");
        print.printText("Driver............sign..........");
        print.printText("Conductor.........sign..........\n");

        print.printText("......Passenger Details.........\n");




        for (int i = 0; i < carmanifestDetails.size(); i++) {
            PassengerManifestDetails passdetails = carmanifestDetails.get(i);
            String phone = passdetails.getPhone().replace("254","0");

            print.printText(passdetails.getRefno() + " " + phone + " " + passdetails.getSeat() + " " + passdetails.getReg_no() +"\n");

        }
        print.printFormattedText();
        print.printText("**************************");
        print.printText("EXPENDITURE|    AMOUNT      |\n" +
                "Fuel        |_______________|\n" +
                "Meal/Accom  |_______________|\n" +
                "Toll/Gates  |_______________|\n" +
                "Washing     |_______________|\n" +
                "Water       |_______________|\n" +
                "Repair      |_______________|\n" +
                "Others      |_______________|\n");

        print.printText("Issued on :" + travel_date);
        print.printText("Issued by :" + app.getLogged_user());
        print.printBitmap(getResources().openRawResource(R.raw.payment_methods_old));
        print.printBitmap(getResources().openRawResource(R.raw.powered_by_mobiticket));
        print.printEndLine();


    }

    private void loadManifest() {
        RequestQueue requestQueue = Volley.newRequestQueue(SingleManifestUpdate.this);
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
                                JSONArray jsonArray = response.getJSONArray("tickets");
                                Log.i("Response:", jsonArray.toString());


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
                                    travel_date= jsonObject1.getString("travel_date");


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


}
