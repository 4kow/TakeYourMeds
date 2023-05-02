package com.example.takeyourmeds.utils;

import java.time.LocalTime;
import java.util.ArrayList;

public class HourEventHolder
{
    public LocalTime time;
    public ArrayList<Med> meds;

    public HourEventHolder(LocalTime time, ArrayList<Med> meds)
    {
        this.time = time;
        this.meds = meds;
    }

    public LocalTime getTime()

    {
        return time;
    }

    public void setTime(LocalTime time)
    {
        this.time = time;
    }

    public ArrayList<Med> getMeds()
    {
        return meds;
    }

    public void setMeds(ArrayList<Med> meds)
    {
        this.meds = meds;
    }
}
