package com.davicaetano.runningwithmusic.ui.presenter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;

import com.davicaetano.runningwithmusic.data.api.PlayerAPI;
import com.davicaetano.runningwithmusic.data.model.PlayerStatus;
import com.davicaetano.runningwithmusic.ui.view.PlayerView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by davicaetano on 12/9/15.
 */
public class PlayerActivityPresenterImpl implements PlayerActivityPresenter {

    private List<LatLng> latLngs = new ArrayList<>();
    PolylineOptions track = new PolylineOptions().width(5).color(Color.BLUE);
    private GoogleMap map;
    public PlayerView playerView;
    public PlayerAPI playerAPI;
    public Context context;
    private Handler handler = new Handler();
    boolean isPlaying;
    private Timer timer;
    private class timerTask extends TimerTask{
        @Override
        public void run() {
            if(isPlaying) {
                refreshTimer();
            }
        }
    };

    public PlayerActivityPresenterImpl(PlayerView playerView, PlayerAPI playerAPI, Context context){
//        Log.v("davi", "PlayerActivityPresenterImpl - PlayerActivityPresenterImpl");
        this.playerView = playerView;
        this.playerAPI = playerAPI;
        this.context = context;
        playerAPI.setCallback(this);
    }

    @Override
    public void startSession() {
//        Log.v("davi", "PlayerActivityPresenterImpl - onCreate");
        playerAPI.startSession();

    }

    @Override
    public void onResume() {
//        Log.v("davi", "PlayerActivityPresenterImpl - onResume");
        playerAPI.refresh();
        if(timer != null)
            timer.cancel();
        timer = new Timer();
        timer.schedule(new timerTask(), 0, 1000);
    }

    @Override
    public void onPause() {
//        Log.v("davi", "PlayerActivityPresenterImpl - onPause");
        timer.cancel();
        timer.purge();
    }

    @Override
    public void play() {
//        Log.v("davi", "PlayerActivityPresenterImpl - play");
        playerAPI.play();
    }

    @Override
    public void next() {
//        Log.v("davi", "PlayerActivityPresenterImpl - next");
        playerAPI.next();
    }

    @Override
    public void prev() {
//        Log.v("davi", "PlayerActivityPresenterImpl - prev");
        playerAPI.prev();
    }

    @Override
    public void refreshTimer() {
//        Log.v("davi", "PlayerActivityPresenterImpl - refreshTimer");
        playerAPI.refreshTime();
    }

    @Override
    public void seekTo(int progress) {
        playerAPI.seekTo(progress);
    }

    @Override
    public void finishSession() {
        playerAPI.finishSession();
    }

    @Override
    public void onRefreshed(PlayerStatus playerStatus) {
//        Log.v("davi", "PlayerActivityPresenterImpl - onRefreshed");
        playerView.setAlbum(playerStatus.song.album);
        playerView.setArtist(playerStatus.song.artist);
        playerView.setSong(playerStatus.song.name);
        playerView.setPlayButton(playerStatus.isPlaying);
        playerView.setSeekToMax(playerStatus.song.duration);
        playerView.setSeekToProgress(playerStatus.time);
        playerView.setTime1(setTime(playerStatus.time));
        playerView.setTime2(setTime(playerStatus.song.duration));
        isPlaying = playerStatus.isPlaying;
    }

    @Override
    public void onRefreshedTime(int time) {
        playerView.setTime1(setTime(time));
        playerView.setSeekToProgress(time);
    }

    @Override
    public void addStep(LatLng latLng) {
        latLngs.add(latLng);
        if(latLngs.size() == 1) {
            CameraPosition cameraPosition = CameraPosition.builder().target(latLng).zoom(15).build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        track.add(latLng);

        map.addPolyline(track);
        CameraPosition cameraPosition = CameraPosition.builder().target(latLng).zoom(15).build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void setMap(GoogleMap googleMap) {
        map = googleMap;
    }

    public static String setTime(int time) {
        BigDecimal duration1 = new BigDecimal(time / 1000);
        BigDecimal minutes1 = duration1.divide(new BigDecimal("60"), 1);
        BigDecimal seconds1 = duration1.remainder(new BigDecimal("60"));
        if(minutes1.intValue() < 1000) {
            return (minutes1.toString() + ":" + String.format("%02d",seconds1.intValue()));
        }else{
            return ("0:00");
        }
    }

    @Override
    public void close() {
        Log.v("davi","Presenter - close");
        playerView.close();
    }
}
