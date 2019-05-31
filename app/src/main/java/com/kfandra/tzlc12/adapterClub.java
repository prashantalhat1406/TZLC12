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

public class adapterClub extends ArrayAdapter<Club> {

    List<Club> clubs;
    Context context;

    public adapterClub(@NonNull Context context, int resource, List<Club> objects) {
        super(context, resource, objects);
        clubs = objects;
        this.context= context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.listitemclub,parent,false);
        }

        Club club = clubs.get(position);

        String[] colorNames =  context.getResources().getStringArray(R.array.jerseycolors);
        int[] colors =  context.getResources().getIntArray(R.array.androidcolors);

        int color=12;
        for (color = 0; color < colorNames.length ; color++) {
            if(colorNames[color].equalsIgnoreCase(club.getClubColor()))
                break;
        }

        TextView clubName = convertView.findViewById(R.id.clubName);
        TextView clubManager = convertView.findViewById(R.id.clubManager);
        TextView clubGround = convertView.findViewById(R.id.clubGround);

        clubName.setText("" + club.getClubName() + "  ( " + club.getClubShortName() + " )");
        clubName.setTextColor(colors[color]);
        if(club.getManager2Name().length() != 0)
            clubManager.setText("" + club.getManagerName() + " , " + club.getManager2Name());
        else
            clubManager.setText("" + club.getManagerName());
        clubGround.setText("" + club.getHomeGround());

        return convertView;
    }
}
