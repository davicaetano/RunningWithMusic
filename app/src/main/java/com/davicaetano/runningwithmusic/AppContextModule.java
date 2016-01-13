package com.davicaetano.runningwithmusic;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by davicaetano on 12/30/15.
 */
@Module
public class AppContextModule {
    private final CustomApplication application;

    public AppContextModule(CustomApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public CustomApplication application(){
        return this.application;
    }

    @Provides
    @Singleton
    public Context applicationContext(){
        return this.application;
    }

    @Provides
    @Singleton
    public SharedPreferences sharedPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

}
