package com.davicaetano.runningwithmusic.data.api;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;

import com.davicaetano.runningwithmusic.R;
import com.squareup.picasso.Picasso;

/**
 * Created by davicaetano on 1/12/16.
 */
public class WidgetProvider extends AppWidgetProvider {

    private boolean onSession;
    private String trackName;
    private String artistName;
    private boolean isPlaying;
    private String imageFile;


    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.hasExtra("ON_SESSION")) {
            onSession = true;
            trackName = intent.getStringExtra("TRACK_NAME");
            artistName = intent.getStringExtra("ARTIST_NAME");
            imageFile = intent.getStringExtra("IMAGE_FILE");
            isPlaying = intent.getBooleanExtra("IS_PLAYING",false);
        }else {
            onSession = false;
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.v("davi", "WidgetProvider - onUpdate");



        for (int i = 0; i < appWidgetIds.length; i++) {
            if (onSession) {
                Log.v("davi", "WidgetProvider - onUpdate 1");
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification);
                remoteViews.setTextViewText(R.id.list_item_song_song_name, trackName);
                remoteViews.setTextViewText(R.id.list_item_song_artist_name, artistName);
                remoteViews.setImageViewResource(R.id.list_item_song_image, R.drawable.icon);


//                Intent intent = new Intent(context, PlayerActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                remoteViews.setOnClickPendingIntent(R.id.layout_notification, pendingIntent);

                remoteViews.setInt(R.id.button1, "setBackgroundResource", android.R.drawable.ic_media_previous);
                Intent intent1 = new Intent(context, PlayerService.class);
                intent1.setAction(PlayerAPIImpl.PREV);
                PendingIntent pendingIntent1 = PendingIntent.getService(context, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.button1, pendingIntent1);

                if (isPlaying) {
                    remoteViews.setInt(R.id.button2, "setBackgroundResource", android.R.drawable.ic_media_pause);
                } else {
                    remoteViews.setInt(R.id.button2, "setBackgroundResource", android.R.drawable.ic_media_play);
                }
                Intent intent2 = new Intent(context, PlayerService.class);
                intent2.setAction(PlayerAPIImpl.PLAY);
                PendingIntent pendingIntent2 = PendingIntent.getService(context, 2, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.button2, pendingIntent2);

                remoteViews.setInt(R.id.button3, "setBackgroundResource", android.R.drawable.ic_media_next);
                Intent intent3 = new Intent(context, PlayerService.class);
                intent3.setAction(PlayerAPIImpl.NEXT);

                PendingIntent pendingIntent3 = PendingIntent.getService(context, 3, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.button3, pendingIntent3);

                remoteViews.setInt(R.id.button1, "setVisibility", Button.VISIBLE);
                remoteViews.setInt(R.id.button2, "setVisibility", Button.VISIBLE);
                remoteViews.setInt(R.id.button3, "setVisibility", Button.VISIBLE);

                final String getOnlinePic = imageFile;
                if (getOnlinePic != null && !getOnlinePic.equals("NOT_FOUND")) {
                    Picasso.with(context).load(getOnlinePic).into(remoteViews, R.id.list_item_song_image, new int[]{appWidgetIds[i]});
                }
                appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
            }else{
                Log.v("davi", "WidgetProvider - onUpdate 2");
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification);
                remoteViews.setTextViewText(R.id.list_item_song_song_name, context.getString(R.string.app_name));
                remoteViews.setTextViewText(R.id.list_item_song_artist_name, "Session not started");
                remoteViews.setImageViewResource(R.id.list_item_song_image, R.drawable.icon);
                remoteViews.setInt(R.id.button1, "setVisibility", Button.INVISIBLE);
                remoteViews.setInt(R.id.button2, "setVisibility", Button.INVISIBLE);
                remoteViews.setInt(R.id.button3, "setVisibility", Button.INVISIBLE);
                appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);

            }
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }
}
