package com.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import static android.view.View.*;

public class OtherActivity extends AppCompatActivity implements OnClickListener {

    private Button roomButton;
    private Button courtButton;
    private Button musicButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        getSupportActionBar().setTitle("Other Reservation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        roomButton = (Button)findViewById(R.id.button_room);
        roomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRoomActivity();
            }
        });

        courtButton = (Button)findViewById(R.id.button_court);
        courtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCourtActivity();
            }
        });

        musicButton = (Button)findViewById(R.id.button_music);
        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMusicActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //disables back button
    }

    public void openRoomActivity(){
        Intent intent = new Intent(this, RoomActivity.class);
        startActivity(intent);
    }

    public void openCourtActivity(){
        Intent intent = new Intent(this, CourtActivity.class);
        startActivity(intent);
    }

    public void openMusicActivity(){
        Intent intent = new Intent(this, MusicActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}
