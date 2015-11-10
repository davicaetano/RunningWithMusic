package com.davicaetano.runningwithmusic.data;

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
                RWMContract.Songs._ID + " INTEGER PRIMARY KEY" +
                RWMContract.Songs.COLUMN_ALBUM_NAME + " TEXT NOT NULL" +
                RWMContract.Songs.COLUMN_ARTIST + " TEXT NOT NULL" +
                RWMContract.Songs.COLUMN_SONG_NAME + " TEXT NOT NULL" +
                ")";

        db.execSQL(SQL_CREATE_SONG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + RWMContract.Songs.TABLE_NAME);
        onCreate(db);
    }
}
