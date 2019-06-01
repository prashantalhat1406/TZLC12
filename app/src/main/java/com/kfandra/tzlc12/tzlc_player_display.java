package com.kfandra.tzlc12;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class tzlc_player_display extends AppCompatActivity {

    private String role;
    private List<Player> players;
    ListView playerList;
    public int scrollIndex=0;
    Query query;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tzlc_player_display);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        role = bundle.getString("role");

        players = new ArrayList<Player>();
        playerList = findViewById(R.id.listPlayer);

        database = FirebaseDatabase.getInstance("https://tzlc12.firebaseio.com/");
        query = database.getReference("/players").orderByChild("playerName");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Player player = dataSnapshot.getValue(Player.class);
                player.setId(dataSnapshot.getKey());
                players.add(player);
                adapterPlayer adapterPlayer = new adapterPlayer(tzlc_player_display.this, R.layout.listitemplayer, players);
                playerList.setAdapter(adapterPlayer);
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
        getSupportActionBar().setTitle(getString(R.string.app_name) + " Players");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_players,menu);

        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        /*if(!role.equalsIgnoreCase("KFANDRAAI")){
            menu.findItem(R.id.addFixture).setVisible(false);
            menu.findItem(R.id.editFixture).setVisible(false);
            menu.findItem(R.id.deleteFixture).setVisible(false);
        }else{
            menu.findItem(R.id.addFixture).setVisible(true);
            menu.findItem(R.id.editFixture).setVisible(true);
            menu.findItem(R.id.deleteFixture).setVisible(true);
        }
        return true;*/
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        adapterPlayer adapterPlayer;
        switch (item.getItemId()){
            case R.id.sortClub :
                Collections.sort(players, new Comparator<Player>() {
                    @Override
                    public int compare(Player o1, Player o2) {
                        return o1.getClubName().compareToIgnoreCase(o2.getClubName());
                    }
                });
                adapterPlayer = new adapterPlayer(tzlc_player_display.this, R.layout.listitemplayer, players);
                playerList.setAdapter(adapterPlayer);
                break;
            case R.id.sortPlayer :

                if (item.getTitle().charAt(item.getTitle().length()-1) == '^' )
                {
                    item.setTitle("Sort by Player v");
                    Collections.sort(players, new Comparator<Player>() {
                        @Override
                        public int compare(Player o1, Player o2) {
                            return o1.getPlayerName().compareToIgnoreCase(o2.getPlayerName());
                        }
                    });
                }
                else
                {
                    item.setTitle("Sort by Player ^");
                    Collections.sort(players, new Comparator<Player>() {
                        @Override
                        public int compare(Player o1, Player o2) {
                            return o2.getPlayerName().compareToIgnoreCase(o1.getPlayerName());
                        }
                    });
                }
                adapterPlayer = new adapterPlayer(tzlc_player_display.this, R.layout.listitemplayer, players);
                playerList.setAdapter(adapterPlayer);
                break;
            case R.id.sortValue :
                query = database.getReference("/players").orderByChild("currentValue");
                Collections.sort(players, new Comparator<Player>() {
                    @Override
                    public int compare(Player o1, Player o2) {
                        return (o1.getCurrentValue() > o2.getCurrentValue() ? -1 : o1.getCurrentValue() == o2.getCurrentValue() ? 0 : 1);
                    }
                });
                adapterPlayer = new adapterPlayer(tzlc_player_display.this, R.layout.listitemplayer, players);
                playerList.setAdapter(adapterPlayer);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
