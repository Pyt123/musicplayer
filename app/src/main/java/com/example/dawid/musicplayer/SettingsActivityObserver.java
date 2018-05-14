package com.example.dawid.musicplayer;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

public class SettingsActivityObserver implements LifecycleObserver
{
     private SettingsActivity settingsActivity;

     public SettingsActivityObserver(SettingsActivity owner)
     {
         settingsActivity = owner;
     }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void informMediaPlayerAboutSettings()
    {
        CustomMediaPlayer.getInstance().handleSettingsChange(settingsActivity.getSharedPrefs());
    }
}
