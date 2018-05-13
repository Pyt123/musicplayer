package com.example.dawid.musicplayer;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class MusicViewModel extends ViewModel
{
    private MutableLiveData<TrackData> trackData;

    public MusicViewModel()
    {
        super();
        trackData = new TrackData();
    }

    public void initMusicData()
    {
        TrackData td = new TrackData();
        this.trackData.setValue(td);
    }

    public MutableLiveData<TrackData> getTrackData()
    {
        return trackData;
    }
}
