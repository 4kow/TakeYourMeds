package com.example.takeyourmeds.activities;

import static com.example.takeyourmeds.utils.CalendarUtils.selectedDate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.takeyourmeds.R;
import com.example.takeyourmeds.adapters.HourAdapter;
import com.example.takeyourmeds.database.MedsBase;
import com.example.takeyourmeds.utils.CalendarUtils;
import com.example.takeyourmeds.utils.HourEventHolder;
import com.example.takeyourmeds.utils.Med;

import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

public class DayViewActivity extends AppCompatActivity
{
    private TextView monthDayText;
    private TextView dayOfWeekTV;
    private ListView hourListView;
    private MedsBase medsBase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_view);
        medsBase = new MedsBase(getApplicationContext());
        initWidgets();
    }

    private void initWidgets()
    {
        monthDayText = findViewById(R.id.monthDayText);
        dayOfWeekTV = findViewById(R.id.dayOfWeekTV);
        hourListView = findViewById(R.id.hourListView);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setDayView();
    }

    private void setDayView()
    {
        monthDayText.setText(CalendarUtils.monthDayFromDate(selectedDate));
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeekTV.setText(dayOfWeek);
        setHourAdapter();
    }

    private void setHourAdapter()
    {
        HourAdapter hourAdapter = new HourAdapter(getApplicationContext(), hourEventList());
        hourListView.setAdapter(hourAdapter);
    }

    private ArrayList<HourEventHolder> hourEventList()
    {
        ArrayList<HourEventHolder> list = new ArrayList<>();
        for(int hour = 0; hour < 24; hour++)
        {
            LocalTime time = LocalTime.of(hour, 0);
            ArrayList<Med> meds = medsBase.readFromBaseForWeekView(selectedDate);
            int finalHour = hour;
            meds = meds.stream()
                    .filter(x->x.getTime().getHour() == finalHour)
                    .collect(Collectors.toCollection(ArrayList::new));
            HourEventHolder hourEvent = new HourEventHolder(time, meds);
            list.add(hourEvent);
        }
        return list;
    }

    public void previousDayAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1);
        setDayView();
    }

    public void nextDayAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusDays(1);
        setDayView();
    }

    public void newEventAction(View view)
    {
        startActivity(new Intent(this, EventEditActivity.class));
    }
}