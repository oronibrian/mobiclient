package com.example.oronz.mobiclientapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.nbbse.printapi.Printer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class ReceiptActivity extends AppCompatActivity {
    TextView txt_name, txt_status;
    Button btnnew, btncomplete,btnprint;
    MobiClientApplication app;
    List<String> myList;
    EditText walletpassword,wallet_username, mpesanumber,agency_username, Agencywaletpassword;

    private ProgressDialog mProgress;
    private ImageView checkView;
    private ImageView crossView;
    AlertDialog mpesaalertDialog,jpAgencyalertDialog,jpwalletalertDialog;
    public static PrinterInterface printInterfaceService;


    String resp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        app = (MobiClientApplication) getApplication();

        txt_name = findViewById(R.id.txt_name);
        txt_status = findViewById(R.id.txt_status);
        btnnew = findViewById(R.id.btnnew);
        btncomplete = findViewById(R.id.btncomplete);
        btnprint= findViewById(R.id.btnprint);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing payment ...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);


        btnnew.setVisibility(View.GONE);
        btnprint.setVisibility(View.GONE);



        String value = getIntent().getStringExtra("data");
        String status = getIntent().getStringExtra("txt_status");



        if (status.equals("Failed")) {
            btncomplete.setVisibility(View.GONE);
            btnprint.setVisibility(View.VISIBLE);

            txt_name.setText(value);

            txt_status.setText(status);
            txt_status.setTextColor(Color.RED);



        } else {
            btncomplete.setVisibility(View.VISIBLE);
            txt_name.setText(value);

            txt_status.setText(status);
            txt_status.setTextColor(Color.GREEN);

        }

        btncomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceed();
                txt_status.setVisibility(View.GONE);



            }
        });

        btnprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printTicket();
            }
        });
        btnnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


    }

    private void printTicket() {
        if (Build.MODEL.equals("MobiPrint")){
            Toast.makeText(getApplicationContext(),"Mobiwire Printing Ticket",Toast.LENGTH_LONG).show();

            Printer print = Printer.getInstance();
            print.printBitmap(getResources().openRawResource(R.raw.mobiticket_receipt_logo));

            print.printText("-------------Testing-------------");
            print.printText("..............Details................");
            print.printFormattedText();
            print.printText("Searved by :"+ app.getLogged_user());

            print.printBitmap(getResources().openRawResource(R.raw.powered_by_mobiticket));
            print.printFormattedTextPrepare();

            print.printEndLine();

        }

    }

    private void proceed() {

        if (String.valueOf(app.getPayment_type()).equals("3")) {
            MpesaDialog();

        } else if (String.valueOf(app.getPayment_type()).equals("2")) {
            JPWalletDialog();

        } else if (String.valueOf(app.getPayment_type()).equals("1")) {
            JPAgencyWalletDialog();

        }

    }

    public void MpesaDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.mpesa_layout, null);
        dialogBuilder.setView(dialogView);

        mpesanumber = dialogView.findViewById(R.id.mpesanumber);
        Button btncomplete = dialogView.findViewById(R.id.mpsabtn);

        btncomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpesaPayment();
            }
        });

         mpesaalertDialog = dialogBuilder.create();
        mpesaalertDialog.show();
    }


    public void JPWalletDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.wallet_confirmation_input_dialog, null);
        dialogBuilder.setView(dialogView);

        wallet_username = dialogView.findViewById(R.id.wallet_username);

        wallet_username.setText(app.getAgency_phone());

        walletpassword = dialogView.findViewById(R.id.waletpassword);
        Button btncomplete = dialogView.findViewById(R.id.btnconfirm);

        btncomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jamboPayWalet();
            }
        });

         jpwalletalertDialog = dialogBuilder.create();
        jpwalletalertDialog.show();
    }


    public void JPAgencyWalletDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.agency_wallet, null);
        dialogBuilder.setView(dialogView);
        agency_username = dialogView.findViewById(R.id.agency_username);

        agency_username.setText(app.getAgency_phone());

        Agencywaletpassword = dialogView.findViewById(R.id.Agencywaletpassword);
        Button btncomplete = dialogView.findViewById(R.id.btnconfirm);

        btncomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jamboPayAgencyWalet();
            }
        });

         jpAgencyalertDialog = dialogBuilder.create();
        jpAgencyalertDialog.show();
    }

    private void mpesaPayment() {


        mProgress.show();

        RequestQueue mpesareserverequestQueue = Volley.newRequestQueue(ReceiptActivity.this);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "AuthorizePayment");
        params.put("payment_method", "3");

        params.put("reference_number", app.getRefno());
        params.put("mpesa_phone_number", mpesanumber.getText().toString());

        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getInt("response_code") == 0) {

                                JSONArray jsonArray = response.getJSONArray("tickets");

                                mProgress.dismiss();
                                mpesaalertDialog.dismiss();

                                String message = response.getString("response_message");

                                Log.d("Mpesa Respose",message);
                                txt_name.setText(message);

                                btncomplete.setVisibility(View.GONE);
                                btnnew.setVisibility(View.VISIBLE);
                                btnprint.setVisibility(View.VISIBLE);


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String reserve_confirmation = jsonObject1.getString("description");

                                    Log.d("Reservation Status: ", reserve_confirmation);
                                    Log.d("Reserve:%n %s", jsonArray.toString(4));


                                }


                            } else {
                                Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();
                                mpesaalertDialog.dismiss();


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("Response Error: "+e);
                            mProgress.dismiss();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mpesaalertDialog.dismiss();

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
        mpesareserverequestQueue.getCache().clear();

        mpesareserverequestQueue.add(req);

    }

    private void jamboPayWalet() {


        //Commond here......"p/IK4:"

        mProgress.show();

        RequestQueue jpreserverequestQueue = Volley.newRequestQueue(ReceiptActivity.this);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "AuthorizePayment");
        params.put("payment_method", "2");
        params.put("reference_number", app.getRefno());
        params.put("jambopay_wallet_username", wallet_username.getText().toString());
        params.put("jambopay_wallet_password", walletpassword.getText().toString());


        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String code = response.getString("response_code");
                            Log.d("CODE Respose",code);

                            if (response.getInt("response_code") == 0) {

                                JSONArray jsonArray = response.getJSONArray("tickets");

                                mProgress.dismiss();
                                jpwalletalertDialog.dismiss();

                                String message = response.getString("response_message");

                                Log.d("Agency Respose",message);
                                txt_name.setText(message);

                                btncomplete.setVisibility(View.GONE);
                                btnnew.setVisibility(View.VISIBLE);
                                btnprint.setVisibility(View.VISIBLE);


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String reserver = jsonObject1.getString("trx_status");

                                    Log.d("Reservation Status: ", reserver);
                                    Log.d("Reserve:%n %s", jsonArray.toString(4));

                                }

                            } else {
                                Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();
                                jpwalletalertDialog.dismiss();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                jpwalletalertDialog.dismiss();
                mProgress.dismiss();

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
        jpreserverequestQueue.getCache().clear();

        jpreserverequestQueue.add(req);


    }


    private void jamboPayAgencyWalet() {
        mProgress.show();

        RequestQueue reserverequestQueue = Volley.newRequestQueue(ReceiptActivity.this);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", app.getUser_name());
        params.put("api_key", app.getApi_key());
        params.put("action", "AuthorizePayment");
        params.put("payment_method", "1");
        params.put("reference_number", app.getRefno());
        params.put("jambopay_agency_username",agency_username.getText().toString());
        params.put("jambopay_agency_password",Agencywaletpassword.getText().toString());


        JsonObjectRequest req = new JsonObjectRequest(URLs.URL, new JSONObject(params),
                response -> {
                    try {
                        String code = response.getString("response_code");

                        if (code.equals("0")) {

                            JSONArray jsonArray = response.getJSONArray("tickets");

                            String message = response.getString("response_message");
                            jpAgencyalertDialog.dismiss();

                            Log.d("Agency Response",message);
                            txt_name.setText(message);

                            btncomplete.setVisibility(View.GONE);
                            btnnew.setVisibility(View.VISIBLE);
                            btnprint.setVisibility(View.VISIBLE);


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                Log.d("Reservation Status: ", message);
                                Log.d("Reserve:%n %s", jsonObject1.toString(4));

                            }

                            mProgress.dismiss();

                        } else {
                            Toast.makeText(getApplicationContext(), response.getString("response_message"), Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();
                            jpAgencyalertDialog.dismiss();

                            txt_name.setText(response.getString("response_message"));

                            btncomplete.setVisibility(View.GONE);
                            btnnew.setVisibility(View.VISIBLE);
                            btnprint.setVisibility(View.VISIBLE);

                        }


                    } catch (JSONException e) {
                        mProgress.dismiss();

                        e.printStackTrace();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                jpAgencyalertDialog.dismiss();
                mProgress.dismiss();

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
        reserverequestQueue.getCache().clear();

        reserverequestQueue.add(req);

    }

}
