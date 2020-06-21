package com.example.howmanydaysi.dataDase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME="eventsDataBase";
    public static final String TABLE_EVENTS="events";

    public static final String FIELD_ID = "id";
    public static final String FIELD_EVENT ="event";
    public static final String FIELD_RECORD_QUANTITY="record_quantity";
    public static final String FIELD_ICON="icon";
    public static final String FIELD_CURRENT_QUANTITY="current_quantity";
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_EVENTS + "(" +
                FIELD_ID + " integer primary key," +
                FIELD_EVENT+" text,"+
                FIELD_ICON+ " integer," +
                FIELD_CURRENT_QUANTITY+" integer,"+
                FIELD_RECORD_QUANTITY+" integer"+ ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_EVENTS);
        onCreate(db);

    }
}

