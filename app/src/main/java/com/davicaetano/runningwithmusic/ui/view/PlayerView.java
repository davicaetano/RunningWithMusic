package com.davicaetano.runningwithmusic.ui.view;

/**
 * Created by davicaetano on 12/9/15.
 */
public interface PlayerView {
    void setArtist(String artist);
    void setAlbum(String album);
    void setSong(String song);
    void setPlayButton(boolean play);
    void setTime1(String time1);
    void setTime2(String time2);
    void setSeekToMax(int seekToMax);
    void setSeekToProgress(int SeekToProgress);

    void close();
}
