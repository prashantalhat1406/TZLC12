package com.kfandra.tzlc12;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 1;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();

        //prashant.alhat@kfandra.com  : Test123$
        //kfandraai@kfandra.com
        //MRManager@kfandra.com
        final Button clubs = findViewById(R.id.butClubs);
        clubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fixtureIntent = new Intent(MainActivity.this, tzlc_club_display.class);
                Bundle extras  = new Bundle();
                extras.putString("role", role);
                fixtureIntent.putExtras(extras);
                startActivity(fixtureIntent);
            }
        });

        final Button players = findViewById(R.id.butPlayer);
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


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //signed in
                    if (user.getEmail().equalsIgnoreCase("kfandraai@kfandra.com") ){
                        clubs.setVisibility(View.VISIBLE);
                        players.setVisibility(View.VISIBLE);
                        fixtures.setVisibility(View.VISIBLE);
                        balancesheet.setVisibility(View.VISIBLE);
                        role = "KFANDRAAI";
                    }else if(user.getEmail().contains("manager")){
                        clubs.setVisibility(View.VISIBLE);
                        players.setVisibility(View.VISIBLE);
                        fixtures.setVisibility(View.VISIBLE);
                        balancesheet.setVisibility(View.VISIBLE);
                        role = "MANAGER";

                    }else {
                        clubs.setVisibility(View.VISIBLE);
                        players.setVisibility(View.VISIBLE);
                        fixtures.setVisibility(View.VISIBLE);
                        balancesheet.setVisibility(View.GONE);
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
