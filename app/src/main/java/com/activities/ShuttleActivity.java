package com.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import components.other.CustomProgressDialog;
import components.other.InvalidReservationDialog;
import components.reservations.Reservation;
import components.reservations.ShuttleReservation;
import components.reservations.interfaces.Idatabase;
import components.reservations.interfaces.Ivalidations;

public class ShuttleActivity
        extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener,
        Idatabase, Ivalidations{

    private Button datePickButton;
    private Button reserverButton;
    private TextView showHistory;

    private FirebaseFirestore db;
    private CollectionReference shuttleReservsationReference;
    private FirebaseAuth firebaseAuth;
    private CollectionReference usersReference;

    private String userId;
    private String name = "NAME";
    private String email = "EMAIL@DOMAIN.COM";

    private CheckBox toBLICheckBox;
    private CheckBox fromBLICheckBox;

    private boolean toBLI = false;
    private boolean fromBLI = false;

    private String date = "(choose date)";
    private boolean isAvailable;

    private void goToRetrieve(){
        Intent intent = new Intent(this, ShuttleHistoryActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuttle);

        getSupportActionBar().setTitle("Shuttle Reservation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        datePickButton = (Button)findViewById(R.id.button_date);
        datePickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new components.other.DatePicker();
                datePicker.show(getSupportFragmentManager(), "date Picker");
            }
        });

        db = FirebaseFirestore.getInstance();
        shuttleReservsationReference = db.collection("ShuttleReservation");
        usersReference = db.collection("users");

        userId = firebaseAuth.getInstance().getCurrentUser().getUid();

        fromBLICheckBox = (CheckBox)findViewById(R.id.checkBox_fromBLI);
        fromBLICheckBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(fromBLICheckBox.isChecked()){
                    fromBLI = true;
                } else{
                    fromBLI = false;
                }
            }
        });

        toBLICheckBox = (CheckBox)findViewById(R.id.checkBox_toBLI);
        toBLICheckBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(toBLICheckBox.isChecked()){
                    toBLI = true;
                } else{
                    toBLI = false;
                }
            }
        });

        reserverButton = (Button)findViewById(R.id.button_reserve);
        reserverButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                reserve();
            }
        });

        showHistory = (TextView) findViewById(R.id.txt_history);
        showHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRetrieve();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currDate = dateFormat.format(c.getTime());

        date = currDate;

        TextView textView = (TextView) findViewById(R.id.textView_dateChosen);
        textView.setText(date);
    }

    @Override
    public void writeToDatabase(final Reservation reservation) {
        shuttleReservsationReference.document().set(reservation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ShuttleActivity.this,
                                "Shuttle is now reserved on " + date + " under the name " + name,
                                Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShuttleActivity.this,
                                "Uh oh, something's wrong. Please try again later.",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setNameAndEmail(){
        usersReference.document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        name = documentSnapshot.getString("uName");
                        email = documentSnapshot.getString("uEmail");
                    }
                });
    }

    @Override
    public void reserve() {
        final CustomProgressDialog progressDialog = new CustomProgressDialog(ShuttleActivity.this);

        setNameAndEmail();
        ShuttleReservation reservation = new ShuttleReservation(name, email, date, toBLI, fromBLI);

        if(validateReservation(reservation) == false) return;

        progressDialog.startDialog();
        Handler handler = new Handler();
        boolean b = handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.stopDialog();
            }
        }, 2000);

        if(checkAvailability(reservation) == false){
            showErrorDialog();
            return;
        }

        writeToDatabase(reservation);
    }

    @Override
    public boolean validateReservation(Reservation reservation) {
        if(date.equals("(choose date)")){
            Toast.makeText(ShuttleActivity.this, "Choose date!", Toast.LENGTH_LONG).show();
            return false;
        }
        if(reservation.dateChecker() == false){
            Toast.makeText(ShuttleActivity.this, "Reservation date must be greater than current date!",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if(toBLI == false && fromBLI == false){
            Toast.makeText(ShuttleActivity.this, "At least one checkbox must be selected!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean checkAvailability(Reservation reservation) {
        isAvailable = true;
        shuttleReservsationReference
                .whereEqualTo("date", reservation.getDate())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        //if this loop runs, there exists a reservation for the same date
                        for (QueryDocumentSnapshot queryDocumentSnapshot: queryDocumentSnapshots){
                            isAvailable = false;
                            if (isAvailable == false) break;
                        }
                    }
                });

        return isAvailable;
    }

    @Override
    public void showErrorDialog() {
        InvalidReservationDialog newDialog = new InvalidReservationDialog();
        newDialog.show(getSupportFragmentManager(), "Dialog");
    }
}
