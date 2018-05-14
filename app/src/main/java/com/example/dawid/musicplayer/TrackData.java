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
        tracks.add(new Track(R.raw.arctic_monkeys_do_i_wanna_lnow, "Do I Wanna Know", "Arctic Monkeys", 265));
        tracks.add(new Track(R.raw.jet_are_you_gonna_be_my_girl, "Are You Gonna Be My Girl", "Jet", 214));
        tracks.add(new Track(R.raw.krzysztof_krawczyk_chcialem_byc, "Chciałem być", "Krzysztof Krawczyk", 221));
        tracks.add(new Track(R.raw.muse_feeling_good, "Feeling Good", "Muse", 198));
        tracks.add(new Track(R.raw.parostatek_krzysztof_krawczyk, "Parostatek", "Krzysztof Krawczyk", 166));

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