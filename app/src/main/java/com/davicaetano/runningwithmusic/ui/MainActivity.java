package com.davicaetano.runningwithmusic.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.davicaetano.runningwithmusic.CustomApplication;
import com.davicaetano.runningwithmusic.R;
import com.davicaetano.runningwithmusic.data.api.PlayerService;
import com.davicaetano.runningwithmusic.data.database.RWMContract;
import com.davicaetano.runningwithmusic.ui.modules.MainActivityModule;
import com.davicaetano.runningwithmusic.ui.presenter.MainActivityPresenter;

import java.util.Random;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @Inject
    public MainActivityPresenter mainActivityPresenter;

    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar.setTitle("Runnig with music");
        setSupportActionBar(toolbar);

        checkMusicContentProvider();
        Cursor c2 = this.getContentResolver().query(RWMContract.Songs.CONTENT_URI, null, null, null, null);
        printCursor(c2);

        Cursor c3 = this.getContentResolver().query(RWMContract.Sessions.CONTENT_URI, null, null, null, null);
        printCursor(c3);

        Cursor c4 = this.getContentResolver().query(RWMContract.Steps.CONTENT_URI, null, null, null, null);
        printCursor(c4);
    }

    @Override
    public void setupActivityComponent() {
        ((CustomApplication)getApplication()).getAppContextComponent()
                .plus(new MainActivityModule(this))
                .inject(this);
    }

    @Override
    public void onBackPressed() {
        Log.v("davi", "MainActivity - onBackPressed");
        Intent intent = new Intent(this, PlayerService.class);
        stopService(intent);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        Log.v("davi", "MainActivity - onDestroy");
        Intent intent = new Intent(this, PlayerService.class);
        stopService(intent);
        super.onDestroy();
    }

    @OnClick(R.id.card_new_run)
    public void oncard_new_runClick(){
        Intent intent = new Intent(this, NewSessionActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.card_history)
    public void onCardHistoryClick(){
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    private void checkMusicContentProvider(){

        Cursor c1 = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null);
        c1.moveToFirst();

        while(!c1.isAfterLast()){
            String album = c1.getString(c1.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            String artist = c1.getString(c1.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            if(!album.toLowerCase().equals("notifications") &&
                    !album.toLowerCase().equals("<unknown>") &&
                    !artist.toLowerCase().equals("<unknown>") &&
                    c1.getInt(c1.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)) != 0){
                ContentValues contentValues = new ContentValues();
                contentValues.put(RWMContract.Songs.COLUMN_ALBUM_NAME,c1.getString(c1.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                contentValues.put(RWMContract.Songs.COLUMN_ARTIST,c1.getString(c1.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                contentValues.put(RWMContract.Songs.COLUMN_SONG_NAME,c1.getString(c1.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                contentValues.put(RWMContract.Songs.COLUMN_FILE, c1.getString(c1.getColumnIndex(MediaStore.Audio.Media.DATA)));
                contentValues.put(RWMContract.Songs.COLUMN_DURATION, c1.getString(c1.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                Random random = new Random();
                contentValues.put(RWMContract.Songs.COLUMN_RATE, random.nextInt(10)+1);
                this.getContentResolver().insert(RWMContract.Songs.CONTENT_URI, contentValues);
            }
            c1.moveToNext();
        }

//
//        Cursor c2 = this.getContentResolver().query(RWMContract.Songs.CONTENT_URI,
//                null,
//                null,
//                null,
//                null);
//
//        printCursor(c2);
    }

    private void printCursor(Cursor c){
        if(c!=null) {
            Log.v("davi - tabela", "-------------------------------------");
            int columns = c.getColumnCount();
            String header = "";
            for (int i = 0; i < columns; i++) {
                header = header + c.getColumnName(i) + "|";
            }
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                String row = "";
                for (int i = 0; i < columns; i++) {
                    row = row + c.getString(i) + "|";
                }
                Log.v("davi - tabela", row);
            }
            Log.v("davi - tabela", "-------------------------------------");
        }else{
            Log.v("davi - tabela", "No records");
        }
    }
}
