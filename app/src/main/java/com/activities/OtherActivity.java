package com.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import static android.view.View.*;

public class OtherActivity extends AppCompatActivity implements OnClickListener {

    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        buttonLogout  = findViewById(R.id.button_logout);

        buttonLogout.setOnClickListener(this);

    }

    public void logout(){
        FirebaseAuth.getInstance().signOut(); //logout
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    @Override
    public void onClick(View view){
        if(view == buttonLogout){
            logout();
        }
    }

}
