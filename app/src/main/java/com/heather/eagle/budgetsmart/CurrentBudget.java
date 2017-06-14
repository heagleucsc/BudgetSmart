package com.heather.eagle.budgetsmart;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by intropella on 6/14/17.
 */

@IgnoreExtraProperties
public class CurrentBudget {

    private String currentBudget;

    public CurrentBudget(){};

    public CurrentBudget(String currentBudget){

        this.currentBudget = currentBudget;
    }
    public String getBudget(){ return currentBudget;}
}

