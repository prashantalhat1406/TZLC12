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

    static class ViewHolder{
        private TextView clubName;
        private TextView clubManager;
        private TextView clubGround;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.clubitem,parent,false);
            holder = new ViewHolder();
            holder.clubName = (TextView)convertView.findViewById(R.id.clubName);
            holder.clubManager = (TextView)convertView.findViewById(R.id.clubManager);
            holder.clubGround = (TextView)convertView.findViewById(R.id.clubGround);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Club club = clubs.get(position);

        String[] colorNames =  context.getResources().getStringArray(R.array.jerseycolors);
        int[] colors =  context.getResources().getIntArray(R.array.androidcolors);

        int color=12;
        for (color = 0; color < colorNames.length ; color++) {
            if(colorNames[color].equalsIgnoreCase(club.getClubColor()))
                break;
        }

        holder.clubName.setText("" + club.getClubName() + "  ( " + club.getClubShortName() + " )");
        holder.clubName.setTextColor(colors[color]);
        holder.clubManager.setText("" + club.getManagerName() + " , " + club.getManager2Name());
        holder.clubGround.setText("" + club.getHomeGround());

        return convertView;
    }
}
