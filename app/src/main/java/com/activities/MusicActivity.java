package com.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class MusicActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button datePickButton;
    private Button reserveButton;

    private CheckBox keyboardCheckBox;
    private CheckBox guitarCheckBox;
    private CheckBox drumBoxCheckBox;
    private CheckBox bassCheckBox;

    boolean keyboardIsReserved = false;
    boolean guitarIsReserved = false;
    boolean drumBoxIsReserved= false;
    boolean bassIsReserved = false;

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
                DialogFragment datePicker = new components.DatePicker();
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
        TextView textView = (TextView) findViewById(R.id.textView_dateChosen);
        textView.setText(currDate);
    }

    public void reserve(){

    }
}
