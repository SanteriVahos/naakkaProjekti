package com.example.juri.naakkaprojekti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


    private ListView lv;
    private Button DateButton;
    private ImageButton PlusButton, MinusButton;
    private ArrayList<Schedule> scheduleArrayList = new ArrayList<Schedule>();

    private String curDate;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance();
    boolean calToggle  = true;
    private String chosenDate;
    private Check check;

    public static final String SELECTED_GAME = "com.example";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String chosenDate = dateFormat.format(Calendar.getInstance().getTime());
        getScheduleData(chosenDate);

        DateButton = (Button) findViewById(R.id.dateButton);
        PlusButton = (ImageButton) findViewById(R.id.imageButton2);
        MinusButton = (ImageButton) findViewById(R.id.imageButton);

        final CalendarView DateCalendarView = (CalendarView) findViewById(R.id.calendarView);
        DateCalendarView.setVisibility(View.GONE);

        final EditText DateText = (EditText) findViewById(R.id.editText2);
        DateText.setKeyListener(null);
        DateText.setText(getCurrentDate());

        DateCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth){

                int correctedMonth = month + 1; // Korjataan kuukausi näyttämään oikein
                curDate =String.valueOf(year) + "-" + String.valueOf(correctedMonth) + "-" + String.valueOf(dayOfMonth);
                Toast.makeText(getApplicationContext(), curDate, Toast.LENGTH_SHORT).show();
                calendar.set(year, month, dayOfMonth);
                DateText.setText(getCurrentDate());
                DateCalendarView.setVisibility(View.GONE);
                getScheduleData(curDate);
                lv.setVisibility(View.VISIBLE);
                calToggle=true;
            }
        });

        DateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(calToggle) {
                    DateCalendarView.setVisibility(View.VISIBLE);
                    lv.setVisibility(View.GONE);
                    calToggle = false;
                }
                else{
                    DateCalendarView.setVisibility(View.GONE);
                    lv.setVisibility(View.VISIBLE);
                    calToggle = true;
                }
            }
        });
        PlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, 1);
                DateText.setText(getCurrentDate());
                DateCalendarView.setDate(calendar.getTimeInMillis());

                printNewGames();
            }
        });
        MinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -1);
                DateText.setText(getCurrentDate());
                DateCalendarView.setDate(calendar.getTimeInMillis());
                printNewGames();
            }
        });

    }


    public String returnClickedDate(){
        return curDate;
    }

    public String getCurrentDate() {
        String strDate = df.format(calendar.getTime());
        return strDate;
    }

    private void printNewGames(){
        if(checkDate()){
            getScheduleData(getCurrentDate());
        }
        else
            setList();

    }

    public void getScheduleData(final String date){

            System.out.println("GetScheduleddata");
            String url = "https://statsapi.web.nhl.com/api/v1/schedule?date=" + date;
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            System.out.println("GetScheduleddata1");
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("GetScheduleddata2");
                            parseJsonAndUpdateUiSchedule(response, date);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("GetScheduleddata3");

                }
            });

            requestQueue.add(stringRequest);
        }


    private void parseJsonAndUpdateUiSchedule( String jsonResponse, String date){
        try {
            JSONObject responseJSONObject = new JSONObject(jsonResponse);
            if(responseJSONObject.getInt("totalGames")>0) {
                JSONObject datesObject = (JSONObject) (responseJSONObject.getJSONArray("dates")).get(0);
                JSONArray gamesObject = (JSONArray) datesObject.getJSONArray("games");
                System.out.println("datesObject: " + datesObject.length());
                System.out.println("gamesObject: " + gamesObject);
                Schedule tmp = new Schedule();
                tmp.time = datesObject.getString("date");

                for (int x = 0; gamesObject.length() > x; x++) {
                    Game gameTmp = new Game();
                    JSONObject oneGameObject = (JSONObject) gamesObject.get(x);
                    gameTmp.gameTime = oneGameObject.getString("gameDate");
                    gameTmp.gameID = oneGameObject.getInt("gamePk");
                    gameTmp.home = (((oneGameObject.getJSONObject("teams")).getJSONObject("home")).getJSONObject("team")).getString("name");
                    gameTmp.away = (((oneGameObject.getJSONObject("teams")).getJSONObject("away")).getJSONObject("team")).getString("name");
                    gameTmp.venue = (oneGameObject.getJSONObject("venue")).getString("name");
                    tmp.gameArrayList.add(gameTmp);
                }
                tmp.isThereGames = true;
                scheduleArrayList.add(tmp);
                Toast.makeText(getApplicationContext(), "READY", Toast.LENGTH_LONG).show();
            } else {
                Schedule tmp = new Schedule();
                tmp.time = date;
                tmp.isThereGames = false;
                scheduleArrayList.add(tmp);
                Toast.makeText(getApplicationContext(), "NO GAMES", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setList();
    }

    public void print(View view){
        for(Schedule d : scheduleArrayList) {
            Toast.makeText(getApplicationContext(), d.toString() , Toast.LENGTH_LONG).show();
        }
    }

    private void setList(){

        lv = (ListView) findViewById(R.id.scheduleList);

        lv.setAdapter(new ScheduleArrayAdapter(this, scheduleArrayList.get(getPosition(getCurrentDate())).gameArrayList));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), scheduleArrayList.get(getPosition(getCurrentDate())).gameArrayList.get(position).toString() , Toast.LENGTH_LONG).show();
                String selectedId = Integer.toString(scheduleArrayList.get(getPosition(getCurrentDate())).gameArrayList.get(position).gameID);
                Intent i;
                i = new Intent(getApplicationContext(), MatchUpView.class);
                i.putExtra(SELECTED_GAME, selectedId);
                startActivity(i);

            }
        });
    }

    private int getPosition(String date){
        int x=0;
        for(Schedule d : scheduleArrayList){
            String tmp = d.time;
            if(tmp.equals(date)){
                return x;
            }
            x++;
        }
        return 0;
    }

    private boolean checkDate(){
        String date = getCurrentDate();
        for(Schedule d : scheduleArrayList){
            String tmp = d.time;
            if(tmp.equals(date)){
                return false;
            }
        }
        return true;
    }

}