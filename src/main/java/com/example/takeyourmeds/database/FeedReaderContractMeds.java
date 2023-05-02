package com.example.takeyourmeds.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class FeedReaderContractMeds
{

    public static class FeedEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "meds";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DOSE = "dose";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TIME = "time";
    }

    public static class FeedReaderDbHelper extends SQLiteOpenHelper
    {

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "FeedReader.db";

        public FeedReaderDbHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { }

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                        FeedEntry.COLUMN_NAME_NAME + " TEXT, " +
                        FeedEntry.COLUMN_NAME_DOSE + " TEXT," +
                        FeedEntry.COLUMN_NAME_DATE +   " TEXT,"    +
                        FeedEntry.COLUMN_NAME_TIME + " TEXT, PRIMARY KEY("+
                        FeedEntry.COLUMN_NAME_NAME + "," + FeedEntry.COLUMN_NAME_DATE +
                        "," + FeedEntry.COLUMN_NAME_TIME+"));";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
    }
}
