package com.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

import components.InvalidReservationDialog;

public class RoomActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private Button datePickButton;
    private Button reserveButton;

    private String dateChosen;
    private String roomChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        getSupportActionBar().setTitle("Room Reservation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner roomSpinner = findViewById(R.id.spinner_room);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roomList, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomSpinner.setAdapter(adapter);
        roomSpinner.setOnItemSelectedListener(this);

        datePickButton = (Button)findViewById(R.id.button_date);
        datePickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new components.DatePicker();
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
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String roomSelected = parent.getItemAtPosition(position).toString();

        roomChosen = roomSelected;

        Toast.makeText(parent.getContext(), roomChosen, Toast.LENGTH_SHORT).show();
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

        String currDate = DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime());

        dateChosen = currDate;

        TextView textView = (TextView) findViewById(R.id.textView_dateChosen);
        textView.setText(dateChosen);
    }

    @Override
    public void onBackPressed() {
        //disables back button
    }

    public void showErrorDialog(){
        InvalidReservationDialog newDialog = new InvalidReservationDialog();
        newDialog.show(getSupportFragmentManager(), "Dialog");
    }

    public void reserve(){
        //process then back to parent activity
        boolean isValidReservation = true;
    }
}
