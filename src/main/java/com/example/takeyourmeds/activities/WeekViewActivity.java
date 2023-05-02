package com.example.takeyourmeds.activities;

import static com.example.takeyourmeds.utils.CalendarUtils.daysInWeekArray;
import static com.example.takeyourmeds.utils.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.takeyourmeds.R;
import com.example.takeyourmeds.adapters.CalendarAdapter;
import com.example.takeyourmeds.adapters.EventAdapter;
import com.example.takeyourmeds.database.MedsBase;
import com.example.takeyourmeds.utils.CalendarUtils;
import com.example.takeyourmeds.utils.Med;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView medListView;
    private MedsBase medsBase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        medsBase = new MedsBase(getApplicationContext());
        initWidgets();
        setWeekView();
    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        medListView = findViewById(R.id.medListView);
    }

    private void setWeekView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();
    }

    public void previousWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date)
    {
        if(date != null)
        {
            CalendarUtils.selectedDate = date;
            setWeekView();
            goToDay();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setEventAdapter();
    }

    private void setEventAdapter()
    {
        ArrayList<Med> weeklyMeds = new ArrayList<>();
                daysInWeekArray(CalendarUtils.selectedDate)
                .forEach(day -> medsBase.readFromBaseForWeekView(day).forEach(med -> weeklyMeds.add(med)));
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), weeklyMeds);
        medListView.setAdapter(eventAdapter);
    }

    public void newEventAction(View view)
    {
        startActivity(new Intent(this, EventEditActivity.class));
    }

    public void dailyAction(View view)
    {
        startActivity(new Intent(this, DayViewActivity.class));
    }

    public void goToDay() {startActivity(new Intent(this, DayViewActivity.class));}

}