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

import components.reservations.CourtReservation;
import components.reservations.MusicReservation;
import components.reservations.RoomReservation;

public class MusicHistoryActivity extends AppCompatActivity {

    private TextView historyTxt;

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private CollectionReference musicReservationReference;
    private CollectionReference usersReference;

    private String userId;
    private String name = "NAME";
    private String email = "EMAIL@DOMAIN.COM";
    private MusicReservation holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_history);

        historyTxt = findViewById(R.id.txt_history);

        getSupportActionBar().setTitle("History (Music Reservation)");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setting up database
        db = FirebaseFirestore.getInstance();
        musicReservationReference = db.collection("MusicReservation");
        usersReference = db.collection("users");
        userId = firebaseAuth.getInstance().getCurrentUser().getUid();

        getHistory();
    }

    private void getHistory() {
        setNameAndEmail();
        showHistory();
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

    private void showHistory(){
        musicReservationReference
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String temp = "====================" +
                                "\nReservation history for " + name + " with email " + email +
                                "\n====================\n";
                        int no = 1;

                        for (QueryDocumentSnapshot q: queryDocumentSnapshots) {
                            temp += "\nReservation #" + no;

                            holder = q.toObject(MusicReservation.class);

                            temp += "\nCreated on : " + holder.getCreationDate();
                            temp += "\nFor date   : " + holder.getDate();
                            temp += "\nInstruments: ";

                            if (holder.isKeyboard()) temp += "keyboard, ";
                            if (holder.isBass()) temp += "bass, ";
                            if (holder.isGuitar()) temp += "guitar, ";
                            if (holder.isDrumBox()) temp += "drum box, ";
                            temp += "\n";
                            no++;
                        }

                        historyTxt.setText(temp);
//                        historyTxt.setText("COMPLETE");
                    }
                });
    }
}