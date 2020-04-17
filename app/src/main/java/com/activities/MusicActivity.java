package com.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

import components.other.InvalidReservationDialog;
import components.reservations.MusicReservation;
import components.reservations.RoomReservation;

public class MusicActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button datePickButton;
    private Button reserveButton;

    private CheckBox keyboardCheckBox;
    private CheckBox guitarCheckBox;
    private CheckBox drumBoxCheckBox;
    private CheckBox bassCheckBox;

    private boolean keyboardIsReserved = false;
    private boolean guitarIsReserved = false;
    private boolean drumBoxIsReserved= false;
    private boolean bassIsReserved = false;
    private String reservationDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        getSupportActionBar().setTitle("Musical Instrument Reservation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        datePickButton = (Button)findViewById(R.id.button_date);
        datePickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new components.other.DatePicker();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        keyboardCheckBox = (CheckBox)findViewById(R.id.checkBox_keyboard);
        keyboardCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyboardCheckBox.isChecked()){
                    keyboardIsReserved = true;
                } else {
                    keyboardIsReserved = false;
                }
            }
        });

        guitarCheckBox = (CheckBox)findViewById(R.id.checkBox_guitar);
        guitarCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (guitarCheckBox.isChecked()){
                    guitarIsReserved = true;
                } else {
                    guitarIsReserved = false;
                }
            }
        });

        drumBoxCheckBox = (CheckBox)findViewById(R.id.checkBox_guitar);
        drumBoxCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drumBoxCheckBox.isChecked()){
                    drumBoxIsReserved = true;
                } else {
                    drumBoxIsReserved = false;
                }
            }
        });

        bassCheckBox = (CheckBox)findViewById(R.id.checkBox_guitar);
        bassCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bassCheckBox.isChecked()){
                    bassIsReserved = true;
                } else {
                    bassIsReserved = false;
                }
            }
        });

        reserveButton = (CheckBox)findViewById(R.id.checkBox_guitar);
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reserve();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        reservationDate = currDate;

        TextView textView = (TextView) findViewById(R.id.textView_dateChosen);
        textView.setText(reservationDate);
    }

    private void showErrorDialog(){
        InvalidReservationDialog newDialog = new InvalidReservationDialog();
        newDialog.show(getSupportFragmentManager(), "Dialog");
    }

    private boolean availabiltyCheck(MusicReservation newReservation){


        return false;
    }

    private void reserve(){
        MusicReservation newReservation = new MusicReservation();

        if (availabiltyCheck(newReservation) == false){
            showErrorDialog();
            return;
        }
    }
}
