package com.example.dungbong.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


public class PlanDBHelper extends SQLiteOpenHelper {

    public static final class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "plantable" ;
        public static final String COLUMN_PLAN_NAME = "name";
        public static final String COLUMN_PLAN_YEAR = "year";
        public static final String COLUMN_PLAN_MONTH = "month";
        public static final String COLUMN_PLAN_DAY = "day";
        public static final String COLUMN_PLAN_CLEARFLAG = "clearflag";
    }
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + "(" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FeedEntry.COLUMN_PLAN_NAME + " TEXT,"+
                    FeedEntry.COLUMN_PLAN_YEAR + " INTEGER,"+
                    FeedEntry.COLUMN_PLAN_MONTH + " INTEGER,"+
                    FeedEntry.COLUMN_PLAN_DAY + " INTEGER," +
                    FeedEntry.COLUMN_PLAN_CLEARFLAG + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE " + FeedEntry.TABLE_NAME;

    public static int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "FeedReader.db";

    public PlanDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
