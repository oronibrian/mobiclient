package com.example.oronz.mobiclientapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;

public class ExpansionViewActivity extends AppCompatActivity {

    private static final String LOG_TAG = ExpansionViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expansion_view);
        init();
    }

    private void init() {
        SingleSelectToggleGroup single = findViewById(R.id.group_choices);
        single.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                Log.v(LOG_TAG, "onCheckedChanged(): checkedId = " + checkedId);
            }
        });
    }

}
