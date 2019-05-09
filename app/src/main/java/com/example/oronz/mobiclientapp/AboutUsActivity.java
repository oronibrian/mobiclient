package com.example.oronz.mobiclientapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.Objects;

public class AboutUsActivity extends AppCompatActivity {
    ImageView mobilogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        mobilogo = findViewById(R.id.mobilogo);

        Bitmap bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.mobiticket_receipt_logo);
        mobilogo.setImageBitmap(bImage);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

}
