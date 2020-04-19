package com.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import components.reservations.RoomReservation;

public class RetrieveTestActivity extends AppCompatActivity{

    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;

    private Button getButton;

    private RoomReservation holder;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_test);

        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("RoomReservation");

        getButton = (Button) findViewById(R.id.btn_get);
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveData();
            }
        });

        textView = (TextView) findViewById(R.id.textView);
    }

    public void retrieveData(){
        collectionReference
                .whereEqualTo("date", "26/04/2020")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                         String data = "";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            holder = documentSnapshot.toObject(RoomReservation.class);
                            data += holder.getUser() + " " + holder.getRoomNo() + " " + holder.getDate() + "\n";
                            textView.setText(data);
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
