package com.davicaetano.runningwithmusic.ui.presenter;

import com.davicaetano.runningwithmusic.data.model.PlayerStatus;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by davicaetano on 12/10/15.
 */
public interface PlayerActivityPresenter {
    void startSession();

    void onRefreshed(PlayerStatus playerStatus);
    void onRefreshedTime(int time);


    void play();
    void next();
    void prev();

    void onResume();
    void onPause();

    void refreshTimer();

    void addStep(LatLng latLng);

    void setMap(GoogleMap googleMap);

    void seekTo(int progress);

    void finishSession();

    void close();
}
