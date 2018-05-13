package com.example.dawid.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;

public class CustomMediaPlayer
{
    private static CustomMediaPlayer instance = new CustomMediaPlayer();
    private MediaPlayer mediaPlayer = new MediaPlayer();

    private CustomMediaPlayer() { }

    public static CustomMediaPlayer getInstance()
    {
        return instance;
    }

    public void startNewTrack(Context context, int trackId)
    {
        stopPlaying();
        setNewTrack(context, trackId);
        startPlaying();
    }

    public MediaPlayer getMediaPlayer()
    {
        return mediaPlayer;
    }

    public void setNewTrack(Context context, int trackId)
    {
        mediaPlayer = MediaPlayer.create(context, trackId);
        mediaPlayer.seekTo(0);
    }

    public void startPlaying()
    {
        mediaPlayer.start();
    }

    public void stopPlaying()
    {
        mediaPlayer.stop();
    }

    public void pausePlaying()
    {
        mediaPlayer.pause();
    }
}
