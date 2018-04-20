package com.example.juri.naakkaprojekti;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by juri on 16.4.2018.
 */

public class MatchUpArrayAdapter extends ArrayAdapter<GameFinalStats> {

    private final Activity context;
    private final ArrayList<GameFinalStats> stats;
    private Check check = new Check();

    public MatchUpArrayAdapter(Activity context, ArrayList<GameFinalStats> stats) {
        super(context, R.layout.match_up_list, stats);
        this.context = context;
        this.stats = stats;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.match_up_list, parent, false);
        TextView stat = (TextView) rowView.findViewById(R.id.comText);
        TextView home = (TextView) rowView.findViewById(R.id.homeStats);
        TextView away = (TextView) rowView.findViewById(R.id.awayStats);
        stat.setText(stats.get(position).Stat);
        home.setText(stats.get(position).Home);
        away.setText(stats.get(position).Away);

        ProgressBar pb = (ProgressBar) rowView.findViewById(R.id.progressBar);
        pb.setMax(200);
        pb.setProgress(check.compareTwoValues(stats.get(position).Home, stats.get(position).Away));

        return rowView;

    }
}
