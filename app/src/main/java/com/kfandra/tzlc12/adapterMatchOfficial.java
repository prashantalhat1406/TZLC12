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

public class adapterMatchOfficial extends ArrayAdapter<MatchOfficial> {

    List<MatchOfficial> matchOfficials;
    Context context;

    public adapterMatchOfficial(@NonNull Context context, int resource, List<MatchOfficial> objects) {
        super(context, resource, objects);
        matchOfficials = objects;
        this.context= context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.listitemmatchofficial,parent,false);
        }

        MatchOfficial matchOfficial = matchOfficials.get(position);

        TextView moName = convertView.findViewById(R.id.moName);
        TextView moRole = convertView.findViewById(R.id.moRole);
        TextView moClub = convertView.findViewById(R.id.moClub);

        moName.setText("" + matchOfficial. getPlayerName());
        moRole.setText("" + matchOfficial.getRole());
        moClub.setText("" + matchOfficial.getClubName());
        return convertView;
    }
}
