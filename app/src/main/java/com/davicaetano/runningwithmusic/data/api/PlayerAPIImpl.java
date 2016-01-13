package com.davicaetano.runningwithmusic.data.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.davicaetano.runningwithmusic.data.model.PlayerStatus;
import com.davicaetano.runningwithmusic.data.model.Song;
import com.davicaetano.runningwithmusic.ui.presenter.PlayerActivityPresenter;
import com.google.android.gms.maps.model.LatLng;

public class PlayerAPIImpl implements PlayerAPI {

    public static final String START = "START";
    public static final String PLAY = "PLAY";
    public static final String NEXT = "NEXT";
    public static final String PREV = "PREV";
    public static final String POS = "POS";
    public static final String SEEKTO = "SEEKTO";
    public static final String REFRESH = "REFRESH";
    public static final String REFRESH_TIME = "REFRESH_TIME";
    public static final String STEP = "STEP";
    public static final String LOCATION = "LOCATION";
    public static final String FINISH_SESSION = "FINISH_SESSION";
    public static final String CLOSE_PLAYER = "CLOSE_PLAYER";


    private Uri song;
    private PlayerActivityPresenter playerActivityPresenter;
    private Context context;

    private LocalBroadcastManager broadcastManager;
    IntentFilter intentFilter;
    PlayerReceiver playerReceiver;

    public PlayerAPIImpl(Context context){
        this.context = context;

        broadcastManager = LocalBroadcastManager.getInstance(context);
        playerReceiver = new PlayerReceiver();//Inner class

        intentFilter = new IntentFilter();
        intentFilter.addAction(REFRESH);
        intentFilter.addAction(REFRESH_TIME);
        intentFilter.addAction(STEP);
        intentFilter.addAction(CLOSE_PLAYER);
    }

    public void setCallback(PlayerActivityPresenter playerActivityPresenter){
        this.playerActivityPresenter = playerActivityPresenter;
        if(playerActivityPresenter != null) {
            broadcastManager.registerReceiver(playerReceiver, intentFilter);
        }else{
            broadcastManager.unregisterReceiver(playerReceiver);
        }
    }

    @Override
    public void refresh(){
//        Log.v("davi", "PlayerAPIImpl - refresh");
        callService(REFRESH);
    }

    @Override
    public void refreshTime() {
//        Log.v("davi", "PlayerAPIImpl - refreshTime");
        callService(REFRESH_TIME);
    }

    @Override
    public void startSession() {
//        Log.v("davi", "PlayerAPIImpl - start");
        callService(START);
    }

    @Override
    public void play(){
//        Log.v("davi", "PlayerAPIImpl - play");
        callService(PLAY);
    }

    @Override
    public void next(){
//        Log.v("davi", "PlayerAPIImpl - next");
        callService(NEXT);
    }

    @Override
    public void prev(){
//        Log.v("davi", "PlayerAPIImpl - prev");
        callService(PREV);
    }

    @Override
    public void seekTo(int pos){
//        Log.v("davi", "PlayerAPIImpl - seekTo");
        Intent intent = new Intent(context, PlayerService.class);
        intent.setAction(SEEKTO);
        intent.putExtra(POS, pos);
        context.startService(intent);
        //playerActivityPresenter.getContext().getApplicationContext().startService(intent);
    }

    @Override
    public void finishSession() {
        callService(FINISH_SESSION);
    }

    private void callService(String action){
//        Log.v("davi", "PlayerAPIImpl - callService" + action.toString());
        Intent intent = new Intent(context, PlayerService.class);
        intent.setAction(action);
        context.startService(intent);
    }


    private class PlayerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.v("davi","PlayerAPIImpl - PlayerReceiver - onReceive" + action);

            if(action.equals(REFRESH)){
//                Log.v("davi", intent.getStringExtra("name"));
                Song song = new Song();
                song.name = intent.getStringExtra("name");
                song.artist = intent.getStringExtra("artist");
                song.album = intent.getStringExtra("album");
                song.duration = intent.getIntExtra("duration", 0);
                PlayerStatus playerStatus = new PlayerStatus();
                playerStatus.isPlaying = intent.getBooleanExtra("IsPlaying", false);
                playerStatus.time  = intent.getIntExtra("time", 0);
                playerStatus.song = song;
                playerActivityPresenter.onRefreshed(playerStatus);
            }else if(action.equals(REFRESH_TIME)){
                playerActivityPresenter.onRefreshedTime(intent.getIntExtra("time", 0));
            }
            else if(action.equals(STEP)){
                LatLng latLng = intent.getParcelableExtra(LOCATION);
                playerActivityPresenter.addStep(latLng);
            }
            else if(action.equals(CLOSE_PLAYER)){
                Log.v("davi", "PlayerReceiver - CLOSE");
                playerActivityPresenter.close();
            }

        }
    }

}
