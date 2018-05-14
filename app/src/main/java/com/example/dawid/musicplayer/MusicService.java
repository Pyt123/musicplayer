package com.example.dawid.musicplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

public class MusicService extends Service
{
    private static MusicService musicService;

    public static final String NOTIFICATION_CHANNEL_ID = "4565";
    private static final String NOTIFICATION_CHANNEL_NAME = "Music Chanel";
    private static final int SERVICE_ID = 1;
    private NotificationChannel notificationChannel;
    private NotificationManager notificationManager;
    private Notification mNotification;

    public static Context getContext()
    {
        return musicService;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            createNotificationChannel();
        }
        createNotification(null);
        observeTrackChanging();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel()
    {

            int importance = NotificationManager.IMPORTANCE_LOW;
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
    }

    private void createNotification(@Nullable Track track)
    {
        String title = "", author = "";
        if(track != null)
        {
            title = track.getTrackTitle();
            author = track.getTrackAuthor();
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle(title)
                .setContentText(author)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        mNotification = mBuilder.build();

        notificationManager.notify(SERVICE_ID, mNotification);
    }

    private void observeTrackChanging()
    {
        CustomMediaPlayer.getInstance().getTrackData().getLiveDataCurrentTrack().observeForever(new Observer<Track>()
        {
            @Override
            public void onChanged(@Nullable Track track)
            {
                createNotification(track);
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        musicService = this;
        CustomMediaPlayer.getInstance().provideService(this);
        startForeground(SERVICE_ID, mNotification);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
