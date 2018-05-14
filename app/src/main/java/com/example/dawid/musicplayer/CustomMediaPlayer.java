package com.example.dawid.musicplayer;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;

public class CustomMediaPlayer
{
    public enum PlayerState {TrackNotSet, Prepared, Paused, Playing, Waiting}
    public enum Mode { LoopAll, LoopOne, PlayOnceAll, PlayOnce }
    private static CustomMediaPlayer instance = new CustomMediaPlayer();
    private MediaPlayer mediaPlayer;
    private MutableLiveData<PlayerState> playerStateLiveData = new MutableLiveData<>();
    private MutableLiveData<Mode> mode = new MutableLiveData<>();
    private TrackData trackData;

    private CustomMediaPlayer()
    {
        mediaPlayer = new MediaPlayer();
        trackData = new TrackData();
        playerStateLiveData.setValue(PlayerState.TrackNotSet);
        mode.setValue(Mode.LoopAll);
        runThreadForPlayerStateChanging();
        handlePlayerStateChange();
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
        synchronized (mediaPlayer)
        {
            getMediaPlayer().reset();
            getMediaPlayer().release();
            mediaPlayer = MediaPlayer.create(MusicService.getContext(), trackId);
        }
        getMediaPlayer().seekTo(0);
        trackData.setCurrentTrack(trackId);
        playerStateLiveData.postValue(PlayerState.Prepared);
    }

    public void startPlaying()
    {
        getMediaPlayer().start();
        playerStateLiveData.postValue(PlayerState.Playing);
    }

    public void stopPlaying()
    {
        if(playerStateLiveData.getValue() != PlayerState.TrackNotSet)
        {
            getMediaPlayer().stop();
        }
        playerStateLiveData.postValue(PlayerState.TrackNotSet);
    }

    public void pausePlaying()
    {
        getMediaPlayer().pause();
        playerStateLiveData.postValue(PlayerState.Paused);
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

    private void runThreadForPlayerStateChanging()
    {
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
                                playerStateLiveData.postValue(PlayerState.Waiting);
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


    private void handlePlayerStateChange()
    {
        playerStateLiveData.observeForever(new Observer<PlayerState>()
        {
            @Override
            public void onChanged(@Nullable PlayerState playerState)
            {
                if(playerState == PlayerState.Waiting)
                {
                    Track track = trackData.getNextTrack(false);
                    if(track != null)
                    {
                        startNewTrack(track.getTrackId());
                    }
                    else
                    {
                        trackData.getLiveDataCurrentTrack().postValue(null);
                        stopPlaying();
                    }
                }
            }
        });
    }
}
