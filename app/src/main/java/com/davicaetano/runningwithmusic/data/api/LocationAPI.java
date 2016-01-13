package com.davicaetano.runningwithmusic.data.api;

/**
 * Created by davicaetano on 12/17/15.
 */
public interface LocationAPI {
    void startLocation();
    void stopLocation();

    void finishSession(Long sessionId);

    void refreshRate(long sessionId, long songId);

    boolean isConnected();
}
