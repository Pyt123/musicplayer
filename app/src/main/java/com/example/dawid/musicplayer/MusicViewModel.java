package com.example.dawid.musicplayer;

import android.arch.lifecycle.ViewModel;

public class MusicViewModel extends ViewModel
{
    private TrackData trackData;

    public MusicViewModel()
    {
        super();
        trackData = new TrackData();
    }

    public TrackData getTrackData()
    {
        return trackData;
    }
}
