package com.example.juri.naakkaprojekti;

/**
 * Created by juri on 12.4.2018.
 */

public class Game {
    public int gameID;
    public String home;
    public String away;
    public String gameTime;
    public String venue;

    public String toString(){
        return gameID + ":" + home + " vs " + away + ", time: " + gameTime + " @ " + venue;
    }
}
