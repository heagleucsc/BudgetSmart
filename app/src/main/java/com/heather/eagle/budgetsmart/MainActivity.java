package com.heather.eagle.budgetsmart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String MYPREFS = "myprefs";
    private static final String LOG_TAG = "budget-main";

    AppInfo appInfo;
    ListView lv;
    private MyAdapter aa;
    private ArrayList<ListElement> itemList;

    private class ListElement {
        ListElement() {};

        ListElement(String item) {
            item = this.item;
        }

        public String item;
    }

    private class MyAdapter extends ArrayAdapter<ListElement> {

        int resource;
        Context context;

        public MyAdapter(Context _context, int _resource, List<ListElement> items) {
            super(_context, _resource, items);
            resource = _resource;
            context = _context;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appInfo = AppInfo.getInstance(this);
        itemList = new ArrayList<ListElement>();
        aa = new MyAdapter(this, R.layout.list_element, itemList);
        lv = (ListView) findViewById(R.id.listView);
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

    private void getDataFromPref() {
        //intent.putExtra("new item", );
        //ArrayList<String> test = getIntent().getStringArrayListExtra("test");

    }

    public void onClickAdd(View v) {
        // Intent to go to next activity
        Intent intent = new Intent(this, ItemFormActivity.class);
        startActivity(intent);
    }
}
