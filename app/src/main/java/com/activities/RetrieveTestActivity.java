package com.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import components.reservations.RoomReservation;

public class RetrieveTestActivity extends AppCompatActivity{

    private FirebaseFirestore db;
    private CollectionReference reference;

    private Button retrieveButton;

    private RoomReservation holder;

    private TextView txtDate, txtUsername, txtRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_test);

        db = FirebaseFirestore.getInstance();
        reference = db.collection("RoomReservation");

//        retrieveButton = (Button) findViewById(R.id.btn_retrieve);
//        retrieveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                retrieveData();
//            }
//        });
//
//        txtDate = (TextView) findViewById(R.id.txt_date);
//        txtRoom = (TextView) findViewById(R.id.txt_room);
//        txtUsername = (TextView) findViewById(R.id.txt_username);
    }

    public void retrieveData(){
        txtUsername.setText("PRESSED");
        reference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                         String data = "";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                             holder = documentSnapshot.toObject(RoomReservation.class);
//                             txtRoom.setText(holder.getRoomNo());
//                             txtUsername.setText(holder.getUser());
//                             txtDate.setText(holder.getDate());
                         }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

}
