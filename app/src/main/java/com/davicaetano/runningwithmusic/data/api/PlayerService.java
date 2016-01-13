package com.davicaetano.runningwithmusic.data.api;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.davicaetano.runningwithmusic.CustomApplication;
import com.davicaetano.runningwithmusic.data.api.components.PlayerServiceComponent;
import com.davicaetano.runningwithmusic.data.api.modules.PlayerServiceModule;
import com.davicaetano.runningwithmusic.data.database.RWMContract;

import java.text.SimpleDateFormat;

import javax.inject.Inject;


public class PlayerService extends Service {
    @Inject
    PlayerServiceComponent playerServiceComponent;

    @Inject
    NotificationAPI notificationAPI;

    @Inject
    LocationAPI locationAPI;

    @Inject
    Context context;

    private long timeStarted;
    protected LocalBroadcastManager broadcastManager;
    private MediaPlayer mp;
    private long sessionId;
    private long songId;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    private Cursor cursor;
    private long sessionTime;

    @Override
    public void onCreate() {
        super.onCreate();
        ((CustomApplication) getApplication()).getAppContextComponent()
                .plus(new PlayerServiceModule(this))
                .inject(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.v("davi", "Serivce - onDestroy");
        notificationAPI.stopNotif();
        mp.stop();
        mp.release();
        locationAPI.finishSession(0L);
        Intent intent1 = new Intent(getApplicationContext(),WidgetProvider.class);
        intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), WidgetProvider.class));
        intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getApplicationContext().sendBroadcast(intent1);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.v("davi", "PlayerService - onStartCommand - " + intent.getAction());
        int out = super.onStartCommand(intent, flags, startId);
        if(broadcastManager == null){
            broadcastManager = LocalBroadcastManager.getInstance(this);
        }

        if(intent != null) {
            String action = intent.getAction();
            if(action.equals(PlayerAPIImpl.START)) {
                sessionTime = intent.getLongExtra("TIME",10L);
                start();
            }

            else if (action.equals(PlayerAPIImpl.PLAY)) {
                play();
            }

            else if (action.equals(PlayerAPIImpl.FINISH_SESSION)) {
                finishSession();

            }

            else if (action.equals(PlayerAPIImpl.SEEKTO)) {
                if (intent.hasExtra(PlayerAPIImpl.POS)) {
                    if(mp.getCurrentPosition() != intent.getIntExtra(PlayerAPIImpl.POS, 0))
                        seekTo(intent.getIntExtra(PlayerAPIImpl.POS, 0));
                }
            }

            else if(action.equals((PlayerAPIImpl.REFRESH))){
                refresh();
            }
            else if(action.equals((PlayerAPIImpl.REFRESH_TIME))){
                refreshTime();
            }
            else if(action.equals((PlayerAPIImpl.NEXT))){
                next();
            }
            else if(action.equals((PlayerAPIImpl.PREV))){
                prev();
            }
        }
        return out;
    }

    private void finishSession() {
        Log.v("davi", "Service - finish session");
        if(mp != null) {
            mp.pause();
        }
        locationAPI.finishSession(sessionId);
        Intent intent = new Intent(PlayerAPIImpl.CLOSE_PLAYER);
        broadcastManager.sendBroadcast(intent);
    }

    private void start() {
        if(mp != null) {
            mp.stop();
            mp.reset();
        }
        mp = new MediaPlayer();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                locationAPI.refreshRate(sessionId, songId);
                next();
            }
        });
        cursor = this.getContentResolver().query(RWMContract.Songs.CONTENT_URI,
                null,
                null,
                null,
                RWMContract.Songs.COLUMN_RATE + " DESC");
        sessionId = startNewSession();
        tryPlay();
    }

    private void tryPlay(){
        boolean run;
        if(cursor.isBeforeFirst()){
            run = cursor.moveToFirst();
        }else{
            run = true;
        }
        if(run) {
            String path = cursor.getString(cursor.getColumnIndex(RWMContract.Songs.COLUMN_FILE));
            try {
                mp.reset();
                mp.setDataSource(path);
                mp.prepare();
                mp.start();
                songId = cursor.getLong(cursor.getColumnIndex(RWMContract.Songs._ID));
                if (cursor.getString(cursor.getColumnIndex(RWMContract.Songs.COLUMN_IMAGE)) == null) {
                    SpotifyAsyncTask sat = new SpotifyAsyncTask(this);
                    sat.execute(new String[]{cursor.getString(cursor.getColumnIndex(RWMContract.Songs.COLUMN_ARTIST))});
                }
                refresh();
            } catch (Exception e) {
                tryPlay();
            }
        }
    }

    private long startNewSession() {
        ContentValues cv = new ContentValues();
        timeStarted = now();
        cv.put(RWMContract.Sessions.COLUMN_TIME1, timeStarted);
        Uri uri = getContentResolver().insert(RWMContract.Sessions.CONTENT_URI,
                cv);
        long sessionId = RWMContract.Sessions.getSessionId(uri);
        if(locationAPI.isConnected()) {
            locationAPI.startLocation();
        }
        return sessionId;
    }

    public void play(){
        if(mp.isPlaying()){
            mp.pause();
            locationAPI.stopLocation();
        }else {
            mp.start();
            locationAPI.startLocation();
        }
        refresh();
    }

    private void prev() {
        cursor.moveToPrevious();
        if(cursor.isBeforeFirst()) {
            cursor.moveToLast();
        }
        tryPlay();
        refresh();
    }

    private void next() {
        if(now() - timeStarted > sessionTime *60 *1000){
            locationAPI.finishSession(sessionId);
        }else {
            cursor.moveToNext();
            if (cursor.isAfterLast()) {
                cursor.moveToFirst();
            }
            tryPlay();
            refresh();
        }
    }

    public void seekTo(int progress) {
        mp.seekTo(progress);
        refreshTime();
    }

    private void refresh(){
        try {

            Intent intent = new Intent(PlayerAPIImpl.REFRESH);
            intent.putExtra("name", cursor.getString(cursor.getColumnIndex(RWMContract.Songs.COLUMN_SONG_NAME)));
            intent.putExtra("artist", cursor.getString(cursor.getColumnIndex(RWMContract.Songs.COLUMN_ARTIST)));
            intent.putExtra("album", cursor.getString(cursor.getColumnIndex(RWMContract.Songs.COLUMN_ALBUM_NAME)));
            intent.putExtra("duration", mp.getDuration());
            intent.putExtra("IsPlaying", mp.isPlaying());
            intent.putExtra("time", mp.getCurrentPosition());
            broadcastManager.sendBroadcast(intent);

            notificationAPI.notif();

            widget();

        }catch (Exception e){}
    }

    private void refreshTime(){
        Intent intent = new Intent(PlayerAPIImpl.REFRESH_TIME);
        if(mp != null){
            intent.putExtra("time", mp.getCurrentPosition());
        }else{
            intent.putExtra("time", 0);
        }

        broadcastManager.sendBroadcast(intent);
    }

    public boolean isPlaying() {
        if(mp != null) {
            return mp.isPlaying();
        }
        return false;
    }

    public CharSequence getTrackName() {
        return cursor.getString(cursor.getColumnIndex(RWMContract.Songs.COLUMN_SONG_NAME));
    }

    public CharSequence getArtistName() {
        return cursor.getString(cursor.getColumnIndex(RWMContract.Songs.COLUMN_ARTIST));
    }

    public String getImage() {
        return cursor.getString(cursor.getColumnIndex(RWMContract.Songs.COLUMN_IMAGE));
    }

    public long getSessionId() {
        return sessionId;
    }

    public void recordImage(String[] args) {
        ContentValues cv = new ContentValues();
        cv.put(RWMContract.Songs.COLUMN_IMAGE, args[1]);
        getContentResolver().update(RWMContract.Songs.CONTENT_URI,
                cv,
                RWMContract.Songs.COLUMN_ARTIST + " = ? ",
                new String[]{args[0]}
        );
        refresh();
    }

    public long getSongId() {
        return songId;
    }

    public long now() {
        long a = System.currentTimeMillis();
        return a;
    }

    private void widget(){
        Intent intent1 = new Intent(getApplicationContext(),WidgetProvider.class);
        intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent1.putExtra("ON_SESSION", "ON_SESSION");
        intent1.putExtra("TRACK_NAME",getTrackName());
        intent1.putExtra("ARTIST_NAME",getArtistName());
        intent1.putExtra("IMAGE_FILE",getImage());
        intent1.putExtra("IS_PLAYING",isPlaying());
        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), WidgetProvider.class));
        intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getApplicationContext().sendBroadcast(intent1);
    }
}