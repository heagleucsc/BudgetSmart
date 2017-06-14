package com.heather.eagle.budgetsmart;

import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    AppInfo appInfo;
    ListView lv;
    private MyAdapter aa;
    final static ArrayList<ListElement> itemList = new ArrayList<ListElement>();

    private class ListElement {
        ListElement() {};

        ListElement(String name, String cost, String status, String category) {
            this.name = name;
            this.cost = cost;
            this.status = status;
            this.category = category;
        }

        public String name;
        public String cost;
        public String status;
        public String category;

        public String getName() {
            return name;
        }
        public String getCost() {
            return cost;
        }
        public String getStatus() { return status; }
        public String getCateg() { return category; }
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
                    //refreshCtr();
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

        // Set and configure navigation drawer
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        addDrawerItems();
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        budgetCounter = (TextView) findViewById(R.id.budgetCurrent);
        lv = (ListView) findViewById(R.id.listView);
        refreshCtr();

        aa = new MyAdapter(this, R.layout.list_element, itemList);
        //loadPreferences();
        lv.setAdapter(aa);
        aa.notifyDataSetChanged();
    }

    private void addDrawerItems(){
        String[] osArray = { "Overview", "Receipts" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "You're a budgeting star!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupDrawer(){
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation");
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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


    public void animateCtr(int initVal, int finVal, final TextView tv){
        ValueAnimator valAnim = ValueAnimator.ofInt(initVal, finVal);
        valAnim.setDuration(900);

        valAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator valAnim){
                tv.setText("$" + valAnim.getAnimatedValue().toString());
            }
        });
        valAnim.start();

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
        String[] categWords = sp.getString("category", null).split(",");

        if(nameWords!=null && nameWords.length>0) {

            // Update budget (increase)
            int budget = sp.getInt("budget", 0);
            int budgetNew = budget + Integer.parseInt(costWords[pos]);
            animateCtr(budget, budgetNew, (TextView)findViewById(R.id.budgetCurrent));
            // if(budget < 0){ }
            Log.d(LOG_TAG, "new budget: " + budgetNew);

            // Remove chosen item at index pos from array, then reconstruct new string
            String[] nameNew = ArrayUtils.remove(nameWords, pos);
            String[] costNew = ArrayUtils.remove(costWords, pos);
            String[] statusNew = ArrayUtils.remove(statusWords, pos);
            String[] categNew = ArrayUtils.remove(categWords, pos);
            Log.d(LOG_TAG, "new arrays: " + Arrays.toString(nameNew) + "\n" + Arrays.toString(costNew) + "\n" + Arrays.toString(statusNew)
            + "\n" + Arrays.toString(categNew));

            StringBuilder bName = new StringBuilder();
            StringBuilder bCost = new StringBuilder();
            StringBuilder bStatus = new StringBuilder();
            StringBuilder bCateg = new StringBuilder();

            for (int i = 0; i < nameNew.length; i++) {
                bName = bName.append(nameNew[i]).append(",");
                bCost = bCost.append(costNew[i]).append(",");
                bStatus = bStatus.append(statusNew[i]).append(",");
                bCateg = bCateg.append(categNew[i]).append(",");
            }

            Log.d(LOG_TAG, "stringbuilder test:" + bName.toString() + " " + bCost.toString() + " " + bStatus.toString() + " " + bCateg.toString());

            // Update to memory
            editor.clear();
            editor.putString("name", bName.toString());
            editor.putString("cost", bCost.toString());
            editor.putString("status", bStatus.toString());
            editor.putString("category", bCateg.toString());
            editor.putInt("old_budget", budget);
            editor.putInt("budget", budgetNew);
            editor.commit();
        }
        //decrementCounter(pos);
    }

    // Repopulate list
    protected void loadPreferences(){
        itemList.clear();
        SharedPreferences sp = getSharedPreferences(MYPREFS, 0);
        TextView tv = (TextView)findViewById(R.id.budgetCurrent);
        String listData = sp.getString("name", null);
        String listData2 = sp.getString("cost", null);
        String listData3 = sp.getString("status", null);
        String listData4 = sp.getString("category", null);
        Log.d(LOG_TAG, "loadPrefs: listData listData2: " + listData + " " + listData2 + " " + listData3);
        Log.d(LOG_TAG, "loadPrefs: listData4: " + listData4);

        refreshCtr();

        // Parse strings
        if(listData !=null && listData2 != null && listData3 != null && listData4 != null){
            //animateCtr(sp.getInt("old_budget", 0), sp.getInt("budget", 0), tv);
            String[] nameWords = listData.split(",");
            String[] costWords = listData2.split(",");
            String[] statusWords = listData3.split(",");
            String[] categWords = listData4.split(",");
            // Split returns at least one element so need to prevent showing empty string
            if(nameWords[0].equals("")) return;
            for (int k=0; k<nameWords.length; k++){
                    Log.d(LOG_TAG, "item/cost array: " + nameWords[k] + " " + costWords[k] );

            }

            // Add item to list
            for(int i=0; i<nameWords.length; i++){
                    aa.add(new ListElement(nameWords[i], costWords[i], statusWords[i], categWords[i]));
            }
            for (ListElement item : itemList){
                Log.d(LOG_TAG, "inCurrentList: " + item.name + " " + item.cost + " " + item.status + " " + item.category);
                Log.d(LOG_TAG, "itemList size: " + itemList.size());
            }
        }

        aa.notifyDataSetChanged();
    }
}
