package com.heather.eagle.budgetsmart;

import android.support.v7.app.AppCompatActivity;
import java.util.Calendar;
import android.app.Activity;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

/**
 * Created by intropella on 6/14/17.
 */

public class AskDate_Activity extends AppCompatActivity {
    private DatePicker datePicker;
    private CalendarView calendarView;
    private TextView tv;
    private int year,month,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_date);
        calendarView = (CalendarView) findViewById(R.id.datePick);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                dateDisplay.setText("Date: " + i2 + " / " + i1 + " / " + i);
                Toast.makeText(getApplicationContext(), "Selected Date:\n" + "Day = " + day + "\n" + "Month = " + month + "\n" + "Year = " + iyear, Toast.LENGTH_LONG).show();
            }
        });
    }

}

