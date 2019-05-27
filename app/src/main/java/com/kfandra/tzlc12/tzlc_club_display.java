package com.kfandra.tzlc12;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class tzlc_club_display extends AppCompatActivity {

    private String role;
    private List<Club> clubs;
    ListView clubList;
    public int scrollIndex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tzlc_club_display);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        role = bundle.getString("role");

        clubs = new ArrayList<Club>();
        clubList = findViewById(R.id.listClub);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://tzlc12.firebaseio.com/");
        //DatabaseReference databaseReference = database.getReference("/fixtures");
        Query query = database.getReference("/clubs").orderByChild("clubName");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Club club = dataSnapshot.getValue(Club.class);
                club.setId(dataSnapshot.getKey());
                clubs.add(club);
                try {
                    adapterClub adapterClub = new adapterClub(tzlc_club_display.this, R.layout.clubitem, clubs);
                    clubList.setAdapter(adapterClub);
                }catch (Exception e)
                {
                    Toast.makeText(tzlc_club_display.this,e.toString(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
