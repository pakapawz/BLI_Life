package com.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        getSupportActionBar().setTitle("Account Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        //disables back button
    }
}
