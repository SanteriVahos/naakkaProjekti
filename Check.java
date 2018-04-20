package com.example.juri.naakkaprojekti;

import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by juri on 12.4.2018.
 */

public class Check {

    private String[] statNamesFinal = new String[] {
            "goals", "penalty min", "shots",
            "PP %", "pp goals", "PP oportinities",
            "faceoff %", "blocked shots", "takeaways",
            "giveaways", "hits"
    };

    private String[] statsFinal = new String[] {
            "goals", "pim", "shots", "powerPlayPercentage",
            "powerPlayGoals", "powerPlayOpportunities",
            "faceOffWinPercentage", "blocked", "takeaways",
            "giveaways", "hits"
    };

    private String[] statsPreview = new String[] {
            "gamesPlayed", "wins", "losses", "ot", "pts",
            "ptPctg", "goalsPerGame", "goalsAgainstPerGame",
            "evGGARatio", "powerPlayPercentage", "powerPlayGoals",
            "powerPlayGoalsAgainst", "powerPlayOpportunities",
            "penaltyKillPercentage", "shotsPerGame", "shotsAllowed",
            "winScoreFirst", "winOppScoreFirst", "winLeadFirstPer",
            "winLeadSecondPer", "winOutshootOpp", "winOutshotByOpp",
            "faceOffsTaken", "faceOffsWon", "faceOffsLost",
            "faceOffWinPercentage", "shootingPctg", "savePctg"
    };

    public GameFinalStats getFinalStat(int x, JSONObject home, JSONObject away, boolean stringInt){
        GameFinalStats tmp = new GameFinalStats();
        tmp.Stat = statNamesFinal[x];
        try {
            if(stringInt){
                tmp.Home = home.getString(statsFinal[x]);
                tmp.Away = away.getString(statsFinal[x]);

            } else {
                tmp.Home = Integer.toString(home.getInt(statsFinal[x]));
                tmp.Away = Integer.toString(away.getInt(statsFinal[x]));
            }
        return tmp;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tmp;
    }


    public GameFinalStats getPreviewStat(int x, JSONObject home, JSONObject away, boolean stringInt){
        GameFinalStats tmp = new GameFinalStats();
        tmp.Stat = statsPreview[x];
        try {
            if(stringInt){
                tmp.Home = home.getString(statsPreview[x]);
                tmp.Away = away.getString(statsPreview[x]);

            } else {
                tmp.Home = Integer.toString(home.getInt(statsPreview[x]));
                tmp.Away = Integer.toString(away.getInt(statsPreview[x]));
            }
            return tmp;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tmp;
    }

    public int compareTwoValues(String home, String away){
        double homeInt = Double.parseDouble(home);
        double awayInt = Double.parseDouble(away);
        double dValue = (homeInt/(homeInt+awayInt))*2*100;
        int value = (int) dValue;
        return value;
    }

    public void chooseImage(String team, ImageView image){
        switch(team){
            case "ANA":
                image.setImageResource(R.drawable.ana);
                break;

            case "ARI":
                image.setImageResource(R.drawable.ari);
                break;

            case "BOS":
                image.setImageResource(R.drawable.bos);
                break;

            case "BUF":
                image.setImageResource(R.drawable.buf);
                break;

            case "CAR":
                image.setImageResource(R.drawable.car);
                break;

            case "CBJ":
                image.setImageResource(R.drawable.cbj);
                break;

            case "CGY":
                image.setImageResource(R.drawable.cgy);
                break;

            case "CHI":
                image.setImageResource(R.drawable.chi);
                break;

            case "COL":
                image.setImageResource(R.drawable.col);
                break;

            case "DAL":
                image.setImageResource(R.drawable.dal);
                break;

            case "DET":
                image.setImageResource(R.drawable.det);
                break;

            case "EDM":
                image.setImageResource(R.drawable.edm);
                break;

            case "FLA":
                image.setImageResource(R.drawable.fla);
                break;

            case "LAK":
                image.setImageResource(R.drawable.la);
                break;

            case "MIN":
                image.setImageResource(R.drawable.min);
                break;

            case "MTL":
                image.setImageResource(R.drawable.mtl);
                break;

            case "NSH":
                image.setImageResource(R.drawable.nas);
                break;

            case "NJD":
                image.setImageResource(R.drawable.nj);
                break;

            case "NYI":
                image.setImageResource(R.drawable.nyi);
                break;

            case "NYR":
                image.setImageResource(R.drawable.nyr);
                break;

            case "OTT":
                image.setImageResource(R.drawable.ott);
                break;

            case "PHI":
                image.setImageResource(R.drawable.phi);
                break;

            case "PIT":
                image.setImageResource(R.drawable.pit);
                break;

            case "SJS":
                image.setImageResource(R.drawable.sj);
                break;

            case "STL":
                image.setImageResource(R.drawable.stl);
                break;

            case "TBL":
                image.setImageResource(R.drawable.tb);
                break;

            case "TOR":
                image.setImageResource(R.drawable.tor);
                break;

            case "VAN":
                image.setImageResource(R.drawable.van);
                break;

            case "VGK":
                image.setImageResource(R.drawable.vgk);
                break;

            case "WSH":
                image.setImageResource(R.drawable.was);
                break;

            case "WPG":
                image.setImageResource(R.drawable.wpg);
                break;

            default:
                image.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
        }
    }
}
