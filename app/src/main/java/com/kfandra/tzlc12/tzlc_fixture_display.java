package com.kfandra.tzlc12;

import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class tzlc_fixture_display extends AppCompatActivity {
    private String role;
    private List<Fixture> fixtures;
    //private FirebaseDatabase database;
    //private DatabaseReference databaseReference;
    ListView fixtureList;
    public int scrollIndex=0;
    private int updatePosition=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tzlc_fixture_display);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Calendar calendar = Calendar.getInstance();
        final int currentdate = (calendar.get(Calendar.YEAR)*100 +(calendar.get(Calendar.MONTH)+1))*100+calendar.get(Calendar.DAY_OF_MONTH);
        Bundle bundle = getIntent().getExtras();
        role = bundle.getString("role");

        fixtures = new ArrayList<Fixture>();
        fixtureList = findViewById(R.id.listFixture);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://tzlc12.firebaseio.com/");
        //DatabaseReference databaseReference = database.getReference("/fixtures");
        Query query = database.getReference("/fixtures").orderByChild("date");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Fixture fixture = dataSnapshot.getValue(Fixture.class);
                fixture.setId(dataSnapshot.getKey());
                if(fixture.getDate() < currentdate)
                    scrollIndex+=1;
                fixtures.add(fixture);

                Collections.sort(fixtures, new Comparator<Fixture>() {
                    @Override
                    public int compare(Fixture o1, Fixture o2) {
                        return  Long.compare(o1.getDate(),o2.getDate());
                    }
                });

                adapterFixture fixtureadapter = new adapterFixture(tzlc_fixture_display.this, R.layout.listitemfixture, fixtures);
                fixtureList.setAdapter(fixtureadapter);
                fixtureList.setSelectionFromTop(scrollIndex, 0);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Fixture fixture = dataSnapshot.getValue(Fixture.class);
                fixture.setId(dataSnapshot.getKey());
                if(fixture.getDate() < currentdate)
                    scrollIndex+=1;
                fixtures.set(updatePosition,fixture);
                adapterFixture fixtureadapter = new adapterFixture(tzlc_fixture_display.this, R.layout.listitemfixture, fixtures);
                fixtureList.setAdapter(fixtureadapter);
                fixtureList.setSelectionFromTop(scrollIndex, 0);
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

        fixtureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fixture fixture = fixtures.get(position);
                Intent fixtureEditIntent = new Intent(tzlc_fixture_display.this, tzlc_fixture_details.class);
                Bundle extras = new Bundle();
                extras.putString("role", role);
                extras.putString("fixtureID", fixture.getId());
                extras.putString("homeClub", fixture.getHomeClub());
                extras.putString("awayClub",fixture.getAwayClub());
                extras.putLong("date", fixture.getDate());
                extras.putString("type",fixture.getType());
                extras.putString("subtype",fixture.getSubtype());
                extras.putInt("scrollIndex", fixtureList.getFirstVisiblePosition());
                fixtureEditIntent.putExtras(extras);
                startActivityForResult(fixtureEditIntent, 100);

            }
        });

        fixtureList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Fixture fixture = fixtures.get(position);
                updatePosition = position;
                Calendar calendar    = Calendar.getInstance();
                if((calendar.get(Calendar.YEAR) * 10000 + (calendar.get(Calendar.MONTH)+1)*100 + calendar.get(Calendar.DAY_OF_MONTH)) <= fixture.getDate()) {
                    if (role.equalsIgnoreCase("KFANDRAAI")) {
                        Intent fixtureEditIntent = new Intent(tzlc_fixture_display.this, tzlc_fixture_add.class);
                        Bundle extras = new Bundle();
                        extras.putString("role", role);
                        extras.putString("fixtureID", fixture.getId());
                        extras.putString("homeClub", fixture.getHomeClub());
                        extras.putString("awayClub",fixture.getAwayClub());
                        extras.putLong("date", fixture.getDate());
                        extras.putString("type",fixture.getType());
                        extras.putString("subtype",fixture.getSubtype());
                        extras.putInt("scrollIndex", fixtureList.getFirstVisiblePosition());
                        fixtureEditIntent.putExtras(extras);
                        startActivityForResult(fixtureEditIntent, 100);

                    } else
                        Toast.makeText(tzlc_fixture_display.this, "Only KFANDRAAI is allowed to edit Fixture", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(tzlc_fixture_display.this, "Previous date's Fixture can not be edited", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        fixtureList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        FloatingActionButton fab = findViewById(R.id.fab);
        if(role.equals("KFANDRAAI"))
            fab.show();
        else
            fab.hide();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fixtureAddIntent = new Intent(tzlc_fixture_display.this, tzlc_fixture_add.class);
                Bundle extras  = new Bundle();
                extras.putString("role", role);
                extras.putString("fixtureID", "");
                extras.putInt("scrollIndex", fixtureList.getFirstVisiblePosition());
                fixtureAddIntent.putExtras(extras);
                startActivityForResult(fixtureAddIntent,100);
            }
        });

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.app_name) + " Fixtures");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(data != null)
        {
            Bundle b = data.getExtras();
            scrollIndex = b.getInt("scrollIndex", 1);
            fixtureList.setSelectionFromTop(scrollIndex, 0);
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnI = new Intent();
        Bundle extras = new Bundle();
        extras.putString("role", role);
        returnI.putExtras(extras);
        setResult(100, returnI);
        finish();
        //super.onBackPressed();
    }
}
