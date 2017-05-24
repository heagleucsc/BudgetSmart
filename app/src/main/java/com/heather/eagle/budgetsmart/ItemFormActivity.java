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
    private static final String LOG_TAG = "formActivity";

    //private List<String> test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_form);
        //test = new ArrayList<String>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences settings = getSharedPreferences(MainActivity.MYPREFS, 0);
    }

    public void onSaveItem(View v){
        // ** Still need to find a way to display list elements in MainActivity listview
        // from these strings saved in sharedPreferences **

        // was looking at: http://stackoverflow.com/questions/30072055/add-items-to-listview-from-other-activity

        EditText edv1 = (EditText) findViewById(R.id.itemName);
        EditText edv2 = (EditText) findViewById(R.id.itemCost);
        String name = edv1.getText().toString();
        String cost = edv2.getText().toString();

        SharedPreferences settings = getSharedPreferences(MainActivity.MYPREFS, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(ITEM_NAME_STRING, name);
        editor.putString(ITEM_COST_STRING, cost);
        editor.commit();

        Toast.makeText(getApplicationContext(),"Item added", Toast.LENGTH_SHORT).show();

        // Go back to main activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Name", name);
        intent.putExtra("Cost", cost);
        Log.d(LOG_TAG,"Name and cost added:" + name + cost);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        //intent.putStringArrayListExtra("test", (ArrayList<String>) test);
        startActivity(intent);
    }
}
