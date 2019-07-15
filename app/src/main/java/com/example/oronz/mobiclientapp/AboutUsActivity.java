package com.example.oronz.mobiclientapp;

import android.content.Intent;
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



    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
        this.finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

}
