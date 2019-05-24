package com.kfandra.tzlc12;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    static class ViewHolder{
        private TextView fixtureDate;
        private TextView fixtureDay;
        private TextView fixtureName;
        private TextView fixtureTypeGround;
        private TextView fixtureResult;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.fixtureitem,parent,false);
            holder = new ViewHolder();
            holder.fixtureName = (TextView)convertView.findViewById(R.id.fixtureDisplayFixture);
            holder.fixtureTypeGround = (TextView)convertView.findViewById(R.id.fixtureDisplayType);
            holder.fixtureResult = (TextView)convertView.findViewById(R.id.fixtureResult);
            holder.fixtureDate = (TextView)convertView.findViewById(R.id.fixtureDisplayDate);
            holder.fixtureDay = (TextView)convertView.findViewById(R.id.fixtureDisplayDay);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Fixture fixture = fixtures.get(position);

        holder.fixtureName.setText("" + fixture.getHomeClub() + " vs " + fixture.getAwayClub());

        if (fixture.getHomeClub().equalsIgnoreCase("Blazing Eagles") || fixture.getHomeClub().equalsIgnoreCase("Silver Sharks") || fixture.getHomeClub().equalsIgnoreCase("KITFO"))
            holder.fixtureTypeGround.setText("" + fixture.getType() + " , Papal" );
        else
            holder.fixtureTypeGround.setText("" + fixture.getType() + " , NCL" );
        holder.fixtureResult.setText("" + fixture.getResult());

        holder.fixtureDate.setText("" + String.format("%02d", (fixture.getDate()%100))+"/"+String.format("%02d", ((fixture.getDate()/100)%100))+"/"+fixture.getDate()/10000);
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
            Date dt1 = format1.parse(holder.fixtureDate.getText().toString());
            DateFormat format2 = new SimpleDateFormat("EEEE");
            holder.fixtureDay.setText("" + format2.format(dt1));
        }catch(Exception e)
        {
            Log.d("Fixture Adapter", "Invalid Date");
        }

        holder.fixtureResult.setText("" + fixture.getResult());

        return convertView;
    }
}
