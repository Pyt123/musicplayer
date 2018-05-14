package com.example.dawid.musicplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

public class MusicService extends Service
{
    private CustomMediaPlayer customMediaPlayer;
    private static MediaPlayer mediaPlayer;
    private static MusicService musicService;
    private Notification mNotification;
    public static final String NOTIFICATION_CHANNEL_ID = "4565";
    private static final String NOTIFICATION_CHANNEL_NAME = "Music Chanel";

    public static Context getContext()
    {
        return musicService;
    }

    public static MediaPlayer getMediaPlayer()
    {
        return mediaPlayer;
    }

    public static void setMediaPlayer(MediaPlayer mediaPlayer)
    {
        MusicService.mediaPlayer = mediaPlayer;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle("Service")
                .setContentText("Music")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        mNotification = mBuilder.build();

        //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, mNotification);
        /*final String text = "Service";
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification = new Notification();
        mNotification.tickerText = text;
        mNotification.icon = android.R.drawable.ic_media_play;
        mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
        NotificationCompat.
        mNotification.(getApplicationContext(), "MusicPlayer",
                text, pi);
        startForeground(NOTIFICATION_ID, mNotification);*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        musicService = this;
        customMediaPlayer = CustomMediaPlayer.getInstance();
        mediaPlayer = new MediaPlayer();
        startForeground(1, mNotification);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Toast.makeText(this, "o kurwa", Toast.LENGTH_LONG).show();
        /*Intent intent = new Intent(this, MusicService.class);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        startForegroundService(intent);*/
    }
}
