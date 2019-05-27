package com.kfandra.tzlc12;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class adapterFixture extends ArrayAdapter<Fixture> {
    List<Fixture> fixtures;
    Context context;

    public adapterFixture(@NonNull Context context, int resource, List<Fixture> objects) {
        super(context, resource, objects);
        fixtures = objects;
        this.context= context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.fixtureitem,parent,false);
        }

        TextView fixtureName = (TextView)convertView.findViewById(R.id.fixtureDisplayFixture);
        TextView fixtureTypeGround = (TextView)convertView.findViewById(R.id.fixtureDisplayType);
        TextView fixtureResult = (TextView)convertView.findViewById(R.id.fixtureResult);
        TextView fixtureDate = (TextView)convertView.findViewById(R.id.fixtureDisplayDate);
        TextView fixtureDay = (TextView)convertView.findViewById(R.id.fixtureDisplayDay);

        Fixture fixture = fixtures.get(position);

        String[] colorNames =  context.getResources().getStringArray(R.array.jerseycolors);
        int[] colors =  context.getResources().getIntArray(R.array.androidcolors);

        int homeColor=12;
        for (homeColor = 0; homeColor < colorNames.length ; homeColor++) {
            if(colorNames[homeColor].equalsIgnoreCase(fixture.getHomeClubColor()))
                break;
        }

        int awayColor=12;
        for (awayColor = 0; awayColor < colorNames.length ; awayColor++) {
            if(colorNames[awayColor].equalsIgnoreCase(fixture.getAwayClubColor()))
                break;
        }

        //holder.fixtureName.setText("" + fixture.getHomeClub() + " vs " + fixture.getAwayClub());
        String hC = "" + "<font color='" + colors[homeColor] + "'>" + fixture.getHomeClub() + "</font>" + " vs <font color='" + colors[awayColor] + "'>" + fixture.getAwayClub() + "</font>";
        fixtureName.setText(Html.fromHtml( hC));

        if (fixture.getHomeClub().equalsIgnoreCase("Blazing Eagles") || fixture.getHomeClub().equalsIgnoreCase("Silver Sharks") || fixture.getHomeClub().equalsIgnoreCase("KITFO"))
            fixtureTypeGround.setText("" + fixture.getType() + " , Papal" );
        else
            fixtureTypeGround.setText("" + fixture.getType() + " , NCL" );
        fixtureResult.setText("" + fixture.getResult());

        fixtureDate.setText("" + String.format("%02d", (fixture.getDate()%100))+"/"+String.format("%02d", ((fixture.getDate()/100)%100))+"/"+fixture.getDate()/10000);
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
            Date dt1 = format1.parse(fixtureDate.getText().toString());
            DateFormat format2 = new SimpleDateFormat("EEEE");
            fixtureDay.setText("" + format2.format(dt1));
        }catch(Exception e)
        {
            Log.d("Fixture Adapter", "Invalid Date");
        }

        fixtureResult.setText("" + fixture.getResult());

        return convertView;
    }
}
