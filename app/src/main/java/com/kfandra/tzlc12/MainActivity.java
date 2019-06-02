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
import android.widget.Toast;

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

        Page clubs = new Page("Clubs","Clubs in TZLC12");
        Page players = new Page("Players", "Players in TZLC12");
        Page fixtures = new Page("Fixtures", "Fixtures for TZLC12");
        Page balancesheet = new Page("Balance Sheet", "Manage Balance Sheet for Club");

        pages.add(clubs);
        pages.add(fixtures);
        pages.add(players);
        pages.add(balancesheet);

        adapterPage adapterPage = new adapterPage(MainActivity.this, R.layout.listitempage, pages);
        pageList.setAdapter(adapterPage);

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
                    case "Balance Sheet":
                        Toast.makeText(MainActivity.this, "Only KFANDRAAI and Managers have access to this page", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "Default", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });




        //prashant.alhat@kfandra.com  : Test123$
        //kfandraai@kfandra.com
        //MRManager@kfandra.com



        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //signed in
                    if (user.getEmail().equalsIgnoreCase("kfandraai@kfandra.com") ){
                        role = "KFANDRAAI";
                    }else if(user.getEmail().contains("manager")){
                        role = "MANAGER";
                    }else {
                        role = "PLAYER";
                    }
                    getSupportActionBar().setTitle("TZLC-12   Welcome " + user.getDisplayName());

                }else{
                    //signed out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build()
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
