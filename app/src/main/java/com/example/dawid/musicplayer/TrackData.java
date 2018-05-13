package com.example.dawid.musicplayer;

import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public final class TrackData extends MutableLiveData<TrackData>
{
    private List<Track> tracks;
    private Track currentTrack;

    public List<Track> getTracks()
    {
        return tracks;
    }

    public TrackData()
    {
        initTracks();
    }

    public void initTracks()
    {
        tracks = new ArrayList<>();
        tracks.add(new Track(R.raw.arctic_monkeys_do_i_wanna_lnow, "Do I Wanna Know", "Arctic Monkeys", 265));
        tracks.add(new Track(R.raw.jet_are_you_gonna_be_my_girl, "Are You Gonna Be My Girl", "Jet", 214));
        tracks.add(new Track(R.raw.krzysztof_krawczyk_chcialem_byc, "Chciałem być", "Krzysztof Krawczyk", 221));
        tracks.add(new Track(R.raw.muse_feeling_good, "Feeling Good", "Muse", 198));
        tracks.add(new Track(R.raw.parostatek_krzysztof_krawczyk, "Parostatek", "Krzysztof Krawczyk", 166));
    }

    public void setCurrentTrack(int idOfTrackInData)
    {
        currentTrack = tracks.get(idOfTrackInData);
    }

    public Track getCurrentTrack()
    {
        return currentTrack;
    }
}