package com.davicaetano.runningwithmusic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.davicaetano.runningwithmusic.CustomApplication;
import com.davicaetano.runningwithmusic.R;
import com.davicaetano.runningwithmusic.ui.modules.PlayerActivityModule;
import com.davicaetano.runningwithmusic.ui.presenter.PlayerActivityPresenter;
import com.davicaetano.runningwithmusic.ui.view.PlayerView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by davicaetano on 12/9/15.
 * This package was developed using MVP.
 * This view uses a presenter to communicate events
 * and receives the events that happen on the music player
 * through the PlayerView interface.
 * The Presenter access the Interactor (the Model).
 * The interactor is the model of the player.
 * In this case this model uses a service to manage the player
 * and a LocalBroadcastReceiver to receive the results of the mediaplayer
 *
 */
public class PlayerActivity extends BaseActivity implements PlayerView, OnMapReadyCallback {

    @Inject
    public PlayerActivityPresenter playerActivityPresenter;

    @Bind(R.id.PLAY) Button buttonPlay;
    @Bind(R.id.NEXT) Button buttonNext;
    @Bind(R.id.PREV) Button buttonPrev;
    @Bind(R.id.seekBar) SeekBar seekBar;
    @Bind(R.id.limit1) TextView limit1;
    @Bind(R.id.limit2) TextView limit2;
    @Bind(R.id.ARTIST_NAME) TextView txtARTIST_NAME;
    @Bind(R.id.ALBUM_NAME) TextView txtALBUM_NAME;
    @Bind(R.id.TRACK_NAME) TextView txtTRACK_NAME;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.button_finish) Button finish;

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser)
                playerActivityPresenter.seekTo(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("davi","PlayerActivity - onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_player);
        ButterKnife.bind(this);

        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);

        toolbar.setTitle("Runnig with music");
        setSupportActionBar(toolbar);

        buttonPlay.setBackgroundResource(android.R.drawable.ic_media_play);
        buttonNext.setBackgroundResource(android.R.drawable.ic_media_next);
        buttonPrev.setBackgroundResource(android.R.drawable.ic_media_previous);

        Intent intent = getIntent();
        String action = intent.getAction();
        if("START".equals(action)) {
            playerActivityPresenter.startSession();
            intent.setAction("");
        }

        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    @Override
    public void setupActivityComponent() {
        ((CustomApplication)getApplication()).getAppContextComponent()
                .plus(new PlayerActivityModule(this))
                .inject(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        playerActivityPresenter.setMap(googleMap);
    }

    @OnClick(R.id.PLAY)
    public void onButtonPlayClick(){playerActivityPresenter.play();}

    @OnClick(R.id.NEXT)
    public void onButtonNextClick(){playerActivityPresenter.next();}

    @OnClick(R.id.PREV)
    public void onButtonPrevClick(){playerActivityPresenter.prev();}

    @OnClick(R.id.button_finish)
    public void onButtonFinishClick(){playerActivityPresenter.finishSession();}

    @Override
    protected void onResume() {
        super.onResume();
        playerActivityPresenter.onResume();
    }

    @Override
    protected void onPause() {
        Log.v("davi","PlayerActivity - onPause");
        super.onPause();
        playerActivityPresenter.onPause();
    }

    @Override
    public void setSeekToProgress(int SeekToProgress) {
        seekBar.setProgress(SeekToProgress);
    }

    @Override
    public void setArtist(String artist) {
        txtARTIST_NAME.setText(artist);
        txtARTIST_NAME.setContentDescription(artist);
    }

    @Override
    public void setAlbum(String album) {
        txtALBUM_NAME.setText(album);
        txtALBUM_NAME.setContentDescription(album);
    }

    @Override
    public void setSong(String song) {
        txtTRACK_NAME.setText(song);
        txtTRACK_NAME.setContentDescription(song);
    }

    @Override
    public void setPlayButton(boolean play) {
        if(!play){
            buttonPlay.setBackgroundResource(android.R.drawable.ic_media_play);
            buttonPlay.setContentDescription(getString(R.string.play));
        }else{
            buttonPlay.setBackgroundResource(android.R.drawable.ic_media_pause);
            buttonPlay.setContentDescription(getString(R.string.pause));

        }
    }

    @Override
    public void setTime1(String time1) {
        limit1.setText(time1);
        limit1.setContentDescription(time1.replace(":", getString(R.string.minutes)) + getString(R.string.seconds));
    }

    @Override
    public void setTime2(String time2) {
        limit2.setText(time2);
        limit2.setContentDescription(time2.replace(":", getString(R.string.minutes)) + getString(R.string.seconds));

    }

    @Override
    public void setSeekToMax(int seekToTotal) {
        seekBar.setMax(seekToTotal);
        seekBar.setContentDescription(limit1.getContentDescription());
    }

    @Override
    public void close() {
        Log.v("davi", "PlayerActivity - close");
        this.finish();
    }
}
