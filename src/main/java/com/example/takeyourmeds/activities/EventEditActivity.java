package com.example.takeyourmeds.activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.takeyourmeds.R;
import com.example.takeyourmeds.database.MedsBase;
import com.example.takeyourmeds.utils.AlarmReceiver;
import com.example.takeyourmeds.utils.CalendarUtils;
import com.example.takeyourmeds.utils.Med;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventEditActivity extends AppCompatActivity
{
    private EditText MedNameEdit;
    private EditText DoseNameEdit;
    private TextView MedDateTV, MedHourTV;
    private LocalTime time;
    private Button timeButton, addButton,removeButton;
    int hour, minute;
    private MedsBase medsBase;
    private Med selectedMed;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        initListeners();
        createNotificationChannel();
        time = LocalTime.now();
        MedDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        timeButton = findViewById(R.id.timeButton);
        addButton = findViewById(R.id.addButton);
        setAddButtonEnabled(false);
        medsBase = new MedsBase(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            selectedMed = getIntent().getParcelableExtra("selectedMed");
            MedNameEdit.setText(selectedMed.getName());
            DoseNameEdit.setText(selectedMed.getDose());
            CalendarUtils.formattedShortTime(selectedMed.getTime());
            timeButton.setText(CalendarUtils.formattedShortTime(selectedMed.getTime()));
            addButton.setText("UPDATE");
            setAddButtonEnabled(true);
            System.out.println("selected med - " + selectedMed.toString());
        }
        else
        {
            removeButton.setEnabled(false);
            removeButton.setVisibility(View.INVISIBLE);
        }
    }

    private void initListeners()
    {
        MedNameEdit.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable)
            {
                setAddButtonEnabled(areAllConditionsMet());
            }
        });

        DoseNameEdit.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable)
            {
                setAddButtonEnabled(areAllConditionsMet());
            }
        });

        removeButton.setOnClickListener(view ->
        {
            medsBase.removeFromBase(selectedMed.getName(),selectedMed.getDate(),selectedMed.getTime());
            finish();
        });
    }

    private void initWidgets()
    {
        MedNameEdit = findViewById(R.id.MedNameEdit);
        DoseNameEdit = findViewById(R.id.DoseNameEdit);
        MedDateTV = findViewById(R.id.MedDateTV);
        MedHourTV = findViewById(R.id.MedHourTV);
        removeButton = findViewById(R.id.removeButton);
    }

    public void saveEventAction(View view)
    {
        String medName = MedNameEdit.getText().toString();
        String medDose = DoseNameEdit.getText().toString();
        time = LocalTime.of(hour, minute);

        if(selectedMed!=null){
            Med newMed = new Med(medName,medDose,CalendarUtils.selectedDate,time);
            medsBase.updateMed(selectedMed,newMed);
        }else{
            medsBase.addToBase(medName, medDose, CalendarUtils.selectedDate, time);
        }
        Toast.makeText(this, "Alarm set succesfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean areAllConditionsMet()
    {
        return !MedNameEdit.getText().toString().equals("")
                && !DoseNameEdit.getText().toString().equals("")
                && !timeButton.getText().toString().equals("Select time");
    }

   public void popTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            hour = selectedHour;
            minute = selectedMinute;
            timeButton.setText(String.format(Locale.getDefault(),"%02d:%02d",hour,minute));
            setAddButtonEnabled(areAllConditionsMet());

            int hourToSet = hour;
            int minuteToSet = minute;

            Date date = Date.from(CalendarUtils.selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, hourToSet);
            calendar.set(Calendar.MINUTE, minuteToSet);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            long alarmTime = calendar.getTimeInMillis();

            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this,0, intent, 0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);

        };

        int style = AlertDialog.THEME_HOLO_DARK;

        final Calendar c = Calendar.getInstance();
        int defaultHour = c.get(Calendar.HOUR_OF_DAY);
        int defaultMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, defaultHour, defaultMinute, true);

        timePickerDialog.setTitle("Select time");
        timePickerDialog.show();
    }

    private void setAddButtonEnabled(boolean state)
    {
        addButton.setEnabled(state);

        if(state)
        {
            addButton.setTextColor(Color.BLUE);
        }
        else
        {
            addButton.setTextColor(Color.GRAY);
        }
    }

    private void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "takeyourmedsReminderChannel";
            String description = "Channel for alarm manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("takeyourmeds", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}