package com.example.dawid.musicplayer;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;

public class CustomMediaPlayer
{
    public enum PlayerState {TrackNotSet, Prepared, Paused, Playing, TrackEnded}
    public enum Mode { LoopAll, LoopOne, PlayOnceAll, PlayOnce, PlayRandom }
    private long timeToPlayInMilisecs = 1;
    private long lastTime;

    private static CustomMediaPlayer instance = new CustomMediaPlayer();

    private MediaPlayer mediaPlayer;
    private MutableLiveData<PlayerState> playerStateLiveData = new MutableLiveData<>();
    private MutableLiveData<Mode> mode = new MutableLiveData<>();
    private TrackData trackData;
    private MusicService musicService;

    private CustomMediaPlayer()
    {
        mediaPlayer = new MediaPlayer();
        trackData = new TrackData();
        playerStateLiveData.setValue(PlayerState.TrackNotSet);
        mode.setValue(Mode.LoopAll);
        handleEndOfTrack();
    }

    public void provideService(MusicService service)
    {
        musicService = service;
        handleSettingsChange(MusicService.getContext().getSharedPreferences(SettingsActivity.PREFS_NAME, Context.MODE_PRIVATE));
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
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer)
                {
                    playerStateLiveData.postValue(PlayerState.TrackEnded);
                }
            });
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
        if(mode.getValue() == Mode.PlayRandom)
        {
            timeToPlayInMilisecs -= System.currentTimeMillis() - lastTime;
            lastTime = System.currentTimeMillis();
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

    private void handleEndOfTrack()
    {
        playerStateLiveData.observeForever(new Observer<PlayerState>()
        {
            @Override
            public void onChanged(@Nullable PlayerState playerState)
            {
                if(playerState == PlayerState.TrackEnded)
                {
                    Track nextTrack = null;
                    switch (mode.getValue())
                    {
                        case LoopAll:
                            nextTrack = trackData.getNextTrack(true);
                            break;
                        case LoopOne:
                            nextTrack = trackData.getLiveDataCurrentTrack().getValue();
                            break;
                        case PlayOnceAll:
                            nextTrack = trackData.getNextTrack(false);
                            break;
                        case PlayOnce:
                            break;
                        case PlayRandom:
                            timeToPlayInMilisecs -= System.currentTimeMillis() - lastTime;
                            if(timeToPlayInMilisecs > 0)
                            {
                                nextTrack = trackData.getRandomTrack();
                                lastTime = System.currentTimeMillis();
                            }
                            break;
                    }
                    if(nextTrack != null)
                    {
                        startNewTrack(nextTrack.getTrackId());
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

    public void handleSettingsChange(SharedPreferences sharedPrefs)
    {
        int t = sharedPrefs.getInt(SettingsActivity.TIME_KEY, 30);
        int m = sharedPrefs.getInt(SettingsActivity.MODE_KEY, 0);
        switch(m)
        {
            case 0:
                mode.postValue(Mode.LoopAll);
                break;
            case 1:
                mode.postValue(Mode.LoopOne);
                break;
            case 2:
                mode.postValue(Mode.PlayOnceAll);
                break;
            case 3:
                mode.postValue(Mode.PlayOnce);
                break;
            case 4:
                mode.postValue(Mode.PlayRandom);
                lastTime = System.currentTimeMillis();
                break;
        }
        timeToPlayInMilisecs = t * 60000;
    }
}
