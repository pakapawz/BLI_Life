package com.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.internal.bind.ArrayTypeAdapter;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText editUsername, editEmail, editPassword, editRepassword;
    private Button btnSignup;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore fireStore;
    private String classChosen;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editUsername     = findViewById(R.id.editText_username);

        Spinner classSpinner        = findViewById(R.id.spinner_class);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.classList, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);
        classSpinner.setOnItemSelectedListener(this);

        editEmail        = findViewById(R.id.editText_email);
        editPassword     = findViewById(R.id.editText_password);
        editRepassword   = findViewById(R.id.editText_repassword);
        btnSignup        = findViewById(R.id.button_signUp);
        textViewSignin   = findViewById(R.id.textView_signIn);

        progressDialog   = new ProgressDialog(this);
        btnSignup.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);

        firebaseAuth     = FirebaseAuth.getInstance();
        fireStore        = FirebaseFirestore.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();
        }

        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private boolean validateUsername(String username){
        //username empty
        if(TextUtils.isEmpty(username)){
            //password empty
            editUsername.setError("Username is required.");
            return false;
        }
        //username too short
        if(username.length() < 4){
            editUsername.setError("Username must be at least 6 Characters.");
            return false;
        }

        //contains whitespace
        if(username.contains(" ")){
            editUsername.setError("Username cannot contain whitespaces.");
            return false;
        }

        return true;
    }

    private boolean validateEmailFormat(String email){
        //empty email
        if(TextUtils.isEmpty(email)){
            editEmail.setError("Email is required.");
            return false;
        }

        //does not contain @ and .
        if (!email.contains("@") && !email.contains(".")) {
            editEmail.setError("Invalid email format.");
            return false;
        }

        //invalid format
        if (email.contains("@.") || email.contains(".@")){
            return false;
        }

        return true;

    }

    private boolean validatePassword(String password){
        if(TextUtils.isEmpty(password)){
            //password empty
            editPassword.setError("Password is required.");
            return false;
        }
        //password too short
        if(password.length() <= 6){
            editPassword.setError("Password must be more than 6 Characters.");
            return false;
        }

        return true;
    }

    private boolean validateRetypePassword(String password, String rePassword){
        if(password.equals(rePassword) == false){
            editRepassword.setError("Retype password must be same with password.");
            return false;
        }

        return true;
    }

    private void registerUser(){
        final String username   = editUsername.getText().toString().trim();
        final String email      = editEmail.getText().toString().trim();
        final String kelas      = classChosen.toString().trim();
        final String password   = editPassword.getText().toString().trim();
        final String rePassword = editRepassword.getText().toString().trim();

        if (validateUsername(username) == false ||
            validateEmailFormat(email) == false ||
            validatePassword(password) == false ||
            validateRetypePassword(password, rePassword) == false) return;

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    //send verification link
                    FirebaseUser userVer = firebaseAuth.getCurrentUser();
                    userVer.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(SignUpActivity.this, "Verification Email has been sent.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String TAG="";
                            Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                        }
                    });

                    Toast.makeText(SignUpActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                    //store data in firebase
                    userID = firebaseAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fireStore.collection("users").document(userID);
                    Map<String,Object> user = new HashMap<>();
                    user.put("uName",username);
                    user.put("uEmail",email);
                    user.put("uKelas",kelas);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            String TAG = "";
                            Log.d(TAG, "onSuccess: User Profile is created for " + userID);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String TAG = "";
                            Log.d(TAG, "onFailure: " + e.toString());
                        }
                    });
                    //move to HomeActivity
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                }else{
                    Toast.makeText(SignUpActivity.this, "Could not register! Please try again" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onClick(View view){
        if(view == btnSignup){
            registerUser();
        }

        if(view == textViewSignin){
            //open signIn activity
            startActivity(new Intent(getApplicationContext(),SignInActivity.class));
            finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String classSelected = parent.getItemAtPosition(position).toString();
        classChosen = classSelected;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
