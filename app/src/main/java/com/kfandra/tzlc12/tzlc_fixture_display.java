package com.kfandra.tzlc12;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class tzlc_fixture_display extends AppCompatActivity {
    private String role;
    private List<Fixture> fixtures;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tzlc_fixture_display);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        role = bundle.getString("role");

        fixtures = new ArrayList<Fixture>();

        final ListView fixtureList = findViewById(R.id.listFixture);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://tzlc12.firebaseio.com/");
        DatabaseReference databaseReference = database.getReference("/fixtures");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Fixture fixture = dataSnapshot.getValue(Fixture.class);
                fixtures.add(fixture);
                fixtureAdapter fixtureadapter = new fixtureAdapter(tzlc_fixture_display.this, R.layout.fixtureitem, fixtures);
                fixtureList.setAdapter(fixtureadapter);
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

        /*Query recent = databaseReference.limitToFirst(10);
        FirebaseListOptions<Fixture> options = new FirebaseListOptions.Builder<Fixture>()
                .setQuery(recent,Fixture.class)
                .setLayout(R.layout.fixtureitem)
                .build();*/



        /*FirebaseListAdapter<Fixture> fixtureadapter = new FirebaseListAdapter<Fixture>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Fixture fixture, int position) {
                ((TextView)v.findViewById(R.id.fixtureDisplayHome)).setText("" + fixture.getHomeClub());
                ((TextView)v.findViewById(R.id.fixtureDisplayAway)).setText("" + fixture.getAwayClub());
            }
        };*/



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }




}
