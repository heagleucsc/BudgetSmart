package com.heather.eagle.budgetsmart;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

public class ItemFormActivity extends AppCompatActivity {

    static final public String ITEM_NAME_STRING = "string_1";
    static final public String ITEM_COST_STRING = "string_2";
    static final public String ITEM_STATUS_STRING = "string_3";
    private static final String LOG_TAG = "formActivity";
    public static StringBuilder sb1 = new StringBuilder();
    public static StringBuilder sb2 = new StringBuilder();
    public static StringBuilder sb3 = new StringBuilder();
    public static StringBuilder sb4 = new StringBuilder();
    String namesb = "";
    String costsb = "";
    String statussb = "";
    String categorysb = "";
    String status = "optional";
    AppInfo appInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_form);
        appInfo = AppInfo.getInstance(this);

        Spinner s1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categ_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onSaveItem(View v){
        SharedPreferences sp = getSharedPreferences(MainActivity.MYPREFS, 0);

        // Load saved strings
        if(sb1.toString().equals("") || sb2.toString().equals("") || sb3.toString().equals("") || sb4.toString().equals("")){
            sb1 = sb1.append(sp.getString("name", ""));
            sb2 = sb2.append(sp.getString("cost", ""));
            sb3 = sb3.append(sp.getString("status", ""));
            sb4 = sb4.append(sp.getString("category", ""));
        }else{
            sb1 = new StringBuilder(sp.getString("name", ""));
            sb2 = new StringBuilder(sp.getString("cost", ""));
            sb3 = new StringBuilder(sp.getString("status", ""));
            sb4 = new StringBuilder(sp.getString("category", ""));
        }

        // Add new item details to corresponding strings (name,cost,nec/opt)
        EditText edv1 = (EditText) findViewById(R.id.itemName);
        String name = edv1.getText().toString();
        //Log.d(LOG_TAG, "name" + name);
        namesb = sb1.append(name).append(",").toString();
        Log.d(LOG_TAG, "namesb: " + namesb);

        EditText edv2 = (EditText) findViewById(R.id.itemCost);
        String cost = edv2.getText().toString();
        //Log.d(LOG_TAG, "cost: " + cost);

        // Retrieve drop-down menu value
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        String spinnerVal = spinner1.getSelectedItem().toString();
        categorysb = sb4.append(spinnerVal).append(",").toString();
        Log.d(LOG_TAG, "Category: " + spinnerVal);
        Log.d(LOG_TAG,"categorysb: " + categorysb);

        /* make sure null, comma, etc values not accepted */
        if(name == null | name.equals("") | cost == null | cost.equals("")) {
            Toast.makeText(getApplicationContext(),"Please enter an item name and cost.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(name.contains(",") | cost.contains(",") | cost.contains("-") | !NumberUtils.isNumber(cost)){
            Toast.makeText(getApplicationContext(),"Please change your item name or cost value.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Round double up to nearest dollar
        int costp = (int) Math.ceil(Double.parseDouble(cost));
        costsb = sb2.append(String.valueOf(costp)).append(",").toString();
        statussb = sb3.append(status).append(",").toString();


        //update budget variable
        int old_budget = sp.getInt("budget", 0);
        int budget = old_budget - costp;
        if(budget < 0){
            //
        }


        // Save to memory
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", namesb);
        editor.putString("cost", costsb);
        editor.putString ("status", statussb);
        editor.putString("category", categorysb);
        editor.putInt("old_budget", old_budget);
        editor.putInt("budget", budget);
        editor.commit();

        Toast.makeText(getApplicationContext(),"Item added", Toast.LENGTH_SHORT).show();

        // Go back to main activity
        Intent intent = new Intent(this, MainActivity.class);
        Log.d(LOG_TAG,"Name and cost added:" + name + cost);
        startActivity(intent);
    }

    public void onClickOpt(View v){ status = "optional"; }

    public void onClickNess(View v){ status = "necessary"; }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
