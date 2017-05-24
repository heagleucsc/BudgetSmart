package com.heather.eagle.budgetsmart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ItemFormActivity extends AppCompatActivity {

    static final public String ITEM_NAME_STRING = "string_1";
    static final public String ITEM_COST_STRING = "string_2";
    static final public String ITEM_STATUS_STRING = "string_3";
    private static final String LOG_TAG = "formActivity";
    public static StringBuilder sb1 = new StringBuilder();
    public static StringBuilder sb2 = new StringBuilder();
    public static StringBuilder sb3 = new StringBuilder();
    String namesb = "";
    String costsb = "";
    String statussb = "";
    String status = "optional";
    AppInfo appInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_form);
        appInfo = AppInfo.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onSaveItem(View v){
        SharedPreferences sp = getSharedPreferences(MainActivity.MYPREFS, 0);
        if(sb1.toString().equals("") || sb2.toString().equals("") || sb3.toString().equals("")){
            sb1 = sb1.append(sp.getString("name", ""));
            sb2 = sb2.append(sp.getString("cost", ""));
            sb3 = sb3.append(sp.getString("status", ""));
        }

        EditText edv1 = (EditText) findViewById(R.id.itemName);
        String name = edv1.getText().toString();
        namesb = sb1.append(name).append(",").toString();
        Log.d(LOG_TAG, "namesb: " + namesb);


        EditText edv2 = (EditText) findViewById(R.id.itemCost);
        String cost = edv2.getText().toString();
        costsb = sb2.append(cost).append(",").toString();

        statussb = sb3.append(status).append(",").toString();

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", namesb);
        editor.putString("cost", costsb);
        editor.putString ("status", statussb);
        editor.commit();

        Toast.makeText(getApplicationContext(),"Item added", Toast.LENGTH_SHORT).show();

        // Go back to main activity
        Intent intent = new Intent(this, MainActivity.class);
        /*
        intent.putExtra("Name", name);
        intent.putExtra("Cost", cost);*/
        Log.d(LOG_TAG,"Name and cost added:" + name + cost);
        startActivity(intent);
    }

    public void onClickOpt(View v){
        status = "optional";
    }

    public void onClickNess(View v){
        status = "necessary";
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
