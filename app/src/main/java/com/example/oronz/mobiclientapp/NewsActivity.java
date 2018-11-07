package com.example.oronz.mobiclientapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.oronz.mobiclientapp.checks.CookieThumperSample;

import su.levenetc.android.textsurface.TextSurface;

import static su.levenetc.android.textsurface.animations.Just.show;


public class NewsActivity extends AppCompatActivity {

    private TextSurface textSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        textSurface = (TextSurface) findViewById(R.id.text_surface);

        textSurface.postDelayed(new Runnable() {
            @Override public void run() {
                show();
            }
        }, 1000);

        findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                show();
            }
        });

    }

    private void show() {
        textSurface.reset();
        CookieThumperSample.play(textSurface, getAssets());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
