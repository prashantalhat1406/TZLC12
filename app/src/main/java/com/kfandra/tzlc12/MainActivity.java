package com.kfandra.tzlc12;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 1;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String role;
    private List<Page> pages;
    ListView pageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();

        pages = new ArrayList<Page>();
        pageList = findViewById(R.id.listPage);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://tzlc12.firebaseio.com/");
        //DatabaseReference databaseReference = database.getReference("/fixtures");
        Query query = database.getReference("/pages").orderByChild("pageName");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Page page = dataSnapshot.getValue(Page.class);
                page.setId(dataSnapshot.getKey());
                pages.add(page);
                adapterPage adapterPage = new adapterPage(MainActivity.this, R.layout.listitempage, pages);
                pageList.setAdapter(adapterPage);
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

        pageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Page page = pages.get(position);
                Bundle extras  = new Bundle();
                switch (page.getPageName()){
                    case "Clubs":
                        Intent clubIntent = new Intent(MainActivity.this, tzlc_club_display.class);
                        extras.putString("role", role);
                        clubIntent.putExtras(extras);
                        startActivity(clubIntent);
                        break;
                    case "Players":
                        Intent playerIntent = new Intent(MainActivity.this, tzlc_player_display.class);
                        extras.putString("role", role);
                        playerIntent.putExtras(extras);
                        startActivity(playerIntent);
                        break;
                    case "Fixtures":
                        Intent fixtureIntent = new Intent(MainActivity.this, tzlc_fixture_display.class);
                        extras.putString("role", role);
                        fixtureIntent.putExtras(extras);
                        startActivity(fixtureIntent);
                        break;
                }
            }
        });

        //query.addChildEventListener(new ChildEventListener() {}


        //prashant.alhat@kfandra.com  : Test123$
        //kfandraai@kfandra.com
        //MRManager@kfandra.com
        /*final Button clubs = findViewById(R.id.butClubs);
        clubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clubIntent = new Intent(MainActivity.this, tzlc_club_display.class);
                Bundle extras  = new Bundle();
                extras.putString("role", role);
                clubIntent.putExtras(extras);
                startActivity(clubIntent);
            }
        });

        final Button players = findViewById(R.id.butPlayer);
        players.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playerIntent = new Intent(MainActivity.this, tzlc_player_display.class);
                Bundle extras  = new Bundle();
                extras.putString("role", role);
                playerIntent.putExtras(extras);
                startActivity(playerIntent);
            }
        });


        final Button fixtures = findViewById(R.id.butFixtures);
        fixtures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fixtureIntent = new Intent(MainActivity.this, tzlc_fixture_display.class);
                Bundle extras  = new Bundle();
                extras.putString("role", role);
                fixtureIntent.putExtras(extras);
                startActivity(fixtureIntent);
            }
        });
        final Button balancesheet = findViewById(R.id.butBalanceSheet);
        */


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //signed in
                    if (user.getEmail().equalsIgnoreCase("kfandraai@kfandra.com") ){
                        /*clubs.setVisibility(View.VISIBLE);
                        players.setVisibility(View.VISIBLE);
                        fixtures.setVisibility(View.VISIBLE);
                        balancesheet.setVisibility(View.VISIBLE);*/
                        role = "KFANDRAAI";
                    }else if(user.getEmail().contains("manager")){
                        /*clubs.setVisibility(View.VISIBLE);
                        players.setVisibility(View.VISIBLE);
                        fixtures.setVisibility(View.VISIBLE);
                        balancesheet.setVisibility(View.VISIBLE);*/
                        role = "MANAGER";

                    }else {
                        /*clubs.setVisibility(View.VISIBLE);
                        players.setVisibility(View.VISIBLE);
                        fixtures.setVisibility(View.VISIBLE);
                        balancesheet.setVisibility(View.GONE);*/
                        role = "PLAYER";
                    }
                    TextView welcometxt = findViewById(R.id.txtwelcomeBar);
                    welcometxt.setText("Welcome " + user.getDisplayName());
                    //Toast.makeText(MainActivity.this, user.getDisplayName() + " Signed  IN", Toast.LENGTH_LONG).show();
                }else{
                    //signed out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()
                                    ))
                                    .build(),RC_SIGN_IN
                    );
                }

            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.signout :
                AuthUI.getInstance().signOut(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
