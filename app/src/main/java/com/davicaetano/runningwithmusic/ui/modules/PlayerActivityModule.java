package com.davicaetano.runningwithmusic.ui.modules;

import android.content.Context;

import com.davicaetano.runningwithmusic.data.api.PlayerAPI;
import com.davicaetano.runningwithmusic.data.api.PlayerAPIImpl;
import com.davicaetano.runningwithmusic.ui.ActivityScope;
import com.davicaetano.runningwithmusic.ui.presenter.PlayerActivityPresenter;
import com.davicaetano.runningwithmusic.ui.presenter.PlayerActivityPresenterImpl;
import com.davicaetano.runningwithmusic.ui.view.PlayerView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by davicaetano on 12/31/15.
 */
@Module
public class PlayerActivityModule {
    private PlayerView playerView;

    public PlayerActivityModule(PlayerView playerView){
        this.playerView = playerView;
    }

    @ActivityScope
    @Provides
    public PlayerView providePlayerView() {
        return this.playerView;
    }

    @ActivityScope
    @Provides
    public PlayerActivityPresenter providePlayerPresenter(PlayerView playerView, PlayerAPI playerAPI, Context context){
        return new PlayerActivityPresenterImpl(playerView, playerAPI, context);
    }


    @Provides
    @ActivityScope
    public PlayerAPI providePlayerAPI(Context context){
        return new PlayerAPIImpl(context);
    }
}
