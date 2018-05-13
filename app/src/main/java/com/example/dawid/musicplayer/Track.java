package com.example.dawid.musicplayer;

public class Track
{
    private int trackId;
    private String trackTitle;
    private String trackAuthor;
    private int trackLength;

    public Track(int trackId, String trackTitle, String trackAuthor, int trackLength)
    {
        this.trackId = trackId;
        this.trackTitle = trackTitle;
        this.trackAuthor = trackAuthor;
        this.trackLength = trackLength;
    }


    public int getTrackId()
    {
        return trackId;
    }

    public String getTrackTitle()
    {
        return trackTitle;
    }

    public String getTrackAuthor()
    {
        return trackAuthor;
    }

    public String getTrackLength()
    {
        return String.valueOf(trackLength);
    }
}
