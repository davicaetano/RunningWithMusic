package com.davicaetano.runningwithmusic.ui.components;

import com.davicaetano.runningwithmusic.ui.ActivityScope;
import com.davicaetano.runningwithmusic.ui.NewSessionActivity;
import com.davicaetano.runningwithmusic.ui.modules.NewSessionModule;

import dagger.Subcomponent;

/**
 * Created by davicaetano on 1/3/16.
 */
@ActivityScope
@Subcomponent(
        modules = {
                NewSessionModule.class
        }
)
public interface NewSessionComponent {
    void inject(NewSessionActivity newSessionActivity);
}
