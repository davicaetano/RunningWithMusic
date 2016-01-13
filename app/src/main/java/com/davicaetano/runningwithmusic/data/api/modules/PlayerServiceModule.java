package com.davicaetano.runningwithmusic.data.api.modules;

import android.content.Context;

import com.davicaetano.runningwithmusic.data.api.LocationAPI;
import com.davicaetano.runningwithmusic.data.api.LocationAPIImpl;
import com.davicaetano.runningwithmusic.data.api.NotificationAPI;
import com.davicaetano.runningwithmusic.data.api.NotificationAPIImpl;
import com.davicaetano.runningwithmusic.data.api.PlayerService;
import com.davicaetano.runningwithmusic.ui.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by davicaetano on 1/3/16.
 */
@Module
public class PlayerServiceModule {
    public PlayerService playerService;
    public PlayerServiceModule(PlayerService playerService){
        this.playerService = playerService;
    }

    @Provides
    @ActivityScope
    public PlayerService providePlayerService(){
        return playerService;
    }

    @Provides
    @ActivityScope
    public LocationAPI provideLocationAPI(Context context, PlayerService playerService){
        return new LocationAPIImpl(context, playerService);
    }

    @Provides
    @ActivityScope
    public NotificationAPI provideNotificationAPI(Context context, PlayerService playerService){
        return new NotificationAPIImpl(context, playerService);
    }
}
