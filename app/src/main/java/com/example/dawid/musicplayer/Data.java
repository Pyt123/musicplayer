package com.example.dawid.musicplayer;

import java.util.ArrayList;
import java.util.List;

public final class Data
{
    private static List<Track> tracks;

    public static List<Track> getTracks()
    {
        return tracks;
    }

    public static void loadTracks()
    {
        tracks = new ArrayList<>();
        tracks.add(new Track(R.raw.arctic_monkeys_do_i_wanna_lnow, "Do I Wanna Know", "Arctic Monkeys", 80));
        tracks.add(new Track(R.raw.jet_are_you_gonna_be_my_girl, "Are You Gonna Be My Girl", "Jet", 75));
        tracks.add(new Track(R.raw.krzysztof_krawczyk_chcialem_byc, "Chciałem być", "Krzysztof Krawczyk", 80));
        tracks.add(new Track(R.raw.muse_feeling_good, "Feeling Good", "Muse", 90));
        tracks.add(new Track(R.raw.parostatek_krzysztof_krawczyk, "Parostatek", "Krzysztof Krawczyk", 126));
    }
}