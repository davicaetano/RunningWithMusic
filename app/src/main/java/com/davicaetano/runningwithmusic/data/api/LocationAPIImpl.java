package com.davicaetano.runningwithmusic.data.api;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.davicaetano.runningwithmusic.data.database.RWMContract;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

public class LocationAPIImpl implements LocationAPI,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private Context context;
    private PlayerService playerService;
    Location lastLocation;
    Long lastDate;

    private LocalBroadcastManager broadcastManager;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public LocationAPIImpl(Context context, PlayerService playerService){
        this.context = context;
        this.playerService = playerService;
        if(mGoogleApiClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(context).
                    addConnectionCallbacks(this).
                    addOnConnectionFailedListener(this).
                    addApi(LocationServices.API).
                    build();
        }
        mGoogleApiClient.connect();
    }

    @Override
    public void stopLocation() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void startLocation() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        createLocationRequest();
        startLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("davi", "LocationAPIImpl - onConnectionSuspended");

    }

    @Override
    public void onLocationChanged(Location location) {
        ContentValues cv = new ContentValues();
        Log.v("davi", "LocationAPIImpl - onLocationChanged");
        double speed = 0;
        double distance = 0;
        double time = 0;
        Calendar calendar;
        if(lastLocation != null && lastDate != null){
            distance = calculateDistance(lastLocation.getLatitude(), lastLocation.getLongitude(),
                    location.getLatitude(), location.getLongitude());
            time = lastDate - playerService.now();
            if(time != 0)
                speed = distance / time * 1000;//Speed is in m/s.
            else
                speed = 0;
        }
        cv.put(RWMContract.Steps.COLUMN_ALTITUDE,location.getAltitude());
        cv.put(RWMContract.Steps.COLUMN_LATITUDE,location.getLatitude());
        cv.put(RWMContract.Steps.COLUMN_LONGITUDE,location.getLongitude());
        cv.put(RWMContract.Steps.COLUMN_SPEED, speed);
        cv.put(RWMContract.Steps.COLUMN_SPACE, distance);
        cv.put(RWMContract.Steps.COLUMN_RUNNING_ID, playerService.getSessionId());
        cv.put(RWMContract.Steps.COLUMN_SONG_ID, playerService.getSongId());
        cv.put(RWMContract.Steps.COLUMN_TIME, playerService.now());
        context.getContentResolver().insert(
                RWMContract.Steps.CONTENT_URI,
                cv);
        Intent intent = new Intent(PlayerAPIImpl.STEP);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        intent.putExtra(PlayerAPIImpl.LOCATION, latLng);
        playerService.broadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("davi", "LocationAPIImpl - onConnectionFailed");
    }

    @Override
    public void finishSession(Long sessionId) {
        stopLocation();
        double space = 0;
        double time = 0;
        if(sessionId > 0) {
            Cursor cursor = context.getContentResolver().query(RWMContract.Steps.CONTENT_URI,
                    null,
                    RWMContract.Steps.COLUMN_RUNNING_ID + " = ? ",
                    new String[]{Long.toString(sessionId)},
                    RWMContract.Steps.COLUMN_TIME + " ASC ");
            if(cursor.moveToFirst()) {
                time = cursor.getDouble(cursor.getColumnIndex(RWMContract.Steps.COLUMN_TIME));
                for (; cursor.moveToNext(); cursor.isAfterLast()) {
                    space += cursor.getDouble(cursor.getColumnIndex(RWMContract.Steps.COLUMN_SPACE));
                }

            }
            ContentValues cv = new ContentValues();
            long time2 = playerService.now();
            cv.put(RWMContract.Sessions.COLUMN_TIME2, time2);
            cv.put(RWMContract.Sessions.COLUMN_SPACE, space);
            cv.put(RWMContract.Sessions.COLUMN_SPEED, space / (time2-time));
            context.getContentResolver().update(RWMContract.Sessions.CONTENT_URI,
                    cv,
                    RWMContract.Sessions._ID + " = ? ",
                    new String[]{Long.toString(sessionId)});
        }

    }

    @Override
    public void refreshRate(long sessionId, long songId) {
        Cursor cursor = context.getContentResolver().query(RWMContract.Steps.CONTENT_URI,
                null,
                RWMContract.Steps.COLUMN_SONG_ID + " = ? AND " + RWMContract.Steps.COLUMN_RUNNING_ID + " = ? ",
                new String[]{Long.toString(songId),Long.toString(sessionId)},
                RWMContract.Steps.COLUMN_TIME + " ASC ");
        double dist1 = 0;
        double time1 = 0;
        double dist2 = 0;
        double time2 = 0;
        int max = 0;
        if(cursor.getCount() > 6)
            max = 6;
        else
            max = cursor.getCount();
        if(cursor.moveToFirst()) {
            time1 = cursor.getDouble(cursor.getColumnIndex(RWMContract.Steps.COLUMN_TIME));
            for (int i = 0; i < max; i++) {
                dist1 += cursor.getDouble(cursor.getColumnIndex(RWMContract.Steps.COLUMN_SPACE));
                cursor.moveToNext();
            }
            time1 = cursor.getDouble(cursor.getColumnIndex(RWMContract.Steps.COLUMN_TIME)) - time1;
        }
        if(cursor.moveToLast()) {
            time2 = cursor.getDouble(cursor.getColumnIndex(RWMContract.Steps.COLUMN_TIME));
            for (int i = 0; i < max; i++) {
                dist2 += cursor.getDouble(cursor.getColumnIndex(RWMContract.Steps.COLUMN_SPACE));
                cursor.moveToPrevious();
            }
            time2 = time2 - cursor.getDouble(cursor.getColumnIndex(RWMContract.Steps.COLUMN_TIME));
        }
        int bonus = (dist1/time1 < dist2/time2)? 1 : -1;
        Cursor cursor1 = context.getContentResolver().query(RWMContract.Songs.CONTENT_URI,
                null,
                RWMContract.Steps.COLUMN_SONG_ID + " = ? ",
                new String[]{Long.toString(songId)},
                null);
        int rate = cursor1.getInt(cursor1.getColumnIndex(RWMContract.Songs.COLUMN_RATE)) + bonus;
        if(rate < 0) rate = 0;
        if(rate > 10) rate = 10;
        ContentValues contentValues = new ContentValues();
        contentValues.put(RWMContract.Songs.COLUMN_RATE,rate);
        context.getContentResolver().update(RWMContract.Songs.CONTENT_URI,
                contentValues,
                RWMContract.Steps.COLUMN_SONG_ID + " = ? ",
                new String[]{Long.toString(songId)});
    }

    @Override
    public boolean isConnected() {
        return mGoogleApiClient.isConnected();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
    }

    private static double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        long distanceInMeters = Math.round(6371000 * c);
        return distanceInMeters;
    }
}
