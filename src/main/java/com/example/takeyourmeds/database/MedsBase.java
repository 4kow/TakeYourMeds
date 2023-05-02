package com.example.takeyourmeds.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.takeyourmeds.utils.Med;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class MedsBase
{
    Context context;
    FeedReaderContractMeds.FeedReaderDbHelper dbHelper;
    SQLiteDatabase db;

    public MedsBase(Context context)
    {
        this.context=context;
        dbHelper = new FeedReaderContractMeds.FeedReaderDbHelper(this.context);
        db = dbHelper.getWritableDatabase();
    }
    public long addToBase(String name, String dose, LocalDate date, LocalTime time)
    {
        ContentValues values = new ContentValues();
        values.put(FeedReaderContractMeds.FeedEntry.COLUMN_NAME_NAME,name);
        values.put(FeedReaderContractMeds.FeedEntry.COLUMN_NAME_DOSE,dose);
        values.put(FeedReaderContractMeds.FeedEntry.COLUMN_NAME_TIME,time.toString());
        values.put(FeedReaderContractMeds.FeedEntry.COLUMN_NAME_DATE,date.toString());
        long newRowId = db.insert(FeedReaderContractMeds.FeedEntry.TABLE_NAME, null, values);
        return newRowId;

    }
    public void removeFromBase(String name,LocalDate date, LocalTime time)
    {
        String[] selectionArgs = {name,date.toString(),time.toString()};
        String whereClause = FeedReaderContractMeds.FeedEntry.COLUMN_NAME_NAME + " = ? AND " +
                FeedReaderContractMeds.FeedEntry.COLUMN_NAME_DATE + " = ? AND " +
                FeedReaderContractMeds.FeedEntry.COLUMN_NAME_TIME + " = ? ;";
        db.delete(FeedReaderContractMeds.FeedEntry.TABLE_NAME, whereClause, selectionArgs);
    }

    public ArrayList<Med> readFromBaseForWeekView(LocalDate date)
    {
        db = dbHelper.getReadableDatabase();
        String[] projection = {
                FeedReaderContractMeds.FeedEntry.COLUMN_NAME_NAME,
                FeedReaderContractMeds.FeedEntry.COLUMN_NAME_DOSE,
                FeedReaderContractMeds.FeedEntry.COLUMN_NAME_TIME,
                FeedReaderContractMeds.FeedEntry.COLUMN_NAME_DATE,
        };

        String selection = FeedReaderContractMeds.FeedEntry.COLUMN_NAME_DATE + " = ?";
        String[] selectionArgs = {date.toString()};
        Cursor cursor = db.query(
                FeedReaderContractMeds.FeedEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        ArrayList<Med> meds = new ArrayList<>();
        while(cursor.moveToNext())
        {
            Med med = new Med(
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContractMeds.FeedEntry.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContractMeds.FeedEntry.COLUMN_NAME_DOSE)),
                    convertStringToLocalDate(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContractMeds.FeedEntry.COLUMN_NAME_DATE))),
                    convertStringToLocalTime(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContractMeds.FeedEntry.COLUMN_NAME_TIME))));
            meds.add(med);
        }
        cursor.close();

        return meds;
    }
    private LocalDate convertStringToLocalDate(String dateString)
    {
        String[] splittedDate = dateString.split("-");
        return LocalDate.of(Integer.parseInt(splittedDate[0]),Integer.parseInt(splittedDate[1]),Integer.parseInt(splittedDate[2]));
    }
    private LocalTime convertStringToLocalTime(String timeString)
    {
        String[] splittedtime = timeString.split(":");
        return LocalTime.of(Integer.parseInt(splittedtime[0]),Integer.parseInt(splittedtime[1]));
    }
    public Med readSelectedMed(Med selectedMed)
    {
        db = dbHelper.getReadableDatabase();
        String[] projection = {
                FeedReaderContractMeds.FeedEntry.COLUMN_NAME_NAME,
                FeedReaderContractMeds.FeedEntry.COLUMN_NAME_DOSE,
                FeedReaderContractMeds.FeedEntry.COLUMN_NAME_TIME,
                FeedReaderContractMeds.FeedEntry.COLUMN_NAME_DATE,
        };

        String selection = FeedReaderContractMeds.FeedEntry.COLUMN_NAME_NAME + " = ? AND " +
                FeedReaderContractMeds.FeedEntry.COLUMN_NAME_DATE + " = ? AND " +
                FeedReaderContractMeds.FeedEntry.COLUMN_NAME_TIME + " = ? ;";
        String[] selectionArgs = {selectedMed.getName(),selectedMed.getDate().toString(),selectedMed.getTime().toString()};
        Cursor cursor = db.query(
                FeedReaderContractMeds.FeedEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        ArrayList<Med> meds = new ArrayList<>();
        while(cursor.moveToNext())
        {
            Med med = new Med(
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContractMeds.FeedEntry.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContractMeds.FeedEntry.COLUMN_NAME_DOSE)),
                    convertStringToLocalDate(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContractMeds.FeedEntry.COLUMN_NAME_DATE))),
                    convertStringToLocalTime(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContractMeds.FeedEntry.COLUMN_NAME_TIME))));
            meds.add(med);
        }
        cursor.close();

        return meds.get(0);
    }

    public void updateMed(Med selectedMed,Med newMed)
    {
        ContentValues values = new ContentValues();
        values.put(FeedReaderContractMeds.FeedEntry.COLUMN_NAME_NAME,newMed.getName());
        values.put(FeedReaderContractMeds.FeedEntry.COLUMN_NAME_DOSE,newMed.getDose());
        values.put(FeedReaderContractMeds.FeedEntry.COLUMN_NAME_TIME,newMed.getTime().toString());
        values.put(FeedReaderContractMeds.FeedEntry.COLUMN_NAME_DATE,newMed.getDate().toString());
        String whereClause = FeedReaderContractMeds.FeedEntry.COLUMN_NAME_NAME + " = ? AND " +
                FeedReaderContractMeds.FeedEntry.COLUMN_NAME_DATE + " = ? AND " +
                FeedReaderContractMeds.FeedEntry.COLUMN_NAME_TIME + " = ? ;";
        String[] whereArgs = {selectedMed.getName(),selectedMed.getDate().toString(),selectedMed.getTime().toString()};
        db.update(FeedReaderContractMeds.FeedEntry.TABLE_NAME,values,whereClause,whereArgs);
    }
}