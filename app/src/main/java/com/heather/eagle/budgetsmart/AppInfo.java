package com.heather.eagle.budgetsmart;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by michellenguyen on 5/12/17.
 */

public class AppInfo {
    private static AppInfo instance = null;
    private static final String STRING_NAME = "some_name";
    private static final String STRING_COST = "some_cost";
    private static final String BOOLEAN_STATUS = "some_status";

    protected AppInfo() {
        // Exists only to defeat instantiation.
    }

    // Here are some values we want to keep global.
    public String itemNameString;
    public String itemCostString;
    public String itemStatusString;

    private Context my_context;

    public static AppInfo getInstance(Context context) {
        if(instance == null) {
            instance = new AppInfo();
            instance.my_context = context;
            SharedPreferences settings = context.getSharedPreferences(MainActivity.MYPREFS, 0);
            //instance.itemNameString = settings.getString(STRING_NAME, null);
            //instance.itemCostString = settings.getString(STRING_COST, null);
        }
        return instance;
    }
/*
    public void setItem(String c) {
        instance.sharedString = c;
        SharedPreferences settings = my_context.getSharedPreferences(MainActivity.MYPREFS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(STRING_NAME, c);
        editor.commit();
    }*/
}
