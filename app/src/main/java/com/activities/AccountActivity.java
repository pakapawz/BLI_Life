package com.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView userName, userEmail, userClass, userAccount, verfyMsg, ver;
    private Button buttonLogout, buttonVerifyNow;

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

        firebaseAuth     = FirebaseAuth.getInstance();
        fireStore        = FirebaseFirestore.getInstance();

        buttonVerifyNow  = findViewById(R.id.button_verifyNow);
        verfyMsg         = findViewById(R.id.textView_VerifyMsg);
        ver              = findViewById(R.id.textView_Verify);


        userId           = firebaseAuth.getCurrentUser().getUid();

        //to check email verified or not
        FirebaseUser userVer = firebaseAuth.getCurrentUser();
        if(!userVer.isEmailVerified()){
            //Verify email
            verfyMsg.setVisibility(View.VISIBLE);
            buttonVerifyNow.setVisibility(View.VISIBLE);

            buttonVerifyNow.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(final View v){
                    FirebaseUser userVer = firebaseAuth.getCurrentUser();
                    userVer.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Verification Email has been sent.", Toast.LENGTH_SHORT).show();
                            logout();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String TAG="";
                            Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                        }
                    });
                }
            });
        }else{
            ver.setVisibility(View.VISIBLE);
        }

        //get user information
        DocumentReference documentReference = fireStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                userAccount.setText("Your Account");
                userName.setText(documentSnapshot.getString("uName"));
                userEmail.setText(documentSnapshot.getString("uEmail"));
                userClass.setText(documentSnapshot.getString("uKelas"));
            }
        });

        buttonLogout  = findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(this);

        getSupportActionBar().setTitle("Account Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),SignInActivity.class));
        finish();
    }

    @Override
    public void onClick(View view){
        if(view == buttonLogout){
            logout();
        }
    }

}
