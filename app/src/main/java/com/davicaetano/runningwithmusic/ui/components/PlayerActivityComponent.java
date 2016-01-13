package com.davicaetano.runningwithmusic.ui.components;

import com.davicaetano.runningwithmusic.ui.ActivityScope;
import com.davicaetano.runningwithmusic.ui.PlayerActivity;
import com.davicaetano.runningwithmusic.ui.modules.PlayerActivityModule;

import dagger.Subcomponent;

/**
 * Created by davicaetano on 12/31/15.
 */
@ActivityScope
@Subcomponent(
        modules = {
                PlayerActivityModule.class
        }
)
public interface PlayerActivityComponent {
        void inject(PlayerActivity playerActivity);
}
