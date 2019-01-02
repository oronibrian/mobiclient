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
import java.util.HashMap;

public class SingleManifestUpdate extends AppCompatActivity {
    private MobiClientApplication app;
    ArrayList<PassengerManifestDetails> carmanifestDetails;
    ListView passengerlistView ;
    TextView details;
    EditText reg;
    Button btnupdate;
    FloatingActionButton fab;

    PassengerManifestDetails det;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_manifest_update);
        passengerlistView = findViewById(R.id.passewngermanifest);
        carmanifestDetails = new ArrayList<>();
        fab = findViewById(R.id.fabprint);

        app = (MobiClientApplication) getApplication();
        getSupportActionBar().setTitle("Manifest:"+app.getManfestSelected());
        details=findViewById(R.id.txtdetails);
        reg=findViewById(R.id.edittextreg_no);
        btnupdate=findViewById(R.id.btnupdate);

        loadManifest();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printManifest();
            }
        });
    }

    private void printManifest() {


        for (int i = 0; i < carmanifestDetails.size(); i++) {
                Toast.makeText(getApplicationContext(), " Printing Manifest", Toast.LENGTH_LONG).show();
            Log.i("Printing manifest:", carmanifestDetails.toString());

            Printer print = Printer.getInstance();
                print.printFormattedText();
                print.printBitmap(getResources().openRawResource(R.raw.ena_coach_logo24bit));
                print.printText("-----------ENA COACH----------");
                print.printText("--------PO BOX 152-40202-------");
                print.printText("..........KEROKA,KENYA..........");
                print.printText("......Passenger Details.........");
                print.printText("Name: " + carmanifestDetails.get(1));
//                print.printText("Ref No:" + json_obj.getString("merchant_transaction_id"));
//                print.printText("Phone No:" + user.getPhone());
//                print.printText("Seat:" + user.getSeat());
//                print.printText("Fare: Ksh." + json_obj.getString("fare"));
//                print.printText("................................");
//                print.printText("......Vehicle Details.........");
//                print.printText("Vehicle:" + json_obj.getString("bus"));
//                print.printText("Route:" + json_obj.getString("route"));
//                print.printText("Travel Date: " + json_obj.getString("travel_date"));
//                print.printText("................................");
//                print.printText("Issued On :" + currentDateandTime);
//                print.printText("Issued by :" + app.getLogged_user());
                print.printBitmap(getResources().openRawResource(R.raw.payment_methods_old));
                print.printBitmap(getResources().openRawResource(R.raw.powered_by_mobiticket));
                print.printEndLine();


        }
    }

    private void loadManifest() {
        RequestQueue requestQueue = Volley.newRequestQueue(SingleManifestUpdate.this);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "PassengerManifest");
        params.put("travel_date",app.getManifestDate());
        params.put("hash", app.getHash_key());

        params.put("route",app.getManfestSelected());

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



                                    carmanifestDetails.add(new PassengerManifestDetails(name,seat,refreference_numbero,reg_no));

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
