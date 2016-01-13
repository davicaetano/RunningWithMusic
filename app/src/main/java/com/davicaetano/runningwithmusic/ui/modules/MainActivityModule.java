package com.davicaetano.runningwithmusic.ui.modules;

import com.davicaetano.runningwithmusic.ui.ActivityScope;
import com.davicaetano.runningwithmusic.ui.MainActivity;
import com.davicaetano.runningwithmusic.ui.presenter.MainActivityPresenter;
import com.davicaetano.runningwithmusic.ui.presenter.MainActivityPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by davicaetano on 1/2/16.
 */
@Module
public class MainActivityModule {
    private MainActivity mainActivity;

    public MainActivityModule(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Provides
    @ActivityScope
    public MainActivity providesMainActivity(){
        return mainActivity;
    }

    @Provides
    @ActivityScope
    public MainActivityPresenter providesMainActivityPresenter(){
        return new MainActivityPresenterImpl();
    }
}
