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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String MYPREFS = "myprefs";
    private static final String LOG_TAG = "budgetSmart";
    public static TextView budgetCounter;
    public static String name;
    public static String cost;     // later may want to deal with as double or int to change counter value
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
            tn.setText(w.name);
            tc.setText("$" + w.cost);

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

        //getNewItemData();
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

    public void decrementCounter(){}

    protected void loadPreferences(){
        itemList.clear();
        SharedPreferences sp = getSharedPreferences(MYPREFS, 0);
        String currentBudget = "$" + sp.getInt("budget", 0);
        ((TextView)findViewById(R.id.budgetCurrent)).setText(currentBudget);
        String listData = sp.getString("name", null);
        String listData2 = sp.getString("cost", null);
        String listData3 = sp.getString("status", null);
        Log.d(LOG_TAG, "loadPrefs: listData listData2: " + listData + " " + listData2 + " " + listData3);
        Log.d(LOG_TAG, "loadPrefs: listData3: " + listData3);


        if(listData!=null && listData2 !=null && listData3 != null){
            String[] nameWords = listData.split(",");
            String[] costWords = listData2.split(",");
            String[] statusWords = listData3.split(",");
            for (int k=0; k<nameWords.length; k++){
                    Log.d(LOG_TAG, "item/cost array: " + nameWords[k] + " " + costWords[k] );

            }

            // Add item to list
            for(int i=0; i<nameWords.length; i++){
                    aa.add(new ListElement(nameWords[i], costWords[i], "optional"));
            }
            for (ListElement item : itemList){
                Log.d(LOG_TAG, "inCurrentList: " + item.name + " " + item.cost + " " + item.status);
                Log.d(LOG_TAG, "itemList size: " + itemList.size());
            }
        }

        aa.notifyDataSetChanged();

    }
}
