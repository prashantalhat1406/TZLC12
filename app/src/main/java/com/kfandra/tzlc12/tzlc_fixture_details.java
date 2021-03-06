package com.kfandra.tzlc12;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class tzlc_fixture_details extends AppCompatActivity {

    private List<Page> fixturedetails;
    ListView fixtureDetailsList;
    private String role;
    private String  fixtureID,homeClub,awayClub;
    public int scrollIndex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tzlc_fixture_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        role = bundle.getString("role");
        fixtureID = bundle.getString("fixtureID");
        homeClub = bundle.getString("homeClub");
        awayClub = bundle.getString("awayClub");



        fixturedetails = new ArrayList<>();
        fixtureDetailsList = findViewById(R.id.listFixtureDetails);

        Page stats = new Page("Stats","View Stats of current fixture");
        Page matchofficials = new Page("Match Officials", "Manage Match Officals for fixture");
        Page loans = new Page("Loans", "Manage Loans for fixure");

        fixturedetails.add(stats);
        fixturedetails.add(matchofficials);
        fixturedetails.add(loans);

        adapterPage adapterpage = new adapterPage(tzlc_fixture_details.this, R.layout.listitempage, fixturedetails);
        fixtureDetailsList.setAdapter(adapterpage);

        fixtureDetailsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Page page = fixturedetails.get(position);
                Bundle extras  = new Bundle();
                switch (page.getPageName()){
                    case "Match Officials":
                        Intent matchofficialIntent = new Intent(tzlc_fixture_details.this, tzlc_matchofficial_display.class);
                        extras.putString("role", role);
                        extras.putString("fixtureID",fixtureID);
                        extras.putString("homeClub", homeClub);
                        extras.putString("awayClub",awayClub);
                        matchofficialIntent.putExtras(extras);
                        startActivityForResult(matchofficialIntent,100);
                        //finish();
                        break;
                    default:
                        Toast.makeText(tzlc_fixture_details.this, "Not yet implemented", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(" " + homeClub + " vs " + awayClub);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(data != null)
        {
            Bundle b = data.getExtras();
            scrollIndex = b.getInt("scrollIndex", 1);
            role = b.getString("role");
            //fixtureList.setSelectionFromTop(scrollIndex, 0);
        }
    }

    @Override
    protected void onResume() {
        Bundle bundle = getIntent().getExtras();
        role = bundle.getString("role");
        fixtureID = bundle.getString("fixtureID");
        super.onResume();
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
}
