package com.example.oronz.mobiclientapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
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
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.oronz.mobiclientapp.API.URLs;
import com.example.oronz.mobiclientapp.Utilities.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import spencerstudios.com.fab_toast.FabToast;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class LoginActivity extends AppCompatActivity {

    Button btnlogin;
    EditText editextpassword, edittextusername;
    private MobiClientApplication app;
    private ProgressDialog mProgress;
    private Context mContext;


    ImageView imageView;
    TextView txtnointernet;

    SharedPreferences sp;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        app = (MobiClientApplication) getApplication();

        mContext = getApplicationContext();

        sp=getSharedPreferences("Login", 0);

        initializedAPI();

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Signing  in...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);


        btnlogin = findViewById(R.id.btnlogin);
        editextpassword = findViewById(R.id.editextpassword);
        edittextusername = findViewById(R.id.edittextusername);
        imageView = findViewById(R.id.imageView1);
        txtnointernet = findViewById(R.id.txtnointernet);


        app.set_Clerk_username(edittextusername.getText().toString());

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


//        String unm=sp.getString("username", null);
//        String pass = sp.getString("password", null);
//
////        Log.d("unm",unm);
////        Log.d("pass",pass);
//
//
//        if (sp.getString("username",null).equals(app.get_Clerk_username())){
//            Toast.makeText(getApplicationContext(),"Welcome Back" + unm,Toast.LENGTH_LONG).show();
//            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//        }
//
//


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
                                String email_address = response.getString("email_address");
                                String phone_num = response.getString("phone_number");


                                app.setLogged_user(first_name + " " + last_name);
                                app.setPhone_num(phone_num);
                                app.setEmail_address(email_address);


                                Log.d("log in ", first_name);

                                Log.d("Response",response.getString("response_message"));


                                mProgress.dismiss();

                                SharedPreferences.Editor Ed=sp.edit();
                                Ed.putString("username",email );
                                Ed.putString("password",password);
                                Ed.apply();


                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                                Animatoo.animateInAndOut(this);



                            } else {

                                Log.d("Error",response.getString("response_message"));

                                FabToast.makeText(getApplicationContext(), response.getString("response_message"), FabToast.LENGTH_LONG, FabToast.ERROR, FabToast.POSITION_DEFAULT).show();

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
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=utf-8";
                }


            };
//            reserverequestQueue.getCache().clear();

            MySingleton.getInstance(mContext).addToRequestQueue(req);

        }
    }
}
