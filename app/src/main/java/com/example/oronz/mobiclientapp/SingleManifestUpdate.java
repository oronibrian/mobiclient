package com.example.oronz.mobiclientapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.oronz.mobiclientapp.Adapter.ManifestAdapter;
import com.example.oronz.mobiclientapp.Adapter.PassengerManifestAdapter;
import com.example.oronz.mobiclientapp.Models.ManifestDetails;
import com.example.oronz.mobiclientapp.Models.PassengerManifestDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SingleManifestUpdate extends AppCompatActivity {
    private MobiClientApplication app;
    ArrayList<PassengerManifestDetails> carmanifestDetails;
    ListView passengerlistView ;
    TextView details;
    EditText reg;
    Button btnupdate;

    PassengerManifestDetails det;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_manifest_update);
        passengerlistView = (ListView) findViewById(R.id.passewngermanifest);
        carmanifestDetails = new ArrayList<PassengerManifestDetails>();

        app = (MobiClientApplication) getApplication();
        getSupportActionBar().setTitle("Manifest:"+app.getManfestSelected());
        details=findViewById(R.id.txtdetails);
        reg=findViewById(R.id.edittextreg_no);
        btnupdate=findViewById(R.id.btnupdate);

        loadManifest();
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
