package com.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView userName, userEmail, userClass, userAccount, verfyMsg, ver;
    private EditText editUname;
    private Button buttonLogout, buttonVerifyNow, buttonsaveChanges;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fireStore;

    private String userId, editUsername;
    private ProgressDialog progressUpdating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        userAccount      = findViewById(R.id.textView_userAccount);
        userName         = findViewById(R.id.textView_ProfileUsername);
        userEmail        = findViewById(R.id.textView_ProfileEmail);
        userClass        = findViewById(R.id.textView_ProfileClass);
        editUname        = findViewById(R.id.editText_editUsername);

        firebaseAuth     = FirebaseAuth.getInstance();
        fireStore        = FirebaseFirestore.getInstance();

        buttonsaveChanges= findViewById(R.id.button_saveChanges);
        buttonVerifyNow  = findViewById(R.id.button_verifyNow);
        verfyMsg         = findViewById(R.id.textView_VerifyMsg);
        ver              = findViewById(R.id.textView_Verify);
        progressUpdating = new ProgressDialog(this);

        buttonsaveChanges.setOnClickListener(this);
        buttonLogout     = findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(this);

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
                            Toast.makeText(v.getContext(), "Verification Email has been sent. ", Toast.LENGTH_SHORT).show();
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
                userEmail.setText("Email    : " + documentSnapshot.getString("uEmail"));
                userClass.setText("Class    : " + documentSnapshot.getString("uKelas"));
            }
        });
        getSupportActionBar().setTitle("Account Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private boolean validateUsername(String username){
        //username empty
        if(TextUtils.isEmpty(username)){
            //password empty
            editUname.setError("Username is required.");
            return false;
        }
        //username too short
        if(username.length() < 4 || username.length() > 15){
            editUname.setError("Username must be at least 4 - 15 Characters.");
            return false;
        }

        //contains whitespace
        if(username.contains(" ")){
            editUname.setError("Username cannot contain whitespaces.");
            return false;
        }

        return true;
    }

    public void updateUsername() {
        editUsername = editUname.getText().toString().trim();
        if(validateUsername(editUsername) == false) return;
        userId = firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fireStore.collection("users").document(userId);
        documentReference.update("uName", editUsername);
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(getApplicationContext(),SignInActivity.class));
    }

    @Override
    public void onClick(View view){
        if(view == buttonLogout){
            logout();
        }
        if(view == buttonsaveChanges){
            updateUsername();
            progressUpdating.setMessage("Please wait ...");
            progressUpdating.show();
            startActivity(new Intent(getApplicationContext(),AccountActivity.class));
        }
    }

}
