package com.kfandra.tzlc12;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class adapterPlayer extends ArrayAdapter<Player> {

    List<Player> players;
    Context context;

    public adapterPlayer(@NonNull Context context, int resource, List<Player> objects) {
        super(context, resource, objects);
        players = objects;
        this.context= context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.listitemplayer,parent,false);
        }

        Player player = players.get(position);

        TextView playerName = convertView.findViewById(R.id.playerName);
        TextView playerValue = convertView.findViewById(R.id.playerValue);
        TextView playerClub = convertView.findViewById(R.id.playerClub);

        playerName.setText("" + player.getPlayerName());
        playerValue.setText("" + player.getCurrentValue());
        playerClub.setText("" + player.getClubName());

        //convertView.setBackgroundColor(Color.parseColor("#E1F5FE"));
        convertView.setBackgroundColor(context.getResources().getColor(R.color.listColorPrimary));

        return convertView;
    }
}
