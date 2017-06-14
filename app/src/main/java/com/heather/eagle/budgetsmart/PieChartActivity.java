package com.heather.eagle.budgetsmart;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class PieChartActivity extends AppCompatActivity {

    // Use MPAndroidChart library to create pie graph

    private static final String LOG_TAG = "PieChartActivity";
    private FrameLayout layout;
    private PieChart mChart;
    AppInfo appInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        appInfo = AppInfo.getInstance(this);
        layout = (FrameLayout) findViewById(R.id.chartContainer);
        mChart = new PieChart(this);

        // Add chart to layout
        layout.addView(mChart);

        mChart.setTransparentCircleRadius(5);

        addData();

        // Set legend
        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
    }

    @Override
    protected void onResume(){
        super.onResume();
        addData();
    }

    private void addData(){

        // Retrieve data saved in memory
        SharedPreferences sp = getSharedPreferences(MainActivity.MYPREFS, 0);
        String costData = sp.getString("cost", null);   // Ex: "300,1000,2"
        String categoryData = sp.getString("category", null);   // Ex: "Food,Rent/Utilities,Food"
        Log.d(LOG_TAG, "costData, categoryData: " + costData + categoryData);

        int foodTotal, rentTotal, entTotal, transTotal, clothTotal, miscTotal;
        foodTotal = rentTotal = entTotal = transTotal = clothTotal =  miscTotal = 0;

        // Parse into string array
        if(costData != null && categoryData != null) {
            String[] costWords = costData.split(",");       // Ex: [300, 1000, 2]
            String[] categoryWords = categoryData.split(","); // Ex: [Food, Rent/Utilities, Food]

            for (int k=0; k<costWords.length; k++){
                Log.d(LOG_TAG, "cost/categ array: " + costWords[k] + " " + categoryWords[k] );

            }

            //Log.d(LOG_TAG, "1st cost & categ: " + categoryWords[0] + costWords[0]);
            //Log.d(LOG_TAG, "Integer.parseInt(costWords[0]): " + Integer.parseInt(costWords[0]));

            // For every same category word, add corresponding cost
            for(int i=0; i<costWords.length; i++){
                if(categoryWords[i].equals("Food")){
                    foodTotal += Integer.parseInt(costWords[i]);
                }else if(categoryWords[i].equals("Rent/Utilities")){
                    rentTotal += Integer.parseInt(costWords[i]);
                }else if(categoryWords[i].equals("Entertainment")){
                    entTotal += Integer.parseInt(costWords[i]);
                }else if(categoryWords[i].equals("Transportation")){
                    transTotal += Integer.parseInt(costWords[i]);
                }else if(categoryWords[i].equals("Clothing")){
                    clothTotal += Integer.parseInt(costWords[i]);
                }else if(categoryWords[i].equals("Misc")){
                    miscTotal += Integer.parseInt(costWords[i]);
                }else{
                    Log.d(LOG_TAG, "Error in identifying categoryWords[i]");
                    return;
                }
            }
        } else {
            Log.d(LOG_TAG, "costData == null | categoryData == null");
            return;
        }

        int grandTotal = foodTotal + rentTotal + entTotal + transTotal + clothTotal + miscTotal;
        Log.d(LOG_TAG, "grandTotal: " + grandTotal);
        Log.d(LOG_TAG, "foodTotal: " + foodTotal);
        int[] totalArr = {foodTotal, rentTotal, entTotal, transTotal, clothTotal, miscTotal};
        float[] percentData = new float[6];
        float[] yData = new float[6];
        String[] xData = new String[]{"Food", "Rent/Utilities", "Entertainment", "Transportation", "Clothing", "Misc"};
        // Calculate spending percentage of each category
        for(int i=0; i<totalArr.length; i++){     // percentData[0] == food % total, percentData[1] == rent %,...
            percentData[i] = (totalArr[i] / (float)grandTotal) * 100;
            Log.d(LOG_TAG, "percent of " + xData[i] + ": " + percentData[i]);
        }

        // xVals == Categories that were used

        // Populate data with array values
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for(int i=0; i < yData.length; i++) {
            // Display only categories in which money was spent
            if(totalArr[i] != 0) {
                yVals.add(new Entry(percentData[i], i));
            }
        }

        ArrayList<String> xVals = new ArrayList<String>();
        for(int i=0; i < xData.length; i++) {
            // Display only categories in which money was spent
            if(totalArr[i] !=0 ) {
                xVals.add(xData[i]);
            }
        }

        // Generate data set
        PieDataSet dataSet = new PieDataSet(yVals, "Total Spending");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // Add colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        // instantiate pie data object now
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        // update pie chart
        mChart.invalidate();
    }


}
