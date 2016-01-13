package com.davicaetano.runningwithmusic.data.api.components;

import com.davicaetano.runningwithmusic.data.api.PlayerService;
import com.davicaetano.runningwithmusic.data.api.modules.PlayerServiceModule;
import com.davicaetano.runningwithmusic.ui.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by davicaetano on 1/3/16.
 */
@ActivityScope
@Subcomponent(
        modules = {
                PlayerServiceModule.class
        }
)
public interface PlayerServiceComponent {
    void inject(PlayerService playerService);
}
