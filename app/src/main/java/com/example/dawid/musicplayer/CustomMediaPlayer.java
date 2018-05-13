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

    public void moveTime(int milisecs)
    {
        int targetTime = mediaPlayer.getCurrentPosition() + milisecs;
        if(targetTime > mediaPlayer.getDuration())
        {
            targetTime = mediaPlayer.getDuration();
        }
        else if(targetTime < 0)
        {
            targetTime = 0;
        }
        mediaPlayer.seekTo(targetTime);
    }

    public void moveToPercentOfTrack(int percent)
    {
        int targetMilisecs = mediaPlayer.getDuration() * percent / 100;
        mediaPlayer.seekTo(targetMilisecs);
    }

    public void handleStartPauseButtonClicked()
    {
        if(mediaPlayer.isPlaying())
        {
            pausePlaying();
        }
        else
        {
            startPlaying();
        }
    }
}
