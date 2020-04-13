package com.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editUsername, editEmail, editPassword, editRepassword;
    private Button btnSignup;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editUsername     = findViewById(R.id.editText_username);
        editEmail    = findViewById(R.id.editText_email);
        editPassword = findViewById(R.id.editText_password);
        editRepassword = findViewById(R.id.editText_repassword);
        btnSignup  = findViewById(R.id.button_signUp);
        textViewSignin = findViewById(R.id.textView_signIn);

        progressDialog = new ProgressDialog(this);
        btnSignup.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();
        }

        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void registerUser(){
        String email     = editEmail.getText().toString().trim();
        String password  = editPassword.getText().toString().trim();
        String rePassword= editRepassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            editEmail.setError("Email is required.");
            return;
        }

        if(TextUtils.isEmpty(password)){
            editPassword.setError("Password is required.");
            return;
        }

        if(password.length() <= 6){
            editPassword.setError("Password must be more than 6 Characters.");
            return;
        }

        if(password.equals(rePassword) != true){
            editRepassword.setError("invalid password!");
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //registered successfully
                    Toast.makeText(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                }else {
                    Toast.makeText(SignUpActivity.this, "Could not register. please try again", Toast.LENGTH_SHORT).show();
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
            //open signin activity
            startActivity(new Intent(getApplicationContext(),SignInActivity.class));
            finish();
        }
    }


}
