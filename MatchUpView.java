package com.example.juri.naakkaprojekti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MatchUpView extends AppCompatActivity {

    private ListView lv;
    private TextView homeText, awayText;
    private GameFinal gameFinal;
    private Check check;
    private ImageView homeImage, awayImage;
    private ArrayList<String> previewResponse = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_up_view);
        homeText = (TextView)findViewById(R.id.homeTW);
        Intent i = getIntent();
        String gameID = i.getStringExtra(MainActivity.SELECTED_GAME);
        getScheduleData(gameID);
    }

    public void getScheduleData(String id){

        System.out.println("GetGameData");
        String url = "https://statsapi.web.nhl.com/api/v1/game/" + id + "/feed/live";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        checkGameStatus(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR");

            }
        });

        requestQueue.add(stringRequest);
    }

    private void checkGameStatus( String jsonResponse){
        try {
            JSONObject responseJSONObject = new JSONObject(jsonResponse);
            String gameStatus = responseJSONObject.getJSONObject("gameData").getJSONObject("status").getString("abstractGameState");
            if(gameStatus.equals("Preview")){
                Toast.makeText(getApplicationContext(), gameStatus, Toast.LENGTH_LONG).show();
                getJsonGameDataPreview(responseJSONObject);
            } else if(gameStatus.equals("Final")){
                Toast.makeText(getApplicationContext(), gameStatus, Toast.LENGTH_LONG).show();
                parseJsonGameDataFinal(responseJSONObject);
            } else {
                Toast.makeText(getApplicationContext(), gameStatus, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseJsonGameDataFinal(JSONObject responseJSONObject){
        try {
            gameFinal = new GameFinal();
            gameFinal.awayTeam = responseJSONObject.getJSONObject("liveData").getJSONObject("boxscore").getJSONObject("teams").getJSONObject("away").getJSONObject("team").getString("abbreviation");
            gameFinal.homeTeam = responseJSONObject.getJSONObject("liveData").getJSONObject("boxscore").getJSONObject("teams").getJSONObject("home").getJSONObject("team").getString("abbreviation");
            gameFinal.awayID = responseJSONObject.getJSONObject("liveData").getJSONObject("boxscore").getJSONObject("teams").getJSONObject("away").getJSONObject("team").getInt("id");
            gameFinal.homeID = responseJSONObject.getJSONObject("liveData").getJSONObject("boxscore").getJSONObject("teams").getJSONObject("home").getJSONObject("team").getInt("id");
            gameFinal.venue = responseJSONObject.getJSONObject("gameData").getJSONObject("venue").getString("name");

            JSONObject boxScoreAway = responseJSONObject.getJSONObject("liveData").getJSONObject("boxscore").getJSONObject("teams").getJSONObject("away");
            JSONObject boxScoreHome = responseJSONObject.getJSONObject("liveData").getJSONObject("boxscore").getJSONObject("teams").getJSONObject("home");

            JSONObject awayTeamStats = boxScoreAway.getJSONObject("teamStats").getJSONObject("teamSkaterStats");
            JSONObject homeTeamStats = boxScoreHome.getJSONObject("teamStats").getJSONObject("teamSkaterStats");

            check = new Check();
            for(int x=0; x<10; x++){
                if(x==3 || x==6)
                    gameFinal.gameArrayList.add(check.getFinalStat(x, homeTeamStats, awayTeamStats, true));
                else
                    gameFinal.gameArrayList.add(check.getFinalStat(x, homeTeamStats, awayTeamStats, false));
            }

            setMatchUpListFinal();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setMatchUpListFinal(){

        check = new Check();
        homeImage = (ImageView) findViewById(R.id.homeImageView);
        check.chooseImage(gameFinal.homeTeam, homeImage);
        awayImage = (ImageView) findViewById(R.id.awayImageView);
        check.chooseImage(gameFinal.awayTeam, awayImage);
        homeText = (TextView) findViewById(R.id.homeTW);
        homeText.setText(gameFinal.homeTeam);
        awayText = (TextView) findViewById(R.id.awayTW);
        awayText.setText(gameFinal.awayTeam);

        lv = (ListView) findViewById(R.id.statsListView);
        lv.setAdapter(new MatchUpArrayAdapter(this, gameFinal.gameArrayList));
    }

    private void getJsonGameDataPreview(JSONObject responseJSONObject){
        try {
            gameFinal = new GameFinal();
            gameFinal.awayTeam = responseJSONObject.getJSONObject("liveData").getJSONObject("boxscore").getJSONObject("teams").getJSONObject("away").getJSONObject("team").getString("abbreviation");
            gameFinal.homeTeam = responseJSONObject.getJSONObject("liveData").getJSONObject("boxscore").getJSONObject("teams").getJSONObject("home").getJSONObject("team").getString("abbreviation");
            gameFinal.awayID = responseJSONObject.getJSONObject("liveData").getJSONObject("boxscore").getJSONObject("teams").getJSONObject("away").getJSONObject("team").getInt("id");
            gameFinal.homeID = responseJSONObject.getJSONObject("liveData").getJSONObject("boxscore").getJSONObject("teams").getJSONObject("home").getJSONObject("team").getInt("id");
            getPreviewData(gameFinal.awayID);
            getPreviewData(gameFinal.homeID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getPreviewData(int id){

        final String[] giveResponse = new String[1];
        String url = "https://statsapi.web.nhl.com/api/v1/teams/" + id + "/stats";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        previewResponse.add(response);
                        if(previewResponse.size()>1)
                            parsePreviewData();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR");
            }
        });
        requestQueue.add(stringRequest);
    }

    private void parsePreviewData(){
        try {
            JSONObject awayResponseJSONObject = new JSONObject(previewResponse.get(0));
            JSONObject homeResponseJSONObject = new JSONObject(previewResponse.get(1));
            JSONObject awaySplitStats = (JSONObject) (awayResponseJSONObject.getJSONArray("stats")).get(0);
            JSONObject awayTmp = (JSONObject) (awaySplitStats.getJSONArray("splits")).get(0);
            JSONObject awayTeamStats = (JSONObject) awayTmp.getJSONObject("stat");
            JSONObject homeSplitStats = (JSONObject) (homeResponseJSONObject.getJSONArray("stats")).get(0);
            JSONObject homeTmp = (JSONObject) (homeSplitStats.getJSONArray("splits")).get(0);
            JSONObject homeTeamStats = (JSONObject) homeTmp.getJSONObject("stat");

            check = new Check();
            for(int x=0; x<28; x++){
                if(x==5 || x==9 || x==13 || x==25)
                    gameFinal.gameArrayList.add(check.getPreviewStat(x, homeTeamStats, awayTeamStats, true));
                else
                    gameFinal.gameArrayList.add(check.getPreviewStat(x, homeTeamStats, awayTeamStats, false));
            }

            setMatchUpListPreview();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setMatchUpListPreview(){

        check = new Check();
        homeImage = (ImageView) findViewById(R.id.homeImageView);
        check.chooseImage(gameFinal.homeTeam, homeImage);
        awayImage = (ImageView) findViewById(R.id.awayImageView);
        check.chooseImage(gameFinal.awayTeam, awayImage);
        homeText = (TextView) findViewById(R.id.homeTW);
        homeText.setText(gameFinal.homeTeam);
        awayText = (TextView) findViewById(R.id.awayTW);
        awayText.setText(gameFinal.awayTeam);

        lv = (ListView) findViewById(R.id.statsListView);
        lv.setAdapter(new MatchUpArrayAdapter(this, gameFinal.gameArrayList));
    }


}
