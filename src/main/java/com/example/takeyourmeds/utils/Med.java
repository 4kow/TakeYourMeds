package com.example.takeyourmeds.utils;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class Med implements Parcelable
{

    protected Med(Parcel in)
    {
        name = in.readString();
        dose = in.readString();
        date = (LocalDate) in.readSerializable();
        time = (LocalTime) in.readSerializable();
    }

    public static final Creator<Med> CREATOR = new Creator<Med>()
    {
        @Override
        public Med createFromParcel(Parcel in) {
            return new Med(in);
        }

        @Override
        public Med[] newArray(int size) {
            return new Med[size];
        }
    };

    private String name;
    private String dose;
    private LocalDate date;
    private LocalTime time;

    public Med(String name, String dose, LocalDate date, LocalTime time)
    {
        this.name = name;
        this.dose = dose;
        this.date = date;
        this.time = time;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDose() { return dose; }

    public void setDose(String dose) { this.dose = dose; }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public LocalTime getTime() { return time; }

    public void setTime(LocalTime time)
    {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i)
    {
    parcel.writeString(name);
    parcel.writeString(dose);
    parcel.writeSerializable(date);
    parcel.writeSerializable(time);
    }

    @Override
    public String toString()
    {
        return "Med{" +
                "name='" + name + '\'' +
                ", dose='" + dose + '\'' +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}
