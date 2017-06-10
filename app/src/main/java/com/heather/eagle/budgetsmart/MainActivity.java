package com.heather.eagle.budgetsmart;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

public class MainActivity extends AppCompatActivity {

    public static final String MYPREFS = "myprefs";
    private static final String LOG_TAG = "budgetSmart";
    public static TextView budgetCounter;
    public static String name;
    public static String cost;     // later may want to deal with as double?
    public static String status;

    AppInfo appInfo;
    ListView lv;
    private MyAdapter aa;
    final static ArrayList<ListElement> itemList = new ArrayList<ListElement>();

    private class ListElement {
        ListElement() {};

        ListElement(String name, String cost, String status) {
            this.name = name;
            this.cost = cost;
            this.status = status;
        }

        public String name;
        public String cost;
        public String status;

        public String getName() {
            return name;
        }

        public String getCost() {
            return cost;
        }

        public String getStatus() { return status; }
    }

    private class MyAdapter extends ArrayAdapter<ListElement> {

        int resource;
        Context context;

        public MyAdapter(Context _context, int _resource, List<ListElement> items) {
            super(_context, _resource, items);
            resource = _resource;
            context = _context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout newView;

            ListElement w = getItem(position);

            // Inflate a new view if necessary.
            if (convertView == null) {
                newView = new LinearLayout(getContext());
                LayoutInflater vi = (LayoutInflater)
                        getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi.inflate(resource,  newView, true);
            } else {
                newView = (LinearLayout) convertView;
            }

            // Fills in the view.
            TextView tn = (TextView) newView.findViewById(R.id.itemName);
            TextView tc = (TextView) newView.findViewById(R.id.itemCost);
            Button deleteBtn = (Button) newView.findViewById(R.id.btn_del);
            tn.setText(w.name);
            tc.setText("$" + w.cost);


            // Remove item from list when delete button pressed
            deleteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Log.d(LOG_TAG, "delete btn pressed");
                    final int position = lv.getPositionForView((View) v.getParent());
                    Log.d(LOG_TAG, "position: " + position);
                    removeElement(position);
                    //decrementCounter(position);
                    refreshCtr();
                    notifyDataSetChanged();
                }
            });

            return newView;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appInfo = AppInfo.getInstance(this);

        budgetCounter = (TextView) findViewById(R.id.budgetCurrent);
        lv = (ListView) findViewById(R.id.listView);

        SharedPreferences sp = getSharedPreferences(MYPREFS, 0);
        String currentBudget = "$" + sp.getInt("budget", 0);
        ((TextView)findViewById(R.id.budgetCurrent)).setText(currentBudget);

        aa = new MyAdapter(this, R.layout.list_element, itemList);
        //loadPreferences();
        lv.setAdapter(aa);
        aa.notifyDataSetChanged();
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadPreferences();
    }

    public void getNewItemData() {
        //intent.putExtra("new item", );
        //ArrayList<String> test = getIntent().getStringArrayListExtra("test");

        // Irrelevant now since we're using SharedPrefs
/*
        Bundle extras = getIntent().getExtras();
        if (getIntent().getStringExtra("Name") != null && getIntent().getStringExtra("Cost") != null){
            name = extras.getString("Name");
            cost = extras.getString("Cost");
            itemList.add(new ListElement(name, cost));
            Log.d(LOG_TAG, "getNewItemData: name and cost added from form activity: " + name + cost);
            for (ListElement item : itemList){
                Log.d(LOG_TAG, "item in list: " + item.name + " " + item.cost);
            }
        }
*/
    }

    public void onClickAdd(View v) {
        // Intent to go to form activity
        Intent intent = new Intent(this, ItemFormActivity.class);
        startActivity(intent);
    }

    // Refresh budget counter
    public void refreshCtr() {
        SharedPreferences sp = getSharedPreferences(MYPREFS, 0);
        String currentBudget = "$" + sp.getInt("budget", 0);
        ((TextView)findViewById(R.id.budgetCurrent)).setText(currentBudget);
    }

    // Remove from list and memory
    public void removeElement(int pos){
        itemList.remove(pos);
        SharedPreferences sp = getSharedPreferences(MYPREFS, 0);
        SharedPreferences.Editor editor = sp.edit();

        // Get strings from memory and parse
        String[] nameWords = sp.getString("name", null).split(",");
        String[] costWords = sp.getString("cost", null).split(",");
        String[] statusWords = sp.getString("status", null).split(",");

        if(nameWords!=null && nameWords.length>0) {

            // Update budget (increase)
            int budget = sp.getInt("budget", 0);
            budget = budget + Integer.parseInt(costWords[pos]);
            // if(budget < 0){ }
            Log.d(LOG_TAG, "new budget: " + budget);

            // Remove chosen item at index pos from array, then reconstruct new string
            String[] nameNew = ArrayUtils.remove(nameWords, pos);
            String[] costNew = ArrayUtils.remove(costWords, pos);
            String[] statusNew = ArrayUtils.remove(statusWords, pos);
            Log.d(LOG_TAG, "new arrays: " + Arrays.toString(nameNew) + "\n" + Arrays.toString(costNew) + "\n" + Arrays.toString(statusNew));

            StringBuilder bName = new StringBuilder();
            StringBuilder bCost = new StringBuilder();
            StringBuilder bStatus = new StringBuilder();

            for (int i = 0; i < nameNew.length; i++) {
                bName = bName.append(nameNew[i]).append(",");
                bCost = bCost.append(costNew[i]).append(",");
                bStatus = bStatus.append(statusNew[i]).append(",");
            }

            Log.d(LOG_TAG, "stringbuilder test:" + bName.toString() + " " + bCost.toString() + " " + bStatus.toString());

            // Update to memory
            editor.clear();
            editor.putString("name", bName.toString());
            editor.putString("cost", bCost.toString());
            editor.putString("status", bStatus.toString());
            editor.putInt("budget", budget);
            editor.commit();
        }
        //decrementCounter(pos);
    }

    // Repopulate list
    protected void loadPreferences(){
        itemList.clear();
        SharedPreferences sp = getSharedPreferences(MYPREFS, 0);
        String listData = sp.getString("name", null);
        String listData2 = sp.getString("cost", null);
        String listData3 = sp.getString("status", null);
        Log.d(LOG_TAG, "loadPrefs: listData listData2: " + listData + " " + listData2 + " " + listData3);
        Log.d(LOG_TAG, "loadPrefs: listData3: " + listData3);

        refreshCtr();

        // Parse strings
        if(listData!=null && listData2 !=null && listData3 != null){
            String[] nameWords = listData.split(",");
            String[] costWords = listData2.split(",");
            String[] statusWords = listData3.split(",");
            // Split returns at least one element so need to prevent showing empty string
            if(nameWords[0].equals("")) return;
            for (int k=0; k<nameWords.length; k++){
                    Log.d(LOG_TAG, "item/cost array: " + nameWords[k] + " " + costWords[k] );

            }

            // Add item to list
            for(int i=0; i<nameWords.length; i++){
                    aa.add(new ListElement(nameWords[i], costWords[i], statusWords[i]));
            }
            for (ListElement item : itemList){
                Log.d(LOG_TAG, "inCurrentList: " + item.name + " " + item.cost + " " + item.status);
                Log.d(LOG_TAG, "itemList size: " + itemList.size());
            }
        }

        aa.notifyDataSetChanged();
    }
}
