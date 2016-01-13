package com.davicaetano.runningwithmusic.data.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class RWMContract {
    public static final String CONTENT_AUTHORITY = "com.davicaetano.runningwithmusic";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //The table below will have all the information about the songs.
    //Each row is created when the program starts and check the music content provider.
    //There will be information about the statistics for each song when the user runs
    //This information will be used to define the next song.
    public static final class Songs implements BaseColumns{

        public static final String TABLE_NAME = "songs";
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + TABLE_NAME;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String COLUMN_ARTIST = "artist";
        public static final String COLUMN_SONG_NAME = "song_name";
        public static final String COLUMN_ALBUM_NAME = "album_name";
        public static final String COLUMN_FILE = "file";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_RATE = "rate";

        public static String getIDFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }

        public static Uri buildSongUri(long id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
        }
    }

    //When the person starts a new running, a row will be created here.
    //For every running there will be a variable number of pieces.
    //for that reason the pieces will be in a different table
    public static final class Sessions implements BaseColumns{

        public static final String TABLE_NAME = "runnings";
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + TABLE_NAME;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String COLUMN_TIME1 = "time1";
        public static final String COLUMN_TIME2 = "time2";
        public static final String COLUMN_SPACE = "space";
        public static final String COLUMN_SPEED = "speed";

        public static long getSessionId(Uri uri){
            return Long.parseLong(uri.getPathSegments().get(1));
        }

        public static Uri buildSessionUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    //the pieces will be in this table
    //each runnning will have lots if pieces.
    public static final class Steps implements  BaseColumns{

        public static final String TABLE_NAME = "pieces";
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String COLUMN_RUNNING_ID = "running_id";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_SPEED = "speed";
        public static final String COLUMN_SPACE = "space";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_ALTITUDE = "altitude";
        public static final String COLUMN_SONG_ID = "song_id";

        public long getSessionId(Uri uri){
            return Long.parseLong(uri.getPathSegments().get(1));
        }

        public static Uri buildSessionUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }
}
