package com.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

import components.reservations.RoomReservation;

public class roomHistoryActivity extends AppCompatActivity {

    private TextView historyTxt;

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private CollectionReference roomReservationReference;
    private CollectionReference usersReference;

    private String userId;
    private String email;
    private RoomReservation holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_history);

        historyTxt = findViewById(R.id.txt_history);

        //setting up database
        db = FirebaseFirestore.getInstance();
        roomReservationReference = db.collection("RoomReservation");
        usersReference = db.collection("users");

    }

    @Override
    protected void onStart() {
        super.onStart();
        setEmail();
        historyTxt.setText(email);
        showHistory();
    }

    private void setEmail(){
        userId = firebaseAuth.getInstance().getCurrentUser().getUid();
        usersReference
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        email = documentSnapshot.getString("email");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        historyTxt.setText("Something's wrong. Please try again later.");
                    }
                });
    }
    
    private void showHistory(){
        roomReservationReference
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String temp = "";
                        int no = 1;

                        for (QueryDocumentSnapshot q: queryDocumentSnapshots) {
                            temp += "Reservation #" + no;

                            holder = q.toObject(RoomReservation.class);

                            temp += "\nCreated on : " + holder.getCreationDate();
                            temp += "\nRoom No.   : " + holder.getRoomNo();
                            temp += "\nFor date   : " + holder.getDate();
                            temp += "\n";

                            no++;
                            historyTxt.setText(temp);
                        }
                    }
                });
    }
}
