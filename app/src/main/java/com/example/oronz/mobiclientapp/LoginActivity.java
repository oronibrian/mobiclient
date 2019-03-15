package com.example.oronz.mobiclientapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.oronz.mobiclientapp.Utilities.SaveSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class LoginActivity extends AppCompatActivity {

    Button btnlogin;
    EditText editextpassword,edittextusername;
    private MobiClientApplication app;
    private ProgressDialog mProgress;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Password = "passwordKey";
    SharedPreferences sharedpreferences;

    ImageView imageView;
    TextView txtnointernet;

    SharedPreferences sharedPreferences;

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
        imageView=findViewById(R.id.imageView1);
        txtnointernet=findViewById(R.id.txtnointernet);


        app.set_Clerk_username(edittextusername.getText().toString());
         sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        btnlogin.setVisibility(View.GONE);





        // Check if UserResponse is Already Logged In


            if(SaveSharedPreference.getLoggedStatus(getApplicationContext())) {


                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Toast.makeText(getApplicationContext(),
                        "Welcome Back" + app.getLogged_user(), Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
            else {

                btnlogin.setVisibility(View.VISIBLE);



        }





        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            // notify user you are online

        } else {
            // notify user you are not online
            btnlogin.setVisibility(View.GONE);
            editextpassword.setVisibility(View.GONE);
            edittextusername.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.nointernet);
            txtnointernet.setText("No Internet Connection");


        }

    }


    private void initializedAPI() {

        app.setUser_name("rWIv7GWzSp");
        app.setApi_key("831b238c5cd73308520e38bbc6c1774f470a89e96d07a5bb6bcac3b86456f889");
        app.setHash_key("1FBEAD9B-D9CD-400D-ADF3-F4D0E639CEE0");


    }


    private void login() {


        final String email = edittextusername.getText().toString();
        final String password = editextpassword.getText().toString();

        app.setAgency_phone(email);
        app.set_Clerk_password(password);


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
                                String first_name = response.getString("first_name");
                                String last_name = response.getString("last_name");

                                app.setLogged_user(first_name +" "+last_name );



                                Log.d("log in ", first_name);

                                mProgress.dismiss();

                                SaveSharedPreference.setLoggedIn(getApplicationContext(), true);


                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", email);
                                editor.putString("password", password);
                                editor.apply();


                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);



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
//            reserverequestQueue.getCache().clear();

            reserverequestQueue.add(req);

        }
    }
}
