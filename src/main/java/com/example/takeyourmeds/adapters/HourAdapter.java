package com.example.takeyourmeds.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.takeyourmeds.R;
import com.example.takeyourmeds.activities.EventEditActivity;
import com.example.takeyourmeds.database.MedsBase;
import com.example.takeyourmeds.utils.CalendarUtils;
import com.example.takeyourmeds.utils.HourEventHolder;
import com.example.takeyourmeds.utils.Med;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HourAdapter extends ArrayAdapter<HourEventHolder>
{
    MedsBase medsBase;

    public HourAdapter(@NonNull Context context, List<HourEventHolder> meds)
    {
        super(context, 0, meds);
        medsBase = new MedsBase(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        HourEventHolder med = getItem(position);

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hour_cell, parent, false);

        setHour(convertView, med.time);
        setMeds(convertView, med.meds);

        return convertView;
    }

    private void setHour(View convertView, LocalTime time)
    {
        TextView timeTV = convertView.findViewById(R.id.timeTV);
        timeTV.setText(CalendarUtils.formattedShortTime(time));
    }

    private void setMeds(View convertView, ArrayList<Med> meds)
    {
        TextView med1 = convertView.findViewById(R.id.med1);
        TextView med2 = convertView.findViewById(R.id.med2);
        TextView med3 = convertView.findViewById(R.id.med3);

        switch (meds.size()){

            case 3:
            med3.setOnClickListener(getOnclickListener(meds.get(2)));
            case 2:
            med2.setOnClickListener(getOnclickListener(meds.get(1)));
            case 1:
            med1.setOnClickListener(getOnclickListener(meds.get(0)));
            default:
                break;
        }

        if(meds.size() == 0)
        {
            hideMed(med1);
            hideMed(med2);
            hideMed(med3);
        }

        else if(meds.size() == 1)
        {
            setMed(med1, meds.get(0));
            hideMed(med2);
            hideMed(med3);
        }

        else if(meds.size() == 2)
        {
            setMed(med1, meds.get(0));
            setMed(med2, meds.get(1));
            hideMed(med3);
        }
        else if(meds.size() == 3)
        {
            setMed(med1, meds.get(0));
            setMed(med2, meds.get(1));
            setMed(med3, meds.get(2));
        }
        else
        {
            setMed(med1, meds.get(0));
            setMed(med2, meds.get(1));
            med3.setVisibility(View.VISIBLE);
            String medsNotShown = String.valueOf(meds.size() - 2);
            medsNotShown += " more meds";
            med3.setText(medsNotShown);
        }
    }

    private View.OnClickListener getOnclickListener(Med med)
    {
        if(med == null) return null;
        return view -> {
            Med selectedMed = medsBase.readSelectedMed(med);
            Intent intent = new Intent(getContext(), EventEditActivity.class);
            intent.putExtra("selectedMed", selectedMed);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        };
    }

    private void setMed(TextView textView, Med med)
    {
        textView.setText(med.getName());
        textView.setVisibility(View.VISIBLE);
    }

    private void hideMed(TextView tv)
    {
        tv.setVisibility(View.INVISIBLE);
    }
}
