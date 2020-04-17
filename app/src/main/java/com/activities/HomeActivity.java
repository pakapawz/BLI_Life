package com.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    private Button shuttleButton;
    private Button fastingButton;
    private Button otherButton;
    private Button accountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle("Home");

//        if(isAppActive == false) finish();

        shuttleButton = (Button) findViewById(R.id.button_shuttle);
        shuttleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openShuttleActivity();
            }
        });

        fastingButton = (Button) findViewById(R.id.button_fasting);
        fastingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openFastingActivity();
            }
        });

        otherButton = (Button) findViewById(R.id.button_other);
        otherButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openOtherActivity();
            }
        });

        accountButton = (Button) findViewById(R.id.button_account);
        accountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openAccountActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //disables back button
    }


    public void openShuttleActivity(){
        Intent intent = new Intent(this, ShuttleActivity.class);
        startActivity(intent);
    }

    public void openFastingActivity(){
        Intent intent = new Intent(this, FastingActivity.class);
        startActivity(intent);
    }

    public void openOtherActivity(){
        Intent intent = new Intent(this, OtherActivity.class);
        startActivity(intent);
    }

    public void openAccountActivity(){
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }
}
