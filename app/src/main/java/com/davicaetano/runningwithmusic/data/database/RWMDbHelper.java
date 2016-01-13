package com.davicaetano.runningwithmusic.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by davi on 11/10/15.
 */
public class RWMDbHelper extends SQLiteOpenHelper{
    private static final String DB_NAME = "RWM.db";
    private static final int DB_VERSION = 1;

    public RWMDbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String SQL_CREATE_SONG_TABLE = "CREATE TABLE " + RWMContract.Songs.TABLE_NAME + " (" +
                RWMContract.Songs._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RWMContract.Songs.COLUMN_ALBUM_NAME + " TEXT NOT NULL, " +
                RWMContract.Songs.COLUMN_ARTIST + " TEXT NOT NULL, " +
                RWMContract.Songs.COLUMN_SONG_NAME + " TEXT NOT NULL, " +
                RWMContract.Songs.COLUMN_FILE + " TEXT NOT NULL, " +
                RWMContract.Songs.COLUMN_IMAGE + " TEXT, " +
                RWMContract.Songs.COLUMN_DURATION + " INTEGER, " +
                RWMContract.Songs.COLUMN_RATE + " INTEGER, " +
                "UNIQUE(" + RWMContract.Songs.COLUMN_FILE +")" +
                ")";

        db.execSQL(SQL_CREATE_SONG_TABLE);

        final String SQL_CREATE_RUNNING_TABLE = "CREATE TABLE " + RWMContract.Sessions.TABLE_NAME + " (" +
                RWMContract.Sessions._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RWMContract.Sessions.COLUMN_TIME1 + " INTEGER NOT NULL, " +
                RWMContract.Sessions.COLUMN_TIME2 + " INTEGER , " +
                RWMContract.Sessions.COLUMN_SPACE + " INTEGER , " +
                RWMContract.Sessions.COLUMN_SPEED + " INTEGER " +
                ")";
        db.execSQL(SQL_CREATE_RUNNING_TABLE);

        final String SQL_CREATE_PIECES_TABLE = "CREATE TABLE " + RWMContract.Steps.TABLE_NAME + " (" +
                RWMContract.Steps._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RWMContract.Steps.COLUMN_RUNNING_ID + " INTEGER NOT NULL, "+
                RWMContract.Steps.COLUMN_ALTITUDE + " REAL, " +
                RWMContract.Steps.COLUMN_LATITUDE + " REAL, " +
                RWMContract.Steps.COLUMN_LONGITUDE + " REAL, " +
                RWMContract.Steps.COLUMN_SPEED + " REAL, " +
                RWMContract.Steps.COLUMN_SPACE + " REAL, " +
                RWMContract.Steps.COLUMN_TIME + " INTEGER NOT NULL, " +
                RWMContract.Steps.COLUMN_SONG_ID + " INTEGER) ";
        db.execSQL(SQL_CREATE_PIECES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + RWMContract.Songs.TABLE_NAME);
        onCreate(db);
    }
}
