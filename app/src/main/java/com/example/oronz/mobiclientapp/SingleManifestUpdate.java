package com.example.oronz.mobiclientapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Objects;

public class SingleManifestUpdate extends AppCompatActivity {
    private MobiClientApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_manifest_update);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        app = (MobiClientApplication) getApplication();
        getSupportActionBar().setTitle(app.getManfestSelected()+" Manifest");



    }
}
