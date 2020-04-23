package com.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import components.reservations.interfaces.Idatabase;
import components.reservations.interfaces.Ivalidations;
import components.reservations.Reservation;
import components.reservations.RoomReservation;

public class RoomActivity
        extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener,
            Ivalidations, Idatabase {

    private Button datePickButton;
    private Button reserveButton;
    private TextView showHistory;

    private Spinner roomSpinner;
    private String name = "NAME";
    private String email = "EMAIL@DOMAIN.COM";
    private String date = "(choose date)";
    private String roomNo = "(choose room)";

    private ProgressDialog writeProgress;

    private FirebaseFirestore db;
    private CollectionReference roomReservationReference;
    private FirebaseAuth firebaseAuth;
    private CollectionReference usersReference;

    private String userId;
    private boolean isAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        getSupportActionBar().setTitle("Room Reservation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setting up database
        db = FirebaseFirestore.getInstance();
        roomReservationReference = db.collection("RoomReservation");
        usersReference = db.collection("users");

        userId = firebaseAuth.getInstance().getCurrentUser().getUid();

        //setting up room spinner
        roomSpinner = findViewById(R.id.spinner_room);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roomList, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomSpinner.setAdapter(adapter);
        roomSpinner.setOnItemSelectedListener(this);

        //setting up progress dialog
        writeProgress = new ProgressDialog(this);

        //setting up buttons
        datePickButton = (Button)findViewById(R.id.button_date);
        datePickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new components.other.DatePicker();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        reserveButton = (Button)findViewById(R.id.button_reserve);
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void goToRetrieve(){
        Intent intent = new Intent(this, roomHistoryActivity.class);
        startActivity(intent);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String roomSelected = parent.getItemAtPosition(position).toString();
        roomNo = roomSelected;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
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
    public boolean validateReservation(Reservation reservation) {
        if (roomNo.equals("(choose room)")){
            Toast.makeText(RoomActivity.this, "Choose room!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (date.equals("(choose date)")){
            Toast.makeText(RoomActivity.this, "Choose date!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (reservation.dateChecker() == false) {
            Toast.makeText(RoomActivity.this, "Reservation date must be greater than current date!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    public boolean checkAvailability(Reservation reservation) {
        isAvailable = true;
        roomReservationReference
                .whereEqualTo("date", reservation.getDate())
                .whereEqualTo("roomNo", roomNo)
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
    public void showErrorDialog(){
        InvalidReservationDialog newDialog = new InvalidReservationDialog();
        newDialog.show(getSupportFragmentManager(), "Dialog");
    }

    @Override
    public void reserve(){
        final CustomProgressDialog progressDialog = new CustomProgressDialog(RoomActivity.this);

        RoomReservation reservation = new RoomReservation(name, email, date, roomNo);
        if (validateReservation(reservation) == false) return;

        setNameAndEmail();

        progressDialog.startDialog();
        Handler handler = new Handler();
        boolean b = handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.stopDialog();
            }
        }, 2000);

        if (checkAvailability(reservation) == false) {
            showErrorDialog();
        } else {
            writeToDatabase(reservation);
        }
    }


    @Override
    public void writeToDatabase(Reservation reservation) {

        roomReservationReference.document().set(reservation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RoomActivity.this,
                                "Room " + roomNo + " is now reserved on " + date + " under the name " + name,
                                Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RoomActivity.this,
                                "Uh oh, something's wrong. Please try again later.",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
