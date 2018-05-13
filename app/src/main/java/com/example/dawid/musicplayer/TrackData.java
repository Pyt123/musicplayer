package com.example.dawid.musicplayer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public final class TrackData
{
    private final MutableLiveData<List<Track>> liveDataTrackList = new MutableLiveData<>();
    private final MutableLiveData<Track> liveDataCurrentTrack = new MutableLiveData<>();

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

    public void setCurrentTrack(int idOfTrackInData)
    {
        liveDataCurrentTrack.setValue(liveDataTrackList.getValue().get(idOfTrackInData));
    }

    public Track getCurrentTrack()
    {
        return liveDataCurrentTrack.getValue();
    }

    public List<Track> getTracks()
    {
        return liveDataTrackList.getValue();
    }

    public LiveData<Track> getLiveDataCurrentTrack()
    {
        return liveDataCurrentTrack;
    }

    public LiveData<List<Track>> getLiveDataTrackList()
    {
        return liveDataTrackList;
    }
}