package com.davicaetano.runningwithmusic.data.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by davi on 11/10/15.
 */
public class RWMProvider extends ContentProvider{
    private static final UriMatcher mUriMatcher = buildUriMatcher();
    private RWMDbHelper mRWMDbHelper;

    static final int SONG = 1;
    static final int SONG_ID = 2;
    static final int SESSION = 3;
    static final int SESSION_ID = 4;
    static final int STEP = 5;
    static final int STEP_ID = 6;

    @Override
    public boolean onCreate()
    {
        mRWMDbHelper = new RWMDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        Cursor c = null;
        int match = mUriMatcher.match(uri);
        if(match == SONG)
            return mRWMDbHelper.getReadableDatabase().query(RWMContract.Songs.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
        if(match == SONG_ID)
            return mRWMDbHelper.getReadableDatabase().query(RWMContract.Songs.TABLE_NAME,
                        projection,
                        RWMContract.Songs._ID + " = ? ",
                        new String[]{RWMContract.Songs.getIDFromUri(uri)},
                        null,
                        null,
                        sortOrder);
        if(match == SESSION)
            return mRWMDbHelper.getReadableDatabase().query(RWMContract.Sessions.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
        if(match == SESSION_ID)
            return null;
        //TODO: code it.
        if(match == STEP)
            return mRWMDbHelper.getReadableDatabase().query(RWMContract.Steps.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
        if(match == STEP_ID)
            return null;
        //TODO: code it.

        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri)
    {
        int match = mUriMatcher.match(uri);
        if(match == SONG) return RWMContract.Songs.CONTENT_TYPE;
        if(match == SONG_ID) return RWMContract.Songs.CONTENT_ITEM_TYPE;
        if(match == SESSION) return RWMContract.Sessions.CONTENT_TYPE;
        if(match == SESSION_ID) return RWMContract.Sessions.CONTENT_ITEM_TYPE;
        if(match == STEP) return RWMContract.Steps.CONTENT_TYPE;
        if(match == STEP_ID) return RWMContract.Steps.CONTENT_ITEM_TYPE;

        return "";
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        final SQLiteDatabase db = mRWMDbHelper.getWritableDatabase();
        Uri returnUri = null;

        int match = mUriMatcher.match(uri);
        if(match == SONG){
            long id = db.insert(RWMContract.Songs.TABLE_NAME,null,values);
            returnUri = RWMContract.Songs.buildSongUri(id);
        }else
        if(match == SESSION){
            long id = db.insert(RWMContract.Sessions.TABLE_NAME, null, values);
            returnUri = RWMContract.Sessions.buildSessionUri(id);
        }
        if(match == STEP){
            long id = db.insert(RWMContract.Steps.TABLE_NAME, null, values);
            returnUri = RWMContract.Steps.buildSessionUri(id);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        final SQLiteDatabase db = mRWMDbHelper.getWritableDatabase();
        int nro = 0;
        switch (mUriMatcher.match(uri)){
            case SONG:{
                nro = db.update(RWMContract.Songs.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case SESSION:{
                nro = db.update(RWMContract.Sessions.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return nro;
    }

    static UriMatcher buildUriMatcher(){
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RWMContract.CONTENT_AUTHORITY;
        uriMatcher.addURI(authority, RWMContract.Songs.TABLE_NAME, SONG);
        uriMatcher.addURI(authority, RWMContract.Songs.TABLE_NAME + "/#", SONG_ID);
        uriMatcher.addURI(authority, RWMContract.Sessions.TABLE_NAME, SESSION);
        uriMatcher.addURI(authority, RWMContract.Sessions.TABLE_NAME + "/#", SESSION_ID);
        uriMatcher.addURI(authority, RWMContract.Steps.TABLE_NAME, STEP);
        uriMatcher.addURI(authority, RWMContract.Steps.TABLE_NAME + "/#", STEP_ID);
        return uriMatcher;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values)
    {
        return super.bulkInsert(uri, values);
        //TODO: Fazer o bulk pra quando abrir o splash adicionar todas as musicas do celular.
    }
}
