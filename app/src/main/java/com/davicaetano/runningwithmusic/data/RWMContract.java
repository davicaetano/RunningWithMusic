package com.davicaetano.runningwithmusic.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class RWMContract {
    public static final String CONTENT_AUTHORITY = "com.davicaetano.runningwithmusic";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class Songs implements BaseColumns{

        public static final String TABLE_NAME = "songs";
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + TABLE_NAME;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath("").build();

        public static final String COLUMN_ARTIST = "artist";
        public static final String COLUMN_SONG_NAME = "song_name";
        public static final String COLUMN_ALBUM_NAME = "album_name";

    }
}
