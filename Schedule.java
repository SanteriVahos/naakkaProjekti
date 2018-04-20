package com.example.juri.naakkaprojekti;

import java.util.ArrayList;

/**
 * Created by juri on 12.4.2018.
 */

public class Schedule {

    public String time;
    public boolean isThereGames;
    public ArrayList<Game> gameArrayList = new ArrayList<Game>();

    public String toString(){
        return time + gameArrayList;
    }
}
