package com.davicaetano.runningwithmusic.data.api;

import com.davicaetano.runningwithmusic.ui.presenter.PlayerActivityPresenter;

/**
 * Created by davicaetano on 12/10/15.
 */
public interface PlayerAPI {
    void refresh();

    void setCallback(PlayerActivityPresenter playerActivityPresenter);

    void startSession();

    void play();

    void next();

    void prev();

    void refreshTime();

    void seekTo(int progress);

    void finishSession();
}
