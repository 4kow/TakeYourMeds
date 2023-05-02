package com.example.takeyourmeds.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.takeyourmeds.R;
import com.example.takeyourmeds.database.MedsBase;
import com.example.takeyourmeds.utils.CalendarUtils;
import com.example.takeyourmeds.utils.Med;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Med>
{
    MedsBase medsBase;

    public EventAdapter(@NonNull Context context, List<Med> meds)
    {
        super(context, 0, meds);
        medsBase = new MedsBase(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Med med = getItem(position);

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);

        TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);

        String medName = med.getName() +" | "+ med.getDose() +" | "+ CalendarUtils.formattedDate(med.getDate()) +", "+ CalendarUtils.formattedTime(med.getTime());
        eventCellTV.setText(medName);

        return convertView;
    }

}
