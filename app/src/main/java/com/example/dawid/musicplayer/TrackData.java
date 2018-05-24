package com.example.dawid.musicplayer;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class TrackData
{
    private final MutableLiveData<List<Track>> liveDataTrackList = new MutableLiveData<>();
    private final MutableLiveData<Track> liveDataCurrentTrack = new MutableLiveData<>();

    private final Random random = new Random();

    public TrackData()
    {
        initTracks();
    }

    private void initTracks()
    {
        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(new Track(R.raw.darude_sandstorm, "Sandstorm", "Darude", 232));
        tracks.add(new Track(R.raw.golec_uorkiestra_sciernisco, "Sciernisco", "Golec uOrkiestra", 218));
        tracks.add(new Track(R.raw.akcent_przez_twe_oczy_zielone, "Przez twe oczy zielone", "Akcent", 264));
        tracks.add(new Track(R.raw.modern_talking_youre_my_heart_youre_my_soul, "You're my heart, you're my soul", "Modern Talking", 195));
        tracks.add(new Track(R.raw.rick_astley_never_gonna_give_you_up, "Never gonna give you up", "Rick Astley", 212));

        liveDataTrackList.setValue(tracks);
    }

    public void setCurrentTrack(int trackId)
    {
        List<Track> list = liveDataTrackList.getValue();
        for(int i = 0; i < list.size(); i++)
        {
            if(list.get(i).getTrackId() == trackId)
            {
                liveDataCurrentTrack.postValue(liveDataTrackList.getValue().get(i));
                return;
            }
        }
    }

    @Nullable
    public Track getNextTrack(boolean looped)
    {
        Track currentTrack = liveDataCurrentTrack.getValue();
        if(currentTrack == null)
            return null;

        int nextIndex = liveDataTrackList.getValue().indexOf(currentTrack) + 1;
        if(nextIndex < liveDataTrackList.getValue().size())
        {
            return liveDataTrackList.getValue().get(nextIndex);
        }
        else if(looped)
        {
            return liveDataTrackList.getValue().get(nextIndex % liveDataTrackList.getValue().size());
        }
        return null;
    }

    public List<Track> getTracks()
    {
        return liveDataTrackList.getValue();
    }

    public MutableLiveData<Track> getLiveDataCurrentTrack()
    {
        return liveDataCurrentTrack;
    }

    public MutableLiveData<List<Track>> getLiveDataTrackList()
    {
        return liveDataTrackList;
    }

    public Track getRandomTrack()
    {
        int size = getTracks().size();
        if(size > 0)
        {
            return getTracks().get(random.nextInt(size));
        }
        else
        {
            return null;
        }
    }
}