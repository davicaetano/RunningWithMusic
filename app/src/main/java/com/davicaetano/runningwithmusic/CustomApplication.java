package com.davicaetano.runningwithmusic;

import android.app.Application;


/**
 * Created by davicaetano on 12/30/15.
 */
public class CustomApplication extends Application {
    private AppContextComponent appContextComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appContextComponent =  DaggerAppContextComponent
                .builder()
                .appContextModule(new AppContextModule(this))
                .build();
    }

//    @Override
//    public void onTerminate() {
//        Log.v("davi","CustomApplication - onTerminate");
//        Intent intent = new Intent(this, PlayerService.class);
//        stopService(intent);
//        super.onTerminate();
//    }

    public AppContextComponent getAppContextComponent(){
        return this.appContextComponent;
    }
}
