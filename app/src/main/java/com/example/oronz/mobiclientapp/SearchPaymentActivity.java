package com.example.oronz.mobiclientapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.oronz.mobiclientapp.API.URLs;
import com.example.oronz.mobiclientapp.Utilities.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SearchPaymentActivity extends AppCompatActivity {
    EditText ref_no;
    Button btnsearch;
    String ref;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_payment);

        ref_no=findViewById(R.id.editextseachpayment);
        btnsearch=findViewById(R.id.btnSearchpayment);
        textView=findViewById(R.id.textView);



        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref = ref_no.getText().toString().trim(); //get text from editText

                searchMpesaTransaction();

            }
        });



    }


    private void searchMpesaTransaction(){

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("developer_username", "emuswailit");
        params.put("developer_api_key", "c8e254c0adbe4b2623ff85567027d78d4cc066357627e284d4b4a01b159d97a7");
        params.put("action", "searchpayment");
        params.put("identifier", ref);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, URLs.Pay_Confim_URL, new JSONObject(params),
                response -> {
                    try {
                        String code = response.getString("response_code");

                        if (code.equals("0")) {


                            String message = response.getString("response_message");

                            Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

                            Log.d("mPesa Payment Response", message);

                            textView.setText(response.getString("response_message"));




                        } else {


                            Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();

                            Log.d("Mpesa Payment Response", response.getString("response_message"));



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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);



    }

}
