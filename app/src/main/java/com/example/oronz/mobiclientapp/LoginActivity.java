package com.example.oronz.mobiclientapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    Button btnlogin;
    EditText editextpassword,edittextusername;
    private MobiClientApplication app;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        app = (MobiClientApplication) getApplication();

        initializedAPI();

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Signing  in...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);


        btnlogin=findViewById(R.id.btnlogin);
        editextpassword=findViewById(R.id.editextpassword);
        edittextusername=findViewById(R.id.edittextusername);


        app.set_Clerk_username(edittextusername.getText().toString());
        app.set_Clerk_password(editextpassword.getText().toString());

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }


    private void initializedAPI() {

        app.setUser_name("rWIv7GWzSp");
        app.setApi_key("831b238c5cd73308520e38bbc6c1774f470a89e96d07a5bb6bcac3b86456f889");
        app.setHash_key("1FBEAD9B-D9CD-400D-ADF3-F4D0E639CEE0");


    }


    private void login() {


        final String email = edittextusername.getText().toString();
        final String password = editextpassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(email)) {
            edittextusername.setError("Please enter your email address or phone");
            edittextusername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editextpassword.setError("Please enter your password");
            editextpassword.requestFocus();
            return;
        } else {

            mProgress.show();

            RequestQueue reserverequestQueue = Volley.newRequestQueue(LoginActivity.this);
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("username", app.getUser_name());
            params.put("api_key", app.getApi_key());
            params.put("action", "UserLogin");
            params.put("clerk_username", email);
            params.put("clerk_password", password);


            JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                    response -> {
                        try {

                            if (response.getInt("response_code") == 0) {
                                String message = response.getString("response_message");

                                Log.d("log in ", message);

                                mProgress.dismiss();

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));


                            } else {
                                Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();


                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();

                    } else if (error instanceof ServerError) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();

                    } else if (error instanceof NetworkError) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();

                    } else if (error instanceof ParseError) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();

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
    }
}
