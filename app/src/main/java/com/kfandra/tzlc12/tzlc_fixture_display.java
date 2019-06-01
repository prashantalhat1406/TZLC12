package com.kfandra.tzlc12;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class tzlc_fixture_display extends AppCompatActivity {
    private String role;
    private List<Fixture> fixtures;
    //private FirebaseDatabase database;
    //private DatabaseReference databaseReference;
    ListView fixtureList;
    public int scrollIndex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tzlc_fixture_display);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                fixtures.add(fixture);
                adapterFixture fixtureadapter = new adapterFixture(tzlc_fixture_display.this, R.layout.listitemfixture, fixtures);
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

        fixtureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(tzlc_fixture_display.this,"Single Click", Toast.LENGTH_SHORT).show();
                /*for(int a = 0; a < parent.getChildCount(); a++)
                {
                    parent.getChildAt(a).setBackgroundColor(Color.TRANSPARENT);
                }
                view.setBackgroundColor(Color.GREEN);*/
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

                Calendar calendar    = Calendar.getInstance();
                if((calendar.get(Calendar.YEAR) * 10000 + (calendar.get(Calendar.MONTH)+1)*100 + calendar.get(Calendar.DAY_OF_MONTH)) < fixture.getDate()) {
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

                return false;
            }
        });

        fixtureList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        /*Query recent = databaseReference.limitToFirst(10);
        FirebaseListOptions<Fixture> options = new FirebaseListOptions.Builder<Fixture>()
                .setQuery(recent,Fixture.class)
                .setLayout(R.layout.listitemfixture)
                .build();*/



        /*FirebaseListAdapter<Fixture> fixtureadapter = new FirebaseListAdapter<Fixture>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Fixture fixture, int position) {
                ((TextView)v.findViewById(R.id.fixtureDisplayHome)).setText("" + fixture.getHomeClub());
                ((TextView)v.findViewById(R.id.fixtureDisplayAway)).setText("" + fixture.getAwayClub());
            }
        };*/

        invalidateOptionsMenu();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fixtures,menu);

        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(!role.equalsIgnoreCase("KFANDRAAI")){
            menu.findItem(R.id.addFixture).setVisible(false);
            menu.findItem(R.id.editFixture).setVisible(false);
            menu.findItem(R.id.deleteFixture).setVisible(false);
        }else{
            menu.findItem(R.id.addFixture).setVisible(true);
            menu.findItem(R.id.editFixture).setVisible(true);
            menu.findItem(R.id.deleteFixture).setVisible(true);
        }
        return true;
        //return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.addFixture :
                Intent fixtureAddIntent = new Intent(tzlc_fixture_display.this, tzlc_fixture_add.class);
                Bundle extras  = new Bundle();
                extras.putString("role", role);
                extras.putString("fixtureID", "");
                extras.putInt("scrollIndex", fixtureList.getFirstVisiblePosition());
                fixtureAddIntent.putExtras(extras);
                startActivityForResult(fixtureAddIntent,100);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
