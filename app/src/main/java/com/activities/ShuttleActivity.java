package com.activities;

import android.app.DatePickerDialog;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

    private FirebaseFirestore db;
    private CollectionReference ref;

    private CheckBox toBLICheckBox;
    private CheckBox fromBLICheckBox;

    private boolean toBLI = false;
    private boolean fromBLI = false;

    private String date = "(choose date)";


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
        ref = db.collection("ShuttleReservation");

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
    public void writeToDatabase(Reservation reservation) {
        ref.document().set(reservation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ShuttleActivity.this,
                                "Shuttle is now reserved on " + date + " under the name " + "NAME",
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

    @Override
    public void reserve() {
        final CustomProgressDialog progressDialog = new CustomProgressDialog(ShuttleActivity.this);

        ShuttleReservation reservation = new ShuttleReservation("USER", "TEST@domain.com", date,
                toBLI, fromBLI);

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
        return true;
    }

    @Override
    public void showErrorDialog() {
        InvalidReservationDialog newDialog = new InvalidReservationDialog();
        newDialog.show(getSupportFragmentManager(), "Dialog");
    }
}
