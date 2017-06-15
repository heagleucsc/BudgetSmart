package com.heather.eagle.budgetsmart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import static com.heather.eagle.budgetsmart.MainActivity.MYPREFS;

/**
 * Created by michellenguyen on 6/14/17.
 */

public class AskBudget extends AppCompatActivity {
    private static final String LOG_TAG = "AskBudget";
    public static final String PREFS = "prefs";

    AppInfo appInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_budget);
        appInfo = AppInfo.getInstance(this);

        //SharedPreferences pref = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        //SharedPreferences.Editor ed = pref.edit();

        //if(pref.getBoolean("activity_executed", false)){
            //Intent intent = new Intent(this, MainActivity.class);
            //startActivity(intent);
            //finish();
        //} else {
            //ed.putBoolean("activity_executed", true);
            //ed.commit();
        //}
    }

    public void onSaveBudget(View v) {
        SharedPreferences pref = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        EditText ed = (EditText) findViewById(R.id.editBudgetV);
        String budgetVal = ed.getText().toString();
        Log.d(LOG_TAG, "AskBudget: budgetVal: " + budgetVal);
        //Intent intent = new Intent(this, MainActivity.class);
        //Bundle bundle = new Bundle();
        //bundle.putString("initBudget", budgetVal);
        //intent.putExtras(bundle);
        //editor.putBoolean("activity_executed", true);
        editor.putString("initBudget", budgetVal);
        editor.commit();
        Intent intent = new Intent(this, setDate.class);
        startActivity(intent);
    }


}
