package com.example.juri.naakkaprojekti;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by juri on 12.4.2018.
 */

public class ScheduleArrayAdapter extends ArrayAdapter<Game> {
    private final Activity context;
    private final ArrayList<Game> games;


    public ScheduleArrayAdapter(Activity context, ArrayList<Game> games) {
        super(context, R.layout.schedule_list, games);
        this.context = context;
        this.games = games;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.schedule_list, parent, false);
        TextView home = (TextView) rowView.findViewById(R.id.home);
        TextView away = (TextView) rowView.findViewById(R.id.away);
        TextView venue = (TextView) rowView.findViewById(R.id.venue);
        TextView time = (TextView) rowView.findViewById(R.id.time);
        home.setText(games.get(position).home);
        away.setText(games.get(position).away);
        venue.setText(games.get(position).venue);
        time.setText(games.get(position).gameTime);
        return rowView;

    }
}

