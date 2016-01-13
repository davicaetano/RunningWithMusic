package com.davicaetano.runningwithmusic.ui.components;

import com.davicaetano.runningwithmusic.ui.ActivityScope;
import com.davicaetano.runningwithmusic.ui.MainActivity;
import com.davicaetano.runningwithmusic.ui.modules.MainActivityModule;

import dagger.Subcomponent;

/**
 * Created by davicaetano on 1/2/16.
 */
@ActivityScope
@Subcomponent(
        modules = {
                MainActivityModule.class
        }
)
public interface MainActivityComponent {
        void inject(MainActivity mainActivity);
}
