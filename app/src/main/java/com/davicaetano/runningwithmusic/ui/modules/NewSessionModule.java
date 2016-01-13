package com.davicaetano.runningwithmusic.ui.modules;

import com.davicaetano.runningwithmusic.ui.NewSessionActivity;

import dagger.Module;

/**
 * Created by davicaetano on 1/3/16.
 */
@Module
public class NewSessionModule {
    private NewSessionActivity newSessionActivity;
    public NewSessionModule(NewSessionActivity newSessionActivity) {
        this.newSessionActivity = newSessionActivity;
    }
}
