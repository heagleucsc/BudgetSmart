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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_budget);
/*
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if(pref.getBoolean("activity_executed", false)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            ed.commit();
        }*/
    }

    public void onSaveBudget(View v) {

        EditText ed = (EditText) findViewById(R.id.editBudgetV);
        String budgetVal = ed.getText().toString();
        Log.d(LOG_TAG, "AskBudget: budgetVal: " + budgetVal);
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("initBudget", budgetVal);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
