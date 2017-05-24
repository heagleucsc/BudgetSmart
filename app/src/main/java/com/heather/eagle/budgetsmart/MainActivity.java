package com.heather.eagle.budgetsmart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    public static String cost;     // may want to deal with as double to change counter

    AppInfo appInfo;
    ListView lv;
    private MyAdapter aa;
    final static ArrayList<ListElement> itemList = new ArrayList<ListElement>();

    private class ListElement {
        ListElement() {};

        ListElement(String name, String cost) {
            this.name = name;
            this.cost = cost;
        }

        public String name;
        public String cost;

        public String getName() {
            return name;
        }

        public String getCost() {
            return cost;
        }
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
            tc.setText(w.cost);

            return newView;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        budgetCounter = (TextView) findViewById(R.id.budgetCurrent);
        lv = (ListView) findViewById(R.id.listView);

        final Button saveButton = (Button) findViewById(R.id.addSaveButton);

        //appInfo = AppInfo.getInstance(this);
        //itemList = new ArrayList<ListElement>();
        getNewItemData();
        aa = new MyAdapter(this, R.layout.list_element, itemList);
        /*Bundle extras = getIntent().getExtras();
        if (getIntent().getStringExtra("Name") != null && getIntent().getStringExtra("Cost") != null){
            name = extras.getString("Name");
            cost = extras.getString("Cost");
            itemList.add(new ListElement(name, cost));
            Log.d(LOG_TAG, "onCreate: name and cost added from form activity: " + name + cost);
        }*/
        lv.setAdapter(aa);
        aa.notifyDataSetChanged();
    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences settings = getSharedPreferences(MainActivity.MYPREFS, 0);

        lv = (ListView) findViewById(R.id.listView);
        String itemText1 = settings.getString(ItemFormActivity.ITEM_NAME_STRING, "");
        String itemText2 = settings.getString(ItemFormActivity.ITEM_COST_STRING, "");

    }

    public void getNewItemData() {
        //intent.putExtra("new item", );
        //ArrayList<String> test = getIntent().getStringArrayListExtra("test");
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
    }

    public void onClickAdd(View v) {
        // Intent to go to form activity
        Intent intent = new Intent(this, ItemFormActivity.class);
        startActivity(intent);
    }

    public void decrementCounter(){}
}
