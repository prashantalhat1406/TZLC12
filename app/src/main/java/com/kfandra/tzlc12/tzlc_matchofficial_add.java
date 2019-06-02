package com.kfandra.tzlc12;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class tzlc_matchofficial_add extends AppCompatActivity {

    private String  fixtureID,homeClub,awayClub,moClub,moPlayer,moDuty,moID;
    private String role;
    private List<Club> clubs;
    private List<String> clubNames;
    private List<Player> players;
    Spinner clubSpinner,playerSpinner,dutySpinner;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tzlc_matchofficial_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        role = bundle.getString("role");
        fixtureID = bundle.getString("fixtureID");
        homeClub = bundle.getString("homeClub");
        awayClub = bundle.getString("awayClub");
        moClub = bundle.getString("moClub");
        moPlayer = bundle.getString("moPlayer");
        moDuty = bundle.getString("moDuty");
        moID = bundle.getString("moID");


        clubSpinner = findViewById(R.id.spnMOAddClub);
        playerSpinner = findViewById(R.id.spnMOAddPlayer);


        clubNames = new ArrayList<String>();
        players = new ArrayList<Player>();

        database = FirebaseDatabase.getInstance("https://tzlc12.firebaseio.com/");
        DatabaseReference databaseReference = database.getReference("clubs");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Club club = dataSnapshot.getValue(Club.class);
                //club.setId(dataSnapshot.getKey());
                //clubs.add(club);
                if(club.getOrganization()  == 0)
                    if(!club.getClubName().equals(homeClub) && !club.getClubName().equals(awayClub))
                        clubNames.add(club.getClubName());
                ArrayAdapter<String> clubAdapter = new ArrayAdapter<String>(tzlc_matchofficial_add.this,R.layout.layout_dropdown_item,clubNames);
                clubAdapter.setDropDownViewResource(R.layout.layout_dropdown_item);
                clubSpinner.setAdapter(clubAdapter);
                if(moID.length() !=0)
                    clubSpinner.setSelection(clubAdapter.getPosition(moClub));

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

        DatabaseReference playersData = database.getReference("players");
        playersData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Player player = dataSnapshot.getValue(Player.class);
                player.setId(dataSnapshot.getKey());
                players.add(player);
                //adapterPlayer adapterPlayer = new adapterPlayer(tzlc_matchofficial_add.this, R.layout.layout_dropdown_item, players);
                //ArrayAdapter<String> playerAdapter = new ArrayAdapter<String>(tzlc_matchofficial_add.this,R.layout.layout_dropdown_item,players);
                //playerAdapter.setDropDownViewResource(R.layout.layout_dropdown_item);
                //playerSpinner.setAdapter(playerAdapter);
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

        clubSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                List<String> playersClub = new ArrayList<String>();
                for (Player player : players) {
                    if(clubNames.get(position).equals(player.getClubName()))
                        playersClub.add(player.getPlayerName());
                }
                ArrayAdapter<String> playerAdapter = new ArrayAdapter<String>(tzlc_matchofficial_add.this,R.layout.layout_dropdown_item,playersClub);
                playerAdapter.setDropDownViewResource(R.layout.layout_dropdown_item);
                playerSpinner.setAdapter(playerAdapter);
                if(moID.length() !=0)
                    playerSpinner.setSelection(playerAdapter.getPosition(moPlayer));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dutySpinner = findViewById(R.id.spnMOAddDuty);
        ArrayAdapter<CharSequence> modutyadapter = ArrayAdapter.createFromResource(this,R.array.matchOfficialDuty,R.layout.layout_dropdown_item);
        modutyadapter.setDropDownViewResource(R.layout.layout_dropdown_item);
        dutySpinner.setAdapter(modutyadapter);

        if(moID.length() != 0)
        {
            dutySpinner.setSelection(modutyadapter.getPosition(moDuty));
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MatchOfficial matchOfficial = new MatchOfficial();
                matchOfficial.setClubName(clubSpinner.getSelectedItem().toString());
                matchOfficial.setPlayeName(playerSpinner.getSelectedItem().toString());
                matchOfficial.setRole(dutySpinner.getSelectedItem().toString());

                //DatabaseReference databaseReference = database.getReference("matchOfficials/" + fixtureID);
                //databaseReference.push().setValue(matchOfficial);

                if (moID.length() != 0) {
                    DatabaseReference databaseReference = database.getReference("matchOfficials/" + fixtureID+"/"+moID);
                    databaseReference.setValue(matchOfficial);

                } else {
                    DatabaseReference databaseReference = database.getReference("matchOfficials/" + fixtureID);
                    databaseReference.push().setValue(matchOfficial);
                }

                Intent returnI = new Intent();
                Bundle extras = new Bundle();
                extras.putString("role", role);
                extras.putString("fixtureID", fixtureID);
                returnI.putExtras(extras);
                setResult(100, returnI);
                finish();
            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
