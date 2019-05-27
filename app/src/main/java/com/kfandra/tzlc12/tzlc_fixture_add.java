package com.kfandra.tzlc12;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class tzlc_fixture_add extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public int scrollIndex;
    private List<Club> clubs;
    private List<String> clubNames;
    Spinner type,subtype,homeClubName,awayClubName;
    public EditText datepicker;
    private long d;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tzlc_fixture_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        homeClubName = findViewById(R.id.spnHomeClub);
        awayClubName = findViewById(R.id.spnAwayClub);

        clubs = new ArrayList<Club>();
        clubNames = new ArrayList<String>();

        Bundle b = getIntent().getExtras();
        final String fixtureID = b.getString("fixtureID");
        scrollIndex = b.getInt("scrollIndex",-1);

        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://tzlc12.firebaseio.com/");
        DatabaseReference databaseReference = database.getReference("clubs");
        //Query getAllClubs = databaseReference.child("/clubs/").orderByChild("clubName");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Club club = dataSnapshot.getValue(Club.class);
                club.setId(dataSnapshot.getKey());
                clubs.add(club);
                clubNames.add(club.getClubName());
                ArrayAdapter<String> clubAdapter = new ArrayAdapter<String>(tzlc_fixture_add.this,R.layout.layout_dropdown_item,clubNames);
                clubAdapter.setDropDownViewResource(R.layout.layout_dropdown_item);
                homeClubName.setAdapter(clubAdapter);
                awayClubName.setAdapter(clubAdapter);
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

        type = findViewById(R.id.spnType);
        ArrayAdapter<CharSequence> typeadapter = ArrayAdapter.createFromResource(this,R.array.fixtureType,R.layout.layout_dropdown_item);
        typeadapter.setDropDownViewResource(R.layout.layout_dropdown_item);
        type.setAdapter(typeadapter);

        subtype = findViewById(R.id.spnSubType);
        ArrayAdapter<CharSequence> subtypeadapter = ArrayAdapter.createFromResource(this,R.array.fixtureSubType,R.layout.layout_dropdown_item);
        subtypeadapter.setDropDownViewResource(R.layout.layout_dropdown_item);
        subtype.setAdapter(subtypeadapter);

        //final Calendar calendar = Calendar.getInstance();
        datepicker = (EditText) findViewById(R.id.edtFixtureDate);
        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog =  new DatePickerDialog(tzlc_fixture_add.this,tzlc_fixture_add.this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fixture fixture = new Fixture();
                fixture.setHomeClub(homeClubName.getSelectedItem().toString());
                fixture.setAwayClub(awayClubName.getSelectedItem().toString());
                fixture.setType(type.getSelectedItem().toString());
                fixture.setSubtype(subtype.getSelectedItem().toString());
                fixture.setDate(d);
                fixture.setResult("Yet to be played");

                if(fixture.getHomeClub().equalsIgnoreCase(fixture.getAwayClub()))
                    Toast.makeText(tzlc_fixture_add.this,"Error !! Home and Away Club can not be same",Toast.LENGTH_SHORT).show();
                else {
                    if (fixtureID.length() != 0) {
                        DatabaseReference databaseReference = database.getReference("fixtures/" + fixtureID);
                        databaseReference.setValue(fixture);

                    } else {
                        DatabaseReference databaseReference = database.getReference("fixtures");
                        databaseReference.push().setValue(fixture);
                    }

                    Intent returnI = new Intent();
                    Bundle extras = new Bundle();
                    extras.putInt("scrollIndex", scrollIndex);
                    returnI.putExtras(extras);
                    setResult(100, returnI);
                    finish();
                }
            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        datepicker.setText(""+String.format("%02d",dayOfMonth )+"/"+String.format("%02d",   (month+1))+"/"+year);
        d=year*100+(month+1);
        d=d*100+dayOfMonth;

    }

}