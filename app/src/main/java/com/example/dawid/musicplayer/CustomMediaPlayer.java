package com.example.dawid.musicplayer;

import android.arch.lifecycle.MutableLiveData;
import android.media.MediaPlayer;

public class CustomMediaPlayer
{
    public enum PlayerState {TrackNotSet, Prepared, Paused, Playing}

    private static CustomMediaPlayer instance = new CustomMediaPlayer();
    private MutableLiveData<PlayerState> playerStateLiveData = new MutableLiveData<>();
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
                        synchronized (getMediaPlayer())
                        {
                            if (getMediaPlayer().getCurrentPosition() >= getMediaPlayer().getDuration())
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
        return MusicService.getMediaPlayer();
    }

    public void setNewTrack(int trackId)
    {
        getMediaPlayer().reset();
        getMediaPlayer().release();
        MusicService.setMediaPlayer(MediaPlayer.create(MusicService.getContext(), trackId));
        getMediaPlayer().seekTo(0);
        trackData.setCurrentTrack(trackId);
        playerStateLiveData.setValue(PlayerState.Prepared);
    }

    public void startPlaying()
    {
        getMediaPlayer().start();
        playerStateLiveData.setValue(PlayerState.Playing);
    }

    public void stopPlaying()
    {
        if(playerStateLiveData.getValue() != PlayerState.TrackNotSet)
        {
            getMediaPlayer().stop();
        }
        playerStateLiveData.setValue(PlayerState.Prepared);
    }

    public void pausePlaying()
    {
        getMediaPlayer().pause();
        playerStateLiveData.setValue(PlayerState.Paused);
    }

    public void moveTime(int milisecs)
    {
        if(playerStateLiveData.getValue() == PlayerState.TrackNotSet)
            return;

        int targetTime = getMediaPlayer().getCurrentPosition() + milisecs;
        if(targetTime > getMediaPlayer().getDuration())
        {
            targetTime = getMediaPlayer().getDuration();
        }
        else if(targetTime < 0)
        {
            targetTime = 0;
        }
        getMediaPlayer().seekTo(targetTime);
    }

    public void moveToPercentOfTrack(int percent)
    {
        int targetMilisecs = getMediaPlayer().getDuration() * percent / 100;
        getMediaPlayer().seekTo(targetMilisecs);
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
