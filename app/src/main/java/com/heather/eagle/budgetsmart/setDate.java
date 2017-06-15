package com.heather.eagle.budgetsmart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class setDate extends AppCompatActivity {

    long week = 604800000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_date);
    }

//CalenderView layout learned from : http://abhiandroid.com/ui/calendarview
    public long getCurrentTime(){
        Date today = new Date();
        long start = today.getTime();
        return start;
    }

    public void onClickOne(View v){
        long start = getCurrentTime();
        long time = start + 2*week;
        SharedPreferences sp = getSharedPreferences(MainActivity.MYPREFS, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("Start", start);
        editor.putLong("End", time);
        editor.commit();
    }

    public void onClickTwo(View v){
        long start = getCurrentTime();
        long time = start + week;
        SharedPreferences sp = getSharedPreferences(MainActivity.MYPREFS, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("Start", start);
        editor.putLong("End", time);
        editor.commit();
    }

    public void onClickSet(View v){
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
    }
}
