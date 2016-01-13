package com.davicaetano.runningwithmusic;

import android.content.Context;
import android.content.SharedPreferences;

import com.davicaetano.runningwithmusic.data.api.components.PlayerServiceComponent;
import com.davicaetano.runningwithmusic.data.api.modules.PlayerServiceModule;
import com.davicaetano.runningwithmusic.ui.components.MainActivityComponent;
import com.davicaetano.runningwithmusic.ui.components.NewSessionComponent;
import com.davicaetano.runningwithmusic.ui.components.PlayerActivityComponent;
import com.davicaetano.runningwithmusic.ui.modules.MainActivityModule;
import com.davicaetano.runningwithmusic.ui.modules.NewSessionModule;
import com.davicaetano.runningwithmusic.ui.modules.PlayerActivityModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by davicaetano on 12/30/15.
 */

@Singleton
@Component(
        modules = {
                AppContextModule.class,
        }
)
public interface AppContextComponent {
    //The instances provided by this component.
    //These instances are Singleton for the Application
    CustomApplication application();
    Context applicationContext();
    SharedPreferences sharedPreferences();

    //subcomponents
    MainActivityComponent plus(MainActivityModule mainActivityModule);
    PlayerActivityComponent plus(PlayerActivityModule playerActivityModule);
    NewSessionComponent plus(NewSessionModule newSessionModule);
    PlayerServiceComponent plus(PlayerServiceModule playerServiceModule);
}
