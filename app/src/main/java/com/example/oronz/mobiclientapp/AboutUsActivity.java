package com.example.oronz.mobiclientapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class AboutUsActivity extends AppCompatActivity {
    ImageView mobilogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        mobilogo = findViewById(R.id.mobilogo);

        Bitmap bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.bus);
        mobilogo.setImageBitmap(bImage);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
