package com.example.dawid.musicplayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class MusicService extends Service
{
    private CustomMediaPlayer customMediaPlayer;
    private MediaPlayer mediaPlayer;
    private static MusicService musicService;

    public static Context getContext()
    {
        return musicService;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        musicService = this;
        customMediaPlayer = CustomMediaPlayer.getInstance();
        mediaPlayer = customMediaPlayer.getMediaPlayer();
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        CustomMediaPlayer.getInstance().getMediaPlayer().stop();
    }
}
