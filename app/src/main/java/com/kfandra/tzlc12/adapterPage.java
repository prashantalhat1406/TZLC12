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

public class adapterPage extends ArrayAdapter<Page> {

    List<Page> pages;
    Context context;

    public adapterPage(@NonNull Context context, int resource, List<Page> objects) {
        super(context, resource, objects);
        pages = objects;
        this.context= context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.listitempage,parent,false);
        }

        Page page = pages.get(position);

        TextView pageName = convertView.findViewById(R.id.pageName);
        TextView pageInfo = convertView.findViewById(R.id.pageInfo);

        pageName.setText("" + page.getPageName());
        pageInfo.setText("" + page.getPageInfo());

        return convertView;
    }
}
