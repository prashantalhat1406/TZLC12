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
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Fixture fixture = fixtures.get(position);
        holder.fixtureName.setText("" + fixture.getHomeClub() + " vs " + fixture.getAwayClub());

        return convertView;
    }
}
