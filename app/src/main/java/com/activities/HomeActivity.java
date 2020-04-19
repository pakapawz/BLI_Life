package com.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.annotation.Nullable;

import com.google.firebase.firestore.model.DatabaseId;

public class HomeActivity extends AppCompatActivity {
    private Button shuttleButton;
    private Button fastingButton;
    private Button otherButton;
    private Button accountButton;

    private TextView greetingMsg;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fireStore;
    private  String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle("Home");

        firebaseAuth     = FirebaseAuth.getInstance();
        fireStore        = FirebaseFirestore.getInstance();
        userId           = firebaseAuth.getCurrentUser().getUid();
        greetingMsg      = findViewById(R.id.textView_greeting1);

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

        DocumentReference documentReference = fireStore.collection("users").document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                greetingMsg.setText("Welcome, " + documentSnapshot.getString("uName"));
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
