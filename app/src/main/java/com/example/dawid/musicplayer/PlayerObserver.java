package com.example.dawid.musicplayer;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

public class PlayerObserver implements LifecycleObserver
{
    private Activity ownerActivity;
    private LifecycleOwner lifecycleOwner;

    public PlayerObserver(LifecycleOwner lifecycleOwner)
    {
        this.lifecycleOwner = lifecycleOwner;
        ownerActivity = (Activity)lifecycleOwner;
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resumePlayedMusic()
    {
        CustomMediaPlayer.getInstance().startPlaying();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pausePlayedMusic()
    {
        CustomMediaPlayer.getInstance().pausePlaying();
    }
}
