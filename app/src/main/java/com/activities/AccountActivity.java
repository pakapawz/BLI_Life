package com.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView userName, userEmail, userClass, userAccount;
    private Button buttonLogout;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fireStore;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        userAccount      = findViewById(R.id.textView_userAccount);
        userName         = findViewById(R.id.textView_ProfileUsername);
        userEmail        = findViewById(R.id.textView_ProfileEmail);
        userClass        = findViewById(R.id.textView_ProfileClass);

        firebaseAuth  = FirebaseAuth.getInstance();
        fireStore     = FirebaseFirestore.getInstance();

        userId        = firebaseAuth.getCurrentUser().getUid();

        //get user information
        DocumentReference documentReference = fireStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                userAccount.setText("Account");
                userName.setText(documentSnapshot.getString("uName"));
                userEmail.setText(documentSnapshot.getString("uEmail"));
                userClass.setText("Class");
            }
        });

        buttonLogout  = findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(this);

        getSupportActionBar().setTitle("Account Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view){
        if(view == buttonLogout){
            logout();
        }
    }

}
