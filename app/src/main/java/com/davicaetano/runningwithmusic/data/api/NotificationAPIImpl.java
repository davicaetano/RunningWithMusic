package com.davicaetano.runningwithmusic.data.api;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.davicaetano.runningwithmusic.R;
import com.davicaetano.runningwithmusic.ui.PlayerActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by davicaetano on 1/5/16.
 */
public class NotificationAPIImpl implements  NotificationAPI {

    public Context context;
    public PlayerService playerService;
    private NotificationManager notificationManager;


    public NotificationAPIImpl(Context context, PlayerService playerService) {
        this.context = context;
        this.playerService = playerService;
        if(notificationManager == null){
            notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        }
    }

    public void notif() {
        Log.v("davi", "notif");
//        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("notification",true)) {
        if (true) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(playerService.getTrackName())
                    .setContentText(playerService.getArtistName());
            Notification notification = mBuilder.build();
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.notification);
            remoteViews.setTextViewText(R.id.list_item_song_song_name, playerService.getTrackName());
            remoteViews.setTextViewText(R.id.list_item_song_artist_name, playerService.getArtistName());
            remoteViews.setImageViewResource(R.id.list_item_song_image, R.drawable.icon);

            Intent intent = new Intent(context, PlayerActivity.class);
            intent.setAction("NOT_START");
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.layout_notification, pendingIntent);

            remoteViews.setInt(R.id.button1, "setBackgroundResource", android.R.drawable.ic_media_previous);
            Intent intent1 = new Intent(context,PlayerService.class);
            intent1.setAction(PlayerAPIImpl.PREV);
            PendingIntent pendingIntent1 = PendingIntent.getService(context, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.button1, pendingIntent1);

            if(playerService.isPlaying()) {
                remoteViews.setInt(R.id.button2, "setBackgroundResource", android.R.drawable.ic_media_pause);
            }else{
                remoteViews.setInt(R.id.button2, "setBackgroundResource", android.R.drawable.ic_media_play);
            }
            Intent intent2 = new Intent(context,PlayerService.class);
            intent2.setAction(PlayerAPIImpl.PLAY);
            PendingIntent pendingIntent2 = PendingIntent.getService(context, 2, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.button2, pendingIntent2);

            remoteViews.setInt(R.id.button3, "setBackgroundResource", android.R.drawable.ic_media_next);
            Intent intent3 = new Intent(context,PlayerService.class);
            intent3.setAction(PlayerAPIImpl.NEXT);

            PendingIntent pendingIntent3 = PendingIntent.getService(context, 3, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.button3, pendingIntent3);
            notification.contentView = remoteViews;
            notificationManager.notify(1, notification);
//            playerService.startForeground(1, notification);
            final String getOnlinePic = playerService.getImage();
            if(getOnlinePic != null && !getOnlinePic.equals("NOT_FOUND")) {
                Picasso.with(context).load(getOnlinePic).into(remoteViews, R.id.list_item_song_image, 1, notification);
            }else{

            }
        }else{
            stopNotif();
        }
    }

    public void stopNotif(){
        notificationManager.cancel(1);
    }
}
