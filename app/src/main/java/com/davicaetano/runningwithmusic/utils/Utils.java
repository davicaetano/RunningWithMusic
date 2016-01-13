package com.davicaetano.runningwithmusic.utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by davicaetano on 12/9/15.
 */
public class Utils {
    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
