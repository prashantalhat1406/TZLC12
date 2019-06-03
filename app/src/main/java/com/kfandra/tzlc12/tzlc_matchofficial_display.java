package com.kfandra.tzlc12;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class tzlc_matchofficial_display extends AppCompatActivity {


    private String role,homeClub,awayClub;
    private List<MatchOfficial> matchOfficials;
    ListView matchofficialList;
    public int scrollIndex=0;
    Query query;
    FirebaseDatabase database;
    private String  fixtureID;
    private int updatePosition=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tzlc_matchofficial_display);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        role = bundle.getString("role");
        fixtureID = bundle.getString("fixtureID");
        homeClub = bundle.getString("homeClub");
        awayClub = bundle.getString("awayClub");



        matchOfficials = new ArrayList<>();
        matchofficialList = findViewById(R.id.listMatchOfficials);

        database = FirebaseDatabase.getInstance("https://tzlc12.firebaseio.com/");
        query = database.getReference("/matchOfficials/"+fixtureID).orderByChild("role");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MatchOfficial matchOfficial = dataSnapshot.getValue(MatchOfficial.class);
                matchOfficial.setId(dataSnapshot.getKey());
                matchOfficials.add(matchOfficial);
                adapterMatchOfficial adapterMatchOfficial = new adapterMatchOfficial(tzlc_matchofficial_display.this, R.layout.listitemmatchofficial, matchOfficials);
                matchofficialList.setAdapter(adapterMatchOfficial);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MatchOfficial matchOfficial = dataSnapshot.getValue(MatchOfficial.class);
                matchOfficial.setId(dataSnapshot.getKey());
                matchOfficials.set(updatePosition,matchOfficial);
                adapterMatchOfficial adapterMatchOfficial = new adapterMatchOfficial(tzlc_matchofficial_display.this, R.layout.listitemmatchofficial, matchOfficials);
                matchofficialList.setAdapter(adapterMatchOfficial);
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

        matchofficialList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(role.equals("KFANDRAAI")) {
                    MatchOfficial matchOfficial = matchOfficials.get(position);
                    updatePosition=position;
                    Intent MOEditIntent = new Intent(tzlc_matchofficial_display.this, tzlc_matchofficial_add.class);
                    Bundle extras = new Bundle();
                    extras.putString("role", role);
                    extras.putString("fixtureID", fixtureID);
                    extras.putString("moID", matchOfficial.getId());
                    extras.putString("awayClub", awayClub);
                    extras.putString("homeClub", homeClub);
                    extras.putString("moClub", matchOfficial.getClubName());
                    extras.putString("moPlayer", matchOfficial.getPlayerName());
                    extras.putString("moDuty", matchOfficial.getRole());
                    extras.putInt("scrollIndex", matchofficialList.getFirstVisiblePosition());
                    extras.putInt("updatePosition", updatePosition);
                    MOEditIntent.putExtras(extras);
                    startActivityForResult(MOEditIntent, 100);
                }else{
                    Toast.makeText(tzlc_matchofficial_display.this,"Only KFANDRAAI can edit MO", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        if(role.equals("PLAYER"))
            fab.hide();
            //fab.setVisibility(View.GONE);
        else
            fab.show();
            //fab.setVisibility(View.VISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fixtureEditIntent = new Intent(tzlc_matchofficial_display.this, tzlc_matchofficial_add.class);
                Bundle extras = new Bundle();
                extras.putString("role", role);
                extras.putString("fixtureID", fixtureID);
                extras.putString("homeClub", homeClub);
                extras.putString("awayClub",awayClub);
                extras.putString("moID", "");
                extras.putString("moClub", "");
                extras.putString("moPlayer", "");
                extras.putString("moDuty", "");
                fixtureEditIntent.putExtras(extras);
                startActivityForResult(fixtureEditIntent, 100);
            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Match Officials for " + homeClub + " vs " + awayClub);
    }

    @Override
    public void onBackPressed() {
        Intent returnI = new Intent();
        Bundle extras = new Bundle();
        extras.putString("role", role);
        extras.putString("fixtureID", fixtureID);
        returnI.putExtras(extras);
        setResult(100, returnI);
        finish();
        //super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(data != null)
        {
            Bundle b = data.getExtras();
            role = b.getString("role");
            fixtureID = b.getString("fixtureID");
            scrollIndex = b.getInt("scrollIndex", 1);
            //updatePosition = b.getInt("updatePosition",-1);
            matchofficialList.setSelectionFromTop(scrollIndex, 0);
        }
    }
}
