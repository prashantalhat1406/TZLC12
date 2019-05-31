package com.kfandra.tzlc12;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

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
                adapterClub adapterClub = new adapterClub(tzlc_club_display.this, R.layout.listitemclub, clubs);
                clubList.setAdapter(adapterClub);
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.app_name) + " Clubs");
    }

}
