package com.kfandra.tzlc12;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class fixtureAdapter  extends ArrayAdapter<Fixture> {
    List<Fixture> fixtures;
    Context context;

    public fixtureAdapter(@NonNull Context context, int resource, List<Fixture> objects) {
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

        Fixture fixture = fixtures.get(position);

        ((TextView)convertView.findViewById(R.id.fixtureDisplayHome)).setText("" + fixture.getHomeClub());
        ((TextView)convertView.findViewById(R.id.fixtureDisplayAway)).setText("" + fixture.getAwayClub());

        return convertView;
        //return super.getView(position, convertView, parent);
    }
}
