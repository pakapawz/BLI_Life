package com.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ShuttleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuttle);

        getSupportActionBar().setTitle("Shuttle Reservation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        //disables back button
    }
}
