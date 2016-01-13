package com.davicaetano.runningwithmusic.data.api;

import android.os.AsyncTask;
import android.widget.Toast;

import com.davicaetano.runningwithmusic.R;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;

/**
 * Created by davicaetano on 1/6/16.
 */
public class SpotifyAsyncTask extends AsyncTask<String, String, String> {
    private SpotifyApi api;
    private SpotifyService spotify;
    private PlayerService playerService ;

    public SpotifyAsyncTask(PlayerService playerService){
        this.playerService = playerService;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Toast.makeText(playerService,values[0],Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params){
        ArrayList<kaaes.spotify.webapi.android.models.Artist> mList = new ArrayList<kaaes.spotify.webapi.android.models.Artist>();
        String uri = "NOT_FOUND";
        try{
            api = new SpotifyApi();
            spotify = api.getService();
            ArtistsPager results = spotify.searchArtists(params[0]);
            Pager<Artist> mPager = results.artists;
            mList = (ArrayList)mPager.items;
        }
        catch (Exception e){
            publishProgress("Conexion error");
        }
        if (mList.size() > 0){
            for(kaaes.spotify.webapi.android.models.Artist cada:mList) {
                if (cada.images.size() > 0) {
                    float height = playerService.getResources().getDimension(R.dimen.artist_height);
                    for (int i = cada.images.size() - 1; i >= 0; i--) {
                        if (((float) cada.images.get(i).height >= height) || i == 0) {
                            uri = cada.images.get(i).url;
                            break;
                        }
                    }
                    break;
                }
            }
        }
        String[] args = {params[0],uri};
        playerService.recordImage(args);
        return null;
    }
}
