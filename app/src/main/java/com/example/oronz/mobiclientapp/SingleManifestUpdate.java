package com.example.oronz.mobiclientapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SingleManifestUpdate extends AppCompatActivity {
    private MobiClientApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_manifest_update);

        app = (MobiClientApplication) getApplication();



    }
}
