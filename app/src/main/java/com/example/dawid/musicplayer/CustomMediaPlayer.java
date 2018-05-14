package com.example.dawid.musicplayer;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.media.MediaPlayer;

public class CustomMediaPlayer
{
    public enum PlayerState {TrackNotSet, Prepared, Paused, Playing}

    private static CustomMediaPlayer instance = new CustomMediaPlayer();
    private MutableLiveData<PlayerState> playerStateLiveData = new MutableLiveData<>();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private TrackData trackData;

    private CustomMediaPlayer()
    {
        trackData = new TrackData();
        playerStateLiveData.setValue(PlayerState.TrackNotSet);
        new Thread()
        {
            @Override
            public void run()
            {
                while(true)
                {
                    if(playerStateLiveData.getValue() == PlayerState.Playing)
                    {
                        synchronized (mediaPlayer)
                        {
                            if (mediaPlayer.getCurrentPosition() >= mediaPlayer.getDuration())
                            {
                                playerStateLiveData.postValue(PlayerState.Prepared);
                            }
                        }
                    }

                    synchronized (this)
                    {
                        try { wait(50);}
                        catch (InterruptedException e) { e.printStackTrace(); }
                    }
                }
            }
        }.start();
    }

    public static CustomMediaPlayer getInstance()
    {
        return instance;
    }

    public TrackData getTrackData()
    {
        return trackData;
    }

    public MutableLiveData<PlayerState> getPlayerStateLiveData()
    {
        return playerStateLiveData;
    }

    public void startNewTrack(int trackId)
    {
        stopPlaying();
        setNewTrack(trackId);
        startPlaying();
    }

    public MediaPlayer getMediaPlayer()
    {
        return mediaPlayer;
    }

    public void setNewTrack(int trackId)
    {
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(MusicService.getContext(), trackId);
        mediaPlayer.seekTo(0);
        trackData.setCurrentTrack(trackId);
        playerStateLiveData.setValue(PlayerState.Prepared);
    }

    public void startPlaying()
    {
        mediaPlayer.start();
        playerStateLiveData.setValue(PlayerState.Playing);
    }

    public void stopPlaying()
    {
        if(playerStateLiveData.getValue() != PlayerState.TrackNotSet)
        {
            mediaPlayer.stop();
        }
        playerStateLiveData.setValue(PlayerState.Prepared);
    }

    public void pausePlaying()
    {
        mediaPlayer.pause();
        playerStateLiveData.setValue(PlayerState.Paused);
    }

    public void moveTime(int milisecs)
    {
        if(playerStateLiveData.getValue() == PlayerState.TrackNotSet)
            return;

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
        if(playerStateLiveData.getValue() == PlayerState.Playing)
        {
            pausePlaying();
        }
        else if(playerStateLiveData.getValue() != PlayerState.TrackNotSet)
        {
            startPlaying();
        }
    }
}
